/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.server_23375175_23366044;

import java.io.*;
import static java.lang.System.out;
import java.net.*;
import java.util.*;

public class Server_23375175_23366044 {
    private static ServerSocket servSock;
    private static final int PORT = 6754;
    private static int clientCounter = 0;
    
    // Storage structures for lectures and lecturers
    private static final List<Map<String, String>> lectures = new ArrayList<>();
    private static final List<Map<String, String>> lecturers = new ArrayList<>();
    private static final Set<String> modules = new HashSet<>();
    
    // Define the custom exception class
    private static class IncorrectActionException extends Exception {
        public IncorrectActionException(String message) {
            super(message);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Opening Port...\n");
        try {
            servSock = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            System.out.println("Waiting for clients...");
        } catch (IOException e) {
            System.out.println("Unable to attach to port.");
            System.exit(1);
        }
        
        while (true) {
            try {
                Socket clientSocket = servSock.accept();
                clientCounter++;
                System.out.println("Client #" + clientCounter + " connected from " + clientSocket.getInetAddress().getHostAddress());
                
                new Thread(new ClientHandler(clientSocket, clientCounter)).start();
            } catch (IOException e) {
                System.out.println("Accept failed: " + e.getMessage());
                break;
            }
        }
        
        try {
            servSock.close();
        } catch (IOException e) {
            System.out.println("Could not close server socket: " + e.getMessage());
        }
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
                System.out.println("Handling client #" + clientId);
                
                boolean running = true;
                while (running) {
                    String message = in.readLine();
                    if (message == null) {
                        System.out.println("Client " + clientId + " disconnected");
                        break;
                    }
                    
                    System.out.println("Message received from client #" + clientId + ": " + message);
                    
                    if (message.equals("QUIT")) {
                        out.println("Closing Lecture Scheduler");
                        System.out.println("Client " + clientId + " closed connection");
                        running = false;
                    } else {
                        processMessageHandler(message, out);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error handling client #" + clientId + ": " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client #" + clientId + " disconnected");
                } catch (IOException e) {
                    System.out.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
        
        private void processMessageHandler(String message, PrintWriter out) {
            try {
                // Check if it's a custom service request
                if (message.startsWith("REQUEST_OTHER_SERVICE:")) {
                    String requestedService = message.substring("REQUEST_OTHER_SERVICE:".length());
                    System.out.println("Client requested unsupported service: " + requestedService);
                    throw new IncorrectActionException("Unsupported service requested: " + requestedService);
                }

                // Handle add lecture message
                if (message.startsWith("ADD_LECTURE:")) {
                    String lectureData = message.substring("ADD_LECTURE:".length());
                    processAddLecture(lectureData, out);
                    return;
                }
                
                // Handle add module message
                if (message.startsWith("ADD_MODULE:")) {
                    String moduleData = message.substring("ADD_MODULE:".length());
                    processAddModule(moduleData, out);
                    return;
                }
                
                // Handle add lecturer message
                if (message.startsWith("ADD_LECTURER:")) {
                    String lecturerData = message.substring("ADD_LECTURER:".length());
                    processAddLecturer(lecturerData, out);
                    return;
                }

                // Handle standard commands
                switch (message) {
                    case "ENTER SYSTEM":
                        out.println("WELCOME_TO_SYSTEM");
                        System.out.println("Server: Client entered the system");
                        break;
                    case "OPEN_LECTURE_TIMETABLE":
                        out.println("SHOW_LECTURE_TIMETABLE");
                        System.out.println("Server: Client opened the Lecture Timetable");
                        System.out.println("Opening timetable...\n");
                        break;
                    case "OPEN_ADD_LECTURE":
                        out.println("ADD_LECTURE_MENU");
                        System.out.println("Server: Client has entered the System Menu");
                        break;
                    case "OPEN_REMOVE_LECTURE":
                        out.println("REMOVE_LECTURE_MENU");
                        System.out.println("Server: Client has opened remove Lecture Menu");
                        break;
                    case "ADD_LECTURER":
                        out.println("LECTURER_ADDED");
                        System.out.println("Server: Lecturer added successfully");
                        break;
                    case "REMOVE_LECTURER":
                        out.println("LECTURER_REMOVED");
                        System.out.println("Server: Lecturer removed successfully");
                        break;
                    case "DISPLAY_LECTURERS":
                        out.println("DISPLAYING_LECTURERS");
                        System.out.println("Server: Displaying all lecturers");
                        break;
                    case "MAIN_MENU":
                        out.println("MAIN_MENU");
                        System.out.println("Server: Lecturer has entered the MainMenu");
                        break;
                    case "OPEN_OTHER_MENU":
                        out.println("OTHER_MENU");
                        System.out.println("Opening other services menu...");
                        break;
                    case "FETCH_LECTURES":
                        sendLectures(out);
                        System.out.println("Server: Sending lectures to client");
                        break;
                    default:
                        if (!message.contains("REQUEST_OTHER_SERVICE:")) {
                            out.println("Unknown Command: " + message);
                        }
                        break;
                }
            } catch (IncorrectActionException e) {
                System.out.println("IncorrectActionException: " + e.getMessage());
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
                    System.out.println("Server: Lecture added: " + parts[5] + " for " + parts[0]);
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
                System.out.println("Server: Module added: " + moduleData);
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
                    System.out.println("Server: Lecturer added: " + name + " for " + module);
                }
            } else {
                out.println("ERROR: Invalid lecturer data format");
            }
        }
        
        private void sendLectures(PrintWriter out) {
            if (lectures.isEmpty()) {
                out.println("INFO: No lectures available");
            } else {
                // Send the number of lectures first
                out.println("LECTURES_COUNT:" + lectures.size());
                
                // Then send each lecture
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
    
    

