/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._23366044_Client;

/**
 *
 * @author louis
 */

// View Class
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author louis
 */


public class LectureSchedulerGUI extends Application {
    private final LecturerManager lectureManager;
    private TextArea responseArea;
    private ListView<String> moduleList;
    private ComboBox<String> moduleSelect;
    private TextField moduleInput;
    private TextField lecturerNameField;
    private Stage primaryStage;
    private ClientServer clientServer;
    
    // Added variable to store the lecture type
    private String lectureType = "Lecture"; // Default type

    public LectureSchedulerGUI() {
        // Use the singleton to get the shared instance
        this.lectureManager = LecturerManagerSingleton.getInstance();
        // Get the client server instance
        this.clientServer = ClientGUIWrapper.getClientServer();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("LM051-2026 Lecture Scheduler");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        setupMainLayout(mainLayout);

        Scene scene = new Scene(mainLayout, 600, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Load existing modules when the screen initializes
        updateModuleLists();
    }

    private void setupMainLayout(VBox mainLayout) {
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

        mainLayout.getChildren().addAll(
                new Label("Course: LM051-2026"),
                createModuleSection(),
                new Separator(),
                createLectureSection(),
                new Separator(),
                createResponseSection(),
                backButton
        );
    }

    private VBox createModuleSection() {
        VBox moduleSection = new VBox(10);
        moduleSection.setAlignment(Pos.CENTER_LEFT);

        moduleInput = new TextField();
        moduleInput.setPromptText("Enter module name");
        Button addModuleButton = new Button("Add Module");

        addModuleButton.setOnAction(e -> handleAddModule());

        moduleList = new ListView<>();
        moduleList.setPrefHeight(100);

        moduleSection.getChildren().addAll(
                new Label("Module Management"),
                new HBox(10, moduleInput, addModuleButton),
                moduleList
        );

        return moduleSection;
    }

private void handleAddModule() {
    String module = moduleInput.getText();
    if (module == null || module.isEmpty()) {
        showAlert("Error", "Please enter a module name");
        return;
    }
    
    // Send to server first
    if (clientServer != null) {
        clientServer.sendMessage("ADD_MODULE:" + module);
    } else {
        showAlert("Error", "Not connected to server");
    }
    
    // Update local UI (this will be confirmed by server response)
    if (lectureManager.addModule(module)) {
        updateModuleLists();
        moduleInput.clear();
    } else {
        showAlert("Error", "Cannot add module. Maximum 5 modules allowed.");
    }
}

    private GridPane createLectureSection() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        // Add Label for lecture section
        Label sectionLabel = new Label("Add Lecture");
        sectionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        moduleSelect = new ComboBox<>();
        moduleSelect.setPromptText("Select Module");

        // Add lecturer name field
        lecturerNameField = new TextField();
        lecturerNameField.setPromptText("Enter lecturer name");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");

        ComboBox<String> timeSelect = new ComboBox<>(
                FXCollections.observableArrayList(UIConstants.TIME_SLOTS)
        );
        timeSelect.setPromptText("Select Time");

        ComboBox<String> roomSelect = new ComboBox<>(
                FXCollections.observableArrayList(UIConstants.ROOMS)
        );
        roomSelect.setPromptText("Select Room");
        
        // Create buttons for lecture types
        Button lectureButton = new Button("Lecture");
        Button tutorialButton = new Button("Tutorial");
        Button labButton = new Button("Lab");

        // Set actions for lecture type buttons
        lectureButton.setOnAction(e -> {
            lectureType = "Lecture";
            updateLectureTypeButtons(lectureButton, tutorialButton, labButton);
        });
        
        tutorialButton.setOnAction(e -> {
            lectureType = "Tutorial";
            updateLectureTypeButtons(tutorialButton, lectureButton, labButton);
        });
        
        labButton.setOnAction(e -> {
            lectureType = "Lab";
            updateLectureTypeButtons(labButton, lectureButton, tutorialButton);
        });
        
        // Set default button style
        lectureButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        tutorialButton.setStyle("-fx-background-color: #f0f0f0;");
        labButton.setStyle("-fx-background-color: #f0f0f0;");
        
        // Create an HBox for the lecture type buttons
        HBox lectureTypeBox = new HBox(10, lectureButton, tutorialButton, labButton);
        lectureTypeBox.setAlignment(Pos.CENTER_LEFT);

        Button addLectureButton = new Button("Add Session");
        addLectureButton.setOnAction(e -> handleAddLectureSubmission(
                moduleSelect.getValue(),
                lecturerNameField.getText(),
                datePicker.getValue(),
                timeSelect.getValue(),
                roomSelect.getValue()
        ));

        grid.add(sectionLabel, 0, 0, 2, 1);
        grid.addRow(1, new Label("Select Module:"), moduleSelect);
        grid.addRow(2, new Label("Lecturer Name:"), lecturerNameField);
        grid.addRow(3, new Label("Select Date:"), datePicker);
        grid.addRow(4, new Label("Select Time:"), timeSelect);
        grid.addRow(5, new Label("Select Room:"), roomSelect);
        grid.add(new Label("Session Type:"), 0, 6);
        grid.add(lectureTypeBox, 1, 6);
        grid.add(addLectureButton, 1, 7);

        return grid;
    }
    
