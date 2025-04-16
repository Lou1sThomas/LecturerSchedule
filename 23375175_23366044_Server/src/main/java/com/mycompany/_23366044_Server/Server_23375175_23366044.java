package com.mycompany._23366044_Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server_23375175_23366044 {
    private static ServerSocket servSock;
    private static final int PORT = 6754;
    private static int clientCounter = 0;
    private ServerGUI gui;
    
    // Storage structures for lectures and lecturers
    private static final List<Map<String, String>> lectures = new ArrayList<>();
    private static final List<Map<String, String>> lecturers = new ArrayList<>();
    private static final Set<String> modules = new HashSet<>();
    
    private static class IncorrectActionException extends Exception {
        public IncorrectActionException(String message) {
            super(message);
        }
    }

    public void startServer(ServerGUI gui) {
        this.gui = gui;
        ServerGUI.updateLog("Opening Port...\n");
        try {
            servSock = new ServerSocket(PORT);
            ServerGUI.updateLog("Server started on port " + PORT);
            ServerGUI.updateLog("Waiting for clients...");
        } catch (IOException e) {
            ServerGUI.updateLog("Unable to attach to port.");
            return;
        }
        
        while (true) {
            try {
                Socket clientSocket = servSock.accept();
                clientCounter++;
                ServerGUI.updateLog("Client #" + clientCounter + " connected from " + 
                    clientSocket.getInetAddress().getHostAddress());
                ServerGUI.updateClientCount(clientCounter);
                
                new Thread(new ClientHandler(clientSocket, clientCounter)).start();
            } catch (IOException e) {
                ServerGUI.updateLog("Accept failed: " + e.getMessage());
                break;
            }
        }
    }

    public static void main(String[] args) {
        ServerGUI.main(args);
    }
    
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final int clientId;
        private BufferedReader in;
        private PrintWriter out;
        
        public ClientHandler(Socket socket, int id) {
            this.clientSocket = socket;
            this.clientId = id;
        }
        
        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                
                out.println("WELCOME_TO_SYSTEM");
                ServerGUI.updateLog("Handling client #" + clientId);
                
                boolean running = true;
                while (running) {
                    String message = in.readLine();
                    if (message == null) {
                        ServerGUI.updateLog("Client " + clientId + " disconnected");
                        break;
                    }
                    
                    ServerGUI.updateLog("Message received from client #" + clientId + ": " + message);
                    
                    if (message.equals("QUIT")) {
                        out.println("Closing Lecture Scheduler");
                        ServerGUI.updateLog("Client " + clientId + " closed connection");
                        running = false;
                    } else {
                        processMessageHandler(message, out);
                    }
                }
            } catch (IOException e) {
                ServerGUI.updateLog("Error handling client #" + clientId + ": " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    clientCounter--;
                    ServerGUI.updateLog("Client #" + clientId + " disconnected");
                    ServerGUI.updateClientCount(clientCounter);
                } catch (IOException e) {
                    ServerGUI.updateLog("Error closing client socket: " + e.getMessage());
                }
            }
        }
        
        private void processMessageHandler(String message, PrintWriter out) {
            try {
                if (message.startsWith("REQUEST_OTHER_SERVICE:")) {
                    String requestedService = message.substring("REQUEST_OTHER_SERVICE:".length());
                    ServerGUI.updateLog("Client requested unsupported service: " + requestedService);
                    throw new IncorrectActionException("Unsupported service requested: " + requestedService);
                }

                if (message.startsWith("ADD_LECTURE:")) {
                    String lectureData = message.substring("ADD_LECTURE:".length());
                    processAddLecture(lectureData, out);
                    return;
                }
                
                if (message.startsWith("ADD_MODULE:")) {
                    String moduleData = message.substring("ADD_MODULE:".length());
                    processAddModule(moduleData, out);
                    return;
                }
                
                if (message.startsWith("ADD_LECTURER:")) {
                    String lecturerData = message.substring("ADD_LECTURER:".length());
                    processAddLecturer(lecturerData, out);
                    return;
                }

                switch (message) {
                    case "ENTER_SYSTEM":
                        out.println("WELCOME_TO_SYSTEM");
                        ServerGUI.updateLog("Client entered the system");
                        break;
                    case "OPEN_LECTURE_TIMETABLE":
                        out.println("SHOW_LECTURE_TIMETABLE");
                        ServerGUI.updateLog("Client opened the Lecture Timetable");
                        break;
                    case "OPEN_ADD_LECTURE":
                        out.println("ADD_LECTURE_MENU");
                        ServerGUI.updateLog("Client has entered the System Menu");
                        break;
                    case "OPEN_REMOVE_LECTURE":
                        out.println("REMOVE_LECTURE_MENU");
                        ServerGUI.updateLog("Client has opened remove Lecture Menu");
                        break;
                    case "FETCH_LECTURES":
                        sendLectures(out);
                        ServerGUI.updateLog("Sending lectures to client");
                        break;
                    default:
                        if (!message.contains("REQUEST_OTHER_SERVICE:")) {
                            out.println("Unknown Command: " + message);
                        }
                        break;
                }
            } catch (IncorrectActionException e) {
                ServerGUI.updateLog("IncorrectActionException: " + e.getMessage());
                out.println("ERROR_UNSUPPORTED_SERVICE:" + e.getMessage());
            }
        }
        
        private void processAddLecture(String lectureData, PrintWriter out) {
            String[] parts = lectureData.split(",");
            if (parts.length >= 6) {
                Map<String, String> lecture = new HashMap<>();
                lecture.put("module", parts[0]);
                lecture.put("date", parts[1]);
                lecture.put("time", parts[2]);
                lecture.put("room", parts[3]);
                lecture.put("lecturer", parts[4]);
                lecture.put("type", parts[5]);
                
                boolean hasConflict = lectures.stream().anyMatch(l -> 
                        l.get("date").equals(parts[1]) &&
                        l.get("time").equals(parts[2]) &&
                        l.get("room").equals(parts[3]));
                
                if (hasConflict) {
                    out.println("ERROR: Lecture time and room conflict");
                } else {
                    lectures.add(lecture);
                    out.println("SUCCESS: " + parts[5] + " added successfully for " + parts[0]);
                    ServerGUI.updateLog("Lecture added: " + parts[5] + " for " + parts[0]);
                }
            } else {
                out.println("ERROR: Invalid lecture data format");
            }
        }
        
        private void processAddModule(String moduleData, PrintWriter out) {
            if (modules.size() >= 5) {
                out.println("ERROR: Maximum 5 modules allowed");
            } else if (modules.contains(moduleData)) {
                out.println("ERROR: Module already exists");
            } else {
                modules.add(moduleData);
                out.println("SUCCESS: Module added: " + moduleData);
                ServerGUI.updateLog("Module added: " + moduleData);
            }
        }
        
        private void processAddLecturer(String lecturerData, PrintWriter out) {
            String[] parts = lecturerData.split(",");
            if (parts.length >= 2) {
                String name = parts[0];
                String module = parts[1];
                if (!modules.contains(module)) {
                    out.println("ERROR: Module does not exist");
                    return;
                }
                boolean lecturerExists = lecturers.stream().anyMatch(l -> 
                        l.get("name").equals(name) && l.get("module").equals(module));
                if (lecturerExists) {
                    out.println("ERROR: Lecturer already exists for this module");
                } else {
                    Map<String, String> lecturer = new HashMap<>();
                    lecturer.put("name", name);
                    lecturer.put("module", module);
                    lecturers.add(lecturer);
                    out.println("SUCCESS: Lecturer added: " + name + " for " + module);
                    ServerGUI.updateLog("Lecturer added: " + name + " for " + module);
                }
            } else {
                out.println("ERROR: Invalid lecturer data format");
            }
        }
        
        private void sendLectures(PrintWriter out) {
            if (lectures.isEmpty()) {
                out.println("INFO: No lectures available");
            } else {
                out.println("LECTURES_COUNT:" + lectures.size());
                for (Map<String, String> lecture : lectures) {
                    String lectureStr = String.format("LECTURE:%s,%s,%s,%s,%s,%s",
                        lecture.get("module"),
                        lecture.get("date"),
                        lecture.get("time"),
                        lecture.get("room"),
                        lecture.get("lecturer"),
                        lecture.get("type"));
                    out.println(lectureStr);
                }
            }
        }
    }
}