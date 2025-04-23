
package com.mycompany._23366044_Client;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.application.Application;
import javafx.scene.control.Alert;

/**
 *
 * @author oranc
 */

// Model class
public class ClientServer {
    private static InetAddress host;
    private static final int PORT = 6754;
    private static Socket link;
    private static BufferedReader in;
    private static PrintWriter out;
    
    public ClientServer(){
        try{
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException e){
            System.out.println("Host not found");
            System.exit(1);
        }
    }
    
    public void connectToServer(){				
        try {
            link = new Socket(host, PORT);		
            
            in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            out = new PrintWriter(link.getOutputStream(), true);	 
            System.out.println("Connected to server");
            
            // Read initial welcome message
            String welcomeMsg = in.readLine();
            if (welcomeMsg != null) {
                System.out.println("Server welcome: " + welcomeMsg);
                handleServerResponse(welcomeMsg);
            }
        } catch (IOException e) {
            System.out.println("Unable to connect to server");
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText("Unable to connect to server");
                alert.setContentText("Please ensure the server is running before starting the client application.");
                alert.showAndWait();
                System.exit(1);
            });
        }
    }
    
    public void sendMessage(String message){
        if (out != null){
            out.println(message);
            System.out.println("Message sent to server: " + message);
            listenForResponse(); // waits for server to respond
        } else {
            System.out.println("Not connected to server");
            showErrorMessage("Client is not connected to the server");
        }
    }
   
    private void listenForResponse() {
        try {
            String response = in.readLine();
            if(response != null) {
                System.out.println("Server response: " + response);
                handleServerResponse(response);
                
                // Check if this is a response that will be followed by more data
                if(response.startsWith("LECTURES_COUNT:")) {
                    // Parse the count of lectures to expect
                    int count = Integer.parseInt(response.substring("LECTURES_COUNT:".length()));
                    
                    // Read the expected number of lecture messages
                    for(int i = 0; i < count; i++) {
                        String lectureData = in.readLine();
                        if(lectureData != null) {
                            System.out.println("Server response: " + lectureData);
                            handleServerResponse(lectureData);
                        } else {
                            System.out.println("Server disconnected while sending lectures");
                            closeConnection();
                            break;
                        }
                    }
                }
            } else {
                System.out.println("Server disconnected");
                closeConnection();
            }
        } 
        catch (IOException e) {
            System.out.println("Error reading response.");
            e.printStackTrace();
            showErrorMessage("Error reading server response: " + e.getMessage());
        }
    }
   
    private void handleServerResponse(String response) {
        if (response.startsWith("ERROR_UNSUPPORTED_SERVICE:")) {
            String errorMessage = response.substring("ERROR_UNSUPPORTED_SERVICE:".length());
            System.out.println("Server error: " + errorMessage);
            showErrorMessage(errorMessage);
            return;
        }
        
        if (response.startsWith("SUCCESS:")) {
            String successMessage = response.substring("SUCCESS:".length());
            System.out.println("Server success: " + successMessage);
            showSuccessMessage(successMessage);
            return;
        }
        
        if (response.startsWith("ERROR:")) {
            String errorMessage = response.substring("ERROR:".length());
            System.out.println("Server error: " + errorMessage);
            showErrorMessage(errorMessage);
            return;
        }
        
        if (response.startsWith("LECTURES_COUNT:")) {
            int count = Integer.parseInt(response.substring("LECTURES_COUNT:".length()));
            System.out.println("Server will send " + count + " lectures");
            return;
        }
        
        if (response.startsWith("LECTURE:")) {
        String lectureData = response.substring("LECTURE:".length());
        System.out.println("Received lecture: " + lectureData);
        
        // Parse and add lecture to local manager
        String[] parts = lectureData.split(",");
        if (parts.length >= 6) {
            try {
                String module = parts[0];
                LocalDate date = LocalDate.parse(parts[1]);
                LocalTime time = LocalTime.parse(parts[2]);
                String room = parts[3];
                String lecturer = parts[4];
                String type = parts[5];
                
                LecturerManager manager = LecturerManagerSingleton.getInstance();
                manager.addLecture(module, date, time, room, type);
            } catch (Exception e) {
                System.err.println("Error parsing lecture data: " + e.getMessage());
                }
            }
        return;
        }
        
        if (response.equals("INFO: No lectures available")) {
            System.out.println("No lectures available on server");
            showInfoMessage("No lectures are currently scheduled on the server.");
            return;
        }
        
        switch (response) {
            case "WELCOME_TO_SYSTEM":
                
                System.out.println("Successfully connected to the timetable system");
                break;
            case "SHOW_LECTURE_TIMETABLE":
                System.out.println("Opening lecture timetable view");
                break;
            case "ADD_LECTURE_MENU":
                System.out.println("Opening add lecture menu");
                
                break;
            case "REMOVE_LECTURE_MENU":
                System.out.println("Opening remove lecture menu");
                break;
            case "MAIN_MENU":
                System.out.println("Returning to main menu");
                break;
            case "LECTURER_ADDED":
                System.out.println("Server confirmed: Lecturer has been added");
                break;
            case "LECTURER_REMOVED":
                System.out.println("Server confirmed: Lecturer has been removed");
                break;
            case "DISPLAYING_LECTURERS":
                System.out.println("Server confirmed: Displaying all lecturers");
                break;
            case "OTHER_MENU":
                System.out.println("Opening other service menu");
                break;
            case "SUCCESS: Lectures have been optimized for earlier times":
                showSuccessMessage("Lectures have been successfully moved to earlier time slots.");
                break;
            case "INFO: Lectures are already optimized for earliest possible times":
            showInfoMessage("Lectures are already scheduled at the earliest possible times.");
                break;
            default:
                System.out.println("Unknown server response: " + response);
                break;
        }
    }
   
    public void fetchLectures() {
       if (out != null) {
        // Clear existing lectures before fetching new ones
        LecturerManager manager = LecturerManagerSingleton.getInstance();
        manager.clearLectures();
        
        out.println("FETCH_LECTURES");
        System.out.println("Requested lectures from server");
        listenForResponse();
    } else {
        System.out.println("Not connected to server");
        showErrorMessage("Cannot fetch lectures: Not connected to server");
    }
}
   
    private static void closeConnection() {
        try {
            System.out.println("\nClosing Connection...");
            if(link != null) {
                link.close();
            }
        }
        catch (IOException e) {
            System.out.println("Unable to disconnect from server");
            System.exit(1);
        }
    }
   
    private void showErrorMessage(String message) {
        // We need to use Platform.runLater since this might be called from a non-JavaFX thread
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Exception");
            alert.setHeaderText("Error");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
   
    public static void main(String[] args) {
        ClientServer clientServer = new ClientServer();
        clientServer.connectToServer();
        
        ClientGUIWrapper.setClientServer(clientServer);
        Application.launch(ClientGUIWrapper.class);
    }
   
    private void showSuccessMessage(String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Operation Successful");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
   
    private void showInfoMessage(String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Server Information");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
