/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

/**
 *
 * @author louis
 */

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

public class LectureSchedulerGUI extends Application {
    private final LecturerManager lectureManager;
    private TextArea responseArea;
    private ListView<String> moduleList;
    private ComboBox<String> moduleSelect;
    private TextField lecturerNameField;
    private TextField moduleInput;
    private Stage primaryStage;

    public LectureSchedulerGUI() {
        // Use the singleton to get the shared instance
        this.lectureManager = LecturerManagerSingleton.getInstance();
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
        if (lectureManager.addModule(module)) {
            updateModuleLists();
            moduleInput.clear();
        } else {
            showAlert("Error", "Cannot add module. Maximum 5 modules allowed.");
        }
    }


    private void handleLecturerSubmission(String action, String module, String lecturerName) {
        if (action == null) {
            showAlert("Error", "Please select an action.");
            return;
        }

        if (!validateLecturerInputs(action, module, lecturerName)) {
            return;
        }

        switch (action) {
            case "Add Lecturer":
                if (lectureManager.addLecturer(lecturerName, module)) {
                    updateResponse("Lecturer added successfully");
                    // Clear the input field after successful addition
                    lecturerNameField.clear();
                } else {
                    showAlert("Error", "Cannot add lecturer.");
                }
                break;
            case "Remove Lecturer":
                if (lectureManager.removeLecturer(lecturerName, module)) {
                    updateResponse("Lecturer removed successfully");
                } else {
                    updateResponse("Lecturer not found");
                }
                break;
            case "Display Lecturers":
                getAllLecturers();
                break;
        }
    }

    private GridPane createLectureSection() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        // Lecture Action Controls
        ComboBox<String> actionSelect = new ComboBox<>(
                FXCollections.observableArrayList(UIConstants.LECTURE_ACTIONS)
        );
        actionSelect.setPromptText("Select Action");

        moduleSelect = new ComboBox<>();
        moduleSelect.setPromptText("Select Module");

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

        Button submitButton = new Button("Submit Request");
        submitButton.setOnAction(e -> handleLectureSubmission(
                actionSelect.getValue(),
                moduleSelect.getValue(),
                datePicker.getValue(),
                timeSelect.getValue(),
                roomSelect.getValue()
        ));

        grid.add(new Label("Lecture Management"), 0, 0, 2, 1);
        grid.addRow(1, new Label("Select Action:"), actionSelect);
        grid.addRow(2, new Label("Select Module:"), moduleSelect);
        grid.addRow(3, new Label("Select Date:"), datePicker);
        grid.addRow(4, new Label("Select Time:"), timeSelect);
        grid.addRow(5, new Label("Select Room:"), roomSelect);
        grid.add(submitButton, 1, 6);

        return grid;
    }

    private void handleLectureSubmission(String action, String module, LocalDate date,
                                         String timeStr, String room) {
        if (action == null) {
            showAlert("Error", "Please select an action.");
            return;
        }

        if (!validateLectureInputs(action, module, date, timeStr, room)) {
            return;
        }

        LocalTime time = LocalTime.parse(timeStr);

        switch (action) {
            case "Add Lecture":
                handleAddLecture(module, date, time, room);
                break;
            case "Remove Lecture":
                handleRemoveLecture(module, date, time, room);
                break;
            case "Display Lectures":
                displayLectures();
                break;
        }
    }

    private void handleAddLecture(String module, LocalDate date, LocalTime time, String room) {
        if (lectureManager.addLecture(module, date, time, room)) {
            updateResponse("Lecture added successfully:\n" +
                    new Lecture(module, date, time, room));
        } else {
            showAlert("Error", "Cannot add lecture.");
        }
    }

    private void handleRemoveLecture(String module, LocalDate date, LocalTime time, String room) {
        if (lectureManager.removeLecture(module, date, time, room)) {
            updateResponse("Lecture removed successfully");
        } else {
            updateResponse("Lecture not found");
        }
    }

    private void getAllLecturers() {
        List<Lecturer> lecturerList = lectureManager.getAllLecturers();
        if (lecturerList.isEmpty()) {
            updateResponse("No lecturers found.");
            return;
        }

        StringBuilder lecturerBuilder = new StringBuilder("Current Lecturers:\n\n");
        for (Lecturer lecturer : lecturerList) {
            lecturerBuilder.append(lecturer.toString()).append("\n");
        }
        updateResponse(lecturerBuilder.toString());
    }

    private void displayLectures() {
        List<Lecture> lectures = lectureManager.getAllLectures();
        if (lectures.isEmpty()) {
            updateResponse("No lectures scheduled.");
            return;
        }

        StringBuilder lectureList = new StringBuilder("Current Lectures:\n\n");
        lectures.forEach(lecture ->
                lectureList.append(lecture.toString()).append("\n")
        );
        updateResponse(lectureList.toString());
    }

    private boolean validateLectureInputs(String action, String module,
                                          LocalDate date, String time, String room) {
        if (module == null || date == null || time == null || room == null) {
            showAlert("Error", "Please fill in all lecture fields");
            return false;
        }
        return true;
    }

    private boolean validateLecturerInputs(String action, String module, String lecturerName) {
        if (module == null || lecturerName.trim().isEmpty()) {
            showAlert("Error", "Please fill in all lecturer fields");
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
