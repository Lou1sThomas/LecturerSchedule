/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveLecturerGUI extends Application {
    private final LecturerManager lectureManager;
    private TextField lecturerNameField;
    private ComboBox<String> moduleSelect;
    private TextArea responseArea;
    private Stage primaryStage;
    private ClientServer clientServer;

    public RemoveLecturerGUI() {
        // Use the singleton to get the shared instance
        this.lectureManager = LecturerManagerSingleton.getInstance();
        // Get the client server instance
        this.clientServer = ClientGUIWrapper.getClientServer();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Remove Lecturer");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));

        setupMainLayout(mainLayout);

        Scene scene = new Scene(mainLayout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Load modules when opening
        updateModuleList();
    }

    private void setupMainLayout(VBox mainLayout) {
        // Create title label
        Label titleLabel = new Label("Remove Lecturer");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Create lecturer name field
        lecturerNameField = new TextField();
        lecturerNameField.setPromptText("Enter lecturer name");
        
        // Create module selection
        moduleSelect = new ComboBox<>();
        moduleSelect.setPromptText("Select Module");
        
        // Create remove button
        Button removeButton = new Button("Remove Lecturer");
        removeButton.setOnAction(e -> handleRemoveLecturer());
        
        // Create display lecturers button
        Button displayButton = new Button("Display All Lecturers");
        displayButton.setOnAction(e -> displayAllLecturers());
        
        // Create back button
        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> {
            MainMenu mainMenu = new MainMenu();
            try {
                mainMenu.start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        // Create response area
        responseArea = new TextArea();
        responseArea.setEditable(false);
        responseArea.setPrefRowCount(10);
        
        // Create an option to also remove associated lectures
        CheckBox removeAssociatedLectures = new CheckBox("Also remove all associated lectures");
        removeAssociatedLectures.setSelected(true); // Selected by default
        
        // Organize layout
        HBox inputBox = new HBox(10, lecturerNameField, moduleSelect);
        inputBox.setAlignment(Pos.CENTER);
        
        HBox buttonBox = new HBox(10, removeButton, displayButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        mainLayout.getChildren().addAll(
                titleLabel,
                new Label("Enter lecturer details to remove:"),
                inputBox,
                removeAssociatedLectures,
                buttonBox,
                new Label("Response:"),
                responseArea,
                backButton
        );
        
        mainLayout.setAlignment(Pos.CENTER);
    }
    
    private void handleRemoveLecturer() {
        String lecturerName = lecturerNameField.getText();
        String module = moduleSelect.getValue();
        
        if (lecturerName.trim().isEmpty() || module == null) {
            showAlert("Error", "Please enter both lecturer name and select a module");
            return;
        }
        
        // First, check if the lecturer exists
        boolean lecturerExists = false;
        for (Lecturer lecturer : lectureManager.getAllLecturers()) {
            if (lecturer.getName().equals(lecturerName) && lecturer.getModule().equals(module)) {
                lecturerExists = true;
                break;
            }
        }
        
        if (!lecturerExists) {
            updateResponse("Could not find lecturer: " + lecturerName + " for module: " + module);
            return;
        }
        
        // Get all lectures for this module before removing them
        List<Lecture> moduleLectures = lectureManager.getLecturesByModule(module);
        
        // Remove the lecturer
        boolean lecturerRemoved = lectureManager.removeLecturer(lecturerName, module);
        
        if (lecturerRemoved) {
            StringBuilder responseMessage = new StringBuilder();
            responseMessage.append("Lecturer successfully removed: ").append(lecturerName)
                          .append(" from ").append(module).append("\n");
            
            // Now remove all related lectures for this module
            int removedLecturesCount = 0;
            for (Lecture lecture : moduleLectures) {
                // Remove each lecture
                if (lectureManager.removeLecture(lecture.getModule(), lecture.getDate(), 
                                               lecture.getTime(), lecture.getRoom(), lecture.getSessionType())) {
                    removedLecturesCount++;
                    responseMessage.append("Removed ").append(lecture.getSessionType())
                                   .append(" for ").append(lecture.getModule())
                                   .append(" on ").append(lecture.getDate())
                                   .append(" at ").append(lecture.getTime())
                                   .append(" in Room ").append(lecture.getRoom())
                                   .append("\n");
                }
            }
            
            if (removedLecturesCount > 0) {
                responseMessage.append("\nTotal removed sessions: ").append(removedLecturesCount);
            } else {
                responseMessage.append("No associated sessions found for this lecturer and module.");
            }
            
            updateResponse(responseMessage.toString());
            
            // Notify the server
            if (clientServer != null) {
                clientServer.sendMessage("REMOVE_LECTURER_AND_LECTURES");
            }
            
            lecturerNameField.clear();
        } else {
            updateResponse("Failed to remove lecturer: " + lecturerName);
        }
    }
    
    private void displayAllLecturers() {
        List<Lecturer> lecturerList = lectureManager.getAllLecturers();
        
        // Notify the server
        if (clientServer != null) {
            clientServer.sendMessage("DISPLAY_LECTURERS");
        }
        
        if (lecturerList.isEmpty()) {
            updateResponse("No lecturers found in the system.");
            return;
        }
        
        StringBuilder sb = new StringBuilder("Current Lecturers:\n\n");
        for (Lecturer lecturer : lecturerList) {
            sb.append(lecturer.toString()).append("\n");
            
            // Get associated lectures for this lecturer
            List<Lecture> associatedLectures = lectureManager.getLecturesByModule(lecturer.getModule());
            if (!associatedLectures.isEmpty()) {
                sb.append("  Associated Sessions:\n");
                for (Lecture lecture : associatedLectures) {
                    sb.append("  - ").append(lecture.getSessionType())
                      .append(" on ").append(lecture.getDate())
                      .append(" at ").append(lecture.getTime())
                      .append(" in Room ").append(lecture.getRoom())
                      .append("\n");
                }
                sb.append("\n");
            }
        }
        updateResponse(sb.toString());
    }
    
    private void updateModuleList() {
        List<String> modules = lectureManager.getModules();
        moduleSelect.setItems(FXCollections.observableArrayList(modules));
    }
    
    private void updateResponse(String message) {
        responseArea.setText(message);
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
