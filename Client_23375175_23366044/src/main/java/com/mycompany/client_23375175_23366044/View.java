/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/***
 * @author louis
 */


public class View extends Application {
    private final LecturerManager lectureManager;
    private TextArea responseArea;
    private ListView<String> moduleList;
    private ComboBox<String> moduleSelect;

    public View() {
        this.lectureManager = new LecturerManager();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LM051-2026 Lecture Scheduler");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        setupMainLayout(mainLayout);

        Scene scene = new Scene(mainLayout, 600, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupMainLayout(VBox mainLayout) {
        mainLayout.getChildren().addAll(
                createModuleSection(),
                new Separator(),
                createLectureSection()
        );
    }

    private VBox createModuleSection() {
        VBox moduleSection = new VBox(10);
        moduleSection.setAlignment(Pos.CENTER_LEFT);

        HBox addModuleBox = createModuleInputBox();
        moduleList = new ListView<>();
        moduleList.setPrefHeight(100);

        moduleSection.getChildren().addAll(
                new Label("Module Management"),
                addModuleBox,
                moduleList
        );

        return moduleSection;
    }

    private HBox createModuleInputBox() {
        HBox addModuleBox = new HBox(10);
        TextField moduleInput = new TextField();
        moduleInput.setPromptText("Enter module name");
        Button addModuleButton = new Button("Add a Module");

        addModuleButton.setOnAction(e -> handleAddModule(moduleInput));
        addModuleBox.getChildren().addAll(moduleInput, addModuleButton);

        return addModuleBox;
    }

    private void handleAddModule(TextField moduleInput) {
        String module = moduleInput.getText();
        if (lectureManager.addModule(module)) {
            updateModuleLists();
            moduleInput.clear();
        } else {
            showAlert("Error", "Cannot add module. Maximum 5 modules allowed.");
        }
    }

    private void updateModuleLists() {
        List<String> modules = lectureManager.getModules();
        moduleList.setItems(FXCollections.observableArrayList(modules));
        moduleSelect.setItems(FXCollections.observableArrayList(modules));
    }

    private GridPane createLectureSection() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        setupLectureControls(grid);

        return grid;
    }

    private void setupLectureControls(GridPane grid) {
        ComboBox<String> actionSelect = new ComboBox<>(
                FXCollections.observableArrayList(UIConstants.ACTIONS)
        );
        moduleSelect = new ComboBox<>();
        DatePicker datePicker = new DatePicker();
        ComboBox<String> timeSelect = new ComboBox<>(
                FXCollections.observableArrayList(UIConstants.TIME_SLOTS)
        );
        ComboBox<String> roomSelect = new ComboBox<>(
                FXCollections.observableArrayList(UIConstants.ROOMS)
        );

        grid.addRow(0, new Label("Select Action:"), actionSelect);
        grid.addRow(1, new Label("Select Module:"), moduleSelect);
        grid.addRow(2, new Label("Select Date:"), datePicker);
        grid.addRow(3, new Label("Select Time:"), timeSelect);
        grid.addRow(4, new Label("Select Room:"), roomSelect);

        Button submitButton = new Button("Submit Request");
        grid.add(submitButton, 1, 5);

        submitButton.setOnAction(e -> handleSubmission(
                actionSelect.getValue(),
                moduleSelect.getValue(),
                datePicker.getValue(),
                timeSelect.getValue(),
                roomSelect.getValue()
        ));
    }

    private void handleSubmission(String action, String module, LocalDate date,
                                  String timeStr, String room) {
        if (action == null) {
            showAlert("Error", "Please select an action.");
            return;
        }

        if (!validateInputs(module, date, timeStr, room)) {
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
            case "Display Schedule":
                displaySchedule();
                break;
        }
    }

    private void handleAddLecture(String module, LocalDate date, LocalTime time, String room) {
        if (lectureManager.addLecturer(module, date, time, room)) {
            updateResponse("Lecture added successfully:\n" +
                    new Lecturer(module, date, time, room));
        }
    }

    private void handleRemoveLecture(String module, LocalDate date, LocalTime time, String room) {
        if (lectureManager.removeLecture(module, date, time, room)) {
            updateResponse("Lecture removed successfully");
        } else {
            updateResponse("Lecture not found");
        }
    }

    private void displaySchedule() {
        List<Lecturer> lectures = lectureManager.getAllLectures();
        if (lectures.isEmpty()) {
            updateResponse("No lectures scheduled.");
            return;
        }

        StringBuilder schedule = new StringBuilder("Current Schedule:\n\n");
        lectures.forEach(lecture ->
                schedule.append(lecture.toString()).append("\n")
        );
        updateResponse(schedule.toString());
    }

    private boolean validateInputs(String module, LocalDate date, String time, String room) {
        if (module == null || date == null || time == null || room == null) {
            showAlert("Error", "Please fill in all fields");
            return false;
        }
        return true;
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
    
    
}

        
   