    // Helper method to update button styles based on selection
    private void updateLectureTypeButtons(Button selectedButton, Button button1, Button button2) {
        selectedButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        button1.setStyle("-fx-background-color: #f0f0f0;");
        button2.setStyle("-fx-background-color: #f0f0f0;");
    }


    private void handleAddLectureSubmission(String module, String lecturerName, LocalDate date,
                                         String timeStr, String room) {
        if (!validateLectureInputs(module, lecturerName, date, timeStr, room)) {
            return;
        }

        LocalTime time = LocalTime.parse(timeStr);

        // First add lecturer if it doesn't exist
        if (!lecturerNameField.getText().trim().isEmpty()) {
            if (lectureManager.addLecturer(lecturerName, module)) {
                // Notify the server that a lecturer was added
                if (clientServer != null) {
                    clientServer.sendMessage("ADD_LECTURER");
                }
            }
        }

        // Then add lecture with session type
        handleAddLecture(module, date, time, room, lecturerName);
    }

private void handleAddLecture(String module, LocalDate date, LocalTime time, String room, String lecturerName) {
    // Format the data to send to the server
    String lectureData = String.format("%s,%s,%s,%s,%s,%s", 
                                    module, 
                                    date.toString(), 
                                    time.toString(), 
                                    room, 
                                    lecturerName,
                                    lectureType);
    
    // Send the data to the server
    if (clientServer != null) {
        clientServer.sendMessage("ADD_LECTURE:" + lectureData);
        
        // The response will be handled by the ClientServer class
        // For now, we'll also update locally for UI feedback
        if (lectureManager.addLecture(module, date, time, room, lectureType)) {
            Lecture lecture = new Lecture(module, date, time, room, lectureType);
            updateResponse(lectureType + " added successfully:\n" + lecture + 
                         "\nLecturer: " + lecturerName);

            // Clear the lecturer name field after successful addition
            lecturerNameField.clear();
        } else {
            showAlert("Error", "Cannot add " + lectureType.toLowerCase() + ". Check if there's a conflict or if the module exists.");
        }
    } else {
        showAlert("Error", "Not connected to server");
    }
}

    private boolean validateLectureInputs(String module, String lecturerName, LocalDate date, 
                                        String time, String room) {
        if (module == null || date == null || time == null || room == null) {
            showAlert("Error", "Please fill in all required fields");
            return false;
        }
        
        if (lecturerName == null || lecturerName.trim().isEmpty()) {
            showAlert("Error", "Please enter a lecturer name");
            return false;
        }
        
        return true;
    }

    private void updateModuleLists() {
        List<String> modules = lectureManager.getModules();
        moduleList.setItems(FXCollections.observableArrayList(modules));
        moduleSelect.setItems(FXCollections.observableArrayList(modules));
    }

    private VBox createResponseSection() {
        VBox responseSection = new VBox(5);
        responseArea = new TextArea();
        responseArea.setEditable(false);
        responseArea.setPrefRowCount(5);

        responseSection.getChildren().addAll(
                new Label("Terminal:"),
                responseArea
        );

        return responseSection;
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
