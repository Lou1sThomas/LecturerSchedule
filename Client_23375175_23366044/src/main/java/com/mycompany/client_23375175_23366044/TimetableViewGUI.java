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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class TimetableViewGUI extends Application {
    private final LecturerManager lectureManager;
    private TableView<LectureDisplay> timetableView;

    public TimetableViewGUI() {
        this.lectureManager = new LecturerManager();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lecturer Timetable");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));

        // Create timetable view
        createTimetableView();

        // Back to Main Menu button
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
                new Label("Lecturer Timetable"),
                timetableView,
                backButton
        );

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createTimetableView() {
        // Create table columns
        TableColumn<LectureDisplay, String> moduleColumn = new TableColumn<>("Module");
        moduleColumn.setCellValueFactory(cellData -> cellData.getValue().moduleProperty());

        TableColumn<LectureDisplay, String> lecturerColumn = new TableColumn<>("Lecturer");
        lecturerColumn.setCellValueFactory(cellData -> cellData.getValue().lecturerProperty());

        TableColumn<LectureDisplay, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<LectureDisplay, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());

        TableColumn<LectureDisplay, String> roomColumn = new TableColumn<>("Room");
        roomColumn.setCellValueFactory(cellData -> cellData.getValue().roomProperty());

        // Create table view
        timetableView = new TableView<>();
        timetableView.getColumns().addAll(
                moduleColumn, lecturerColumn, dateColumn, timeColumn, roomColumn
        );

        // Populate table
        populateTimetable();
    }

    private void populateTimetable() {
        // Get all lectures
        List<Lecture> lectures = lectureManager.getAllLectures();

        // Get all lecturers
        List<Lecturer> lecturers = lectureManager.getAllLecturers();

        // Create display items
        List<LectureDisplay> displayItems = lectures.stream().map(lecture -> {
            // Find lecturer for this module
            String lecturerName = lecturers.stream()
                    .filter(l -> l.getModule().equals(lecture.getModule()))
                    .map(Lecturer::getName)
                    .findFirst()
                    .orElse("Unassigned");

            return new LectureDisplay(
                    lecture.getModule(),
                    lecturerName,
                    lecture.getDate().toString(),
                    lecture.getTime().toString(),
                    lecture.getRoom()
            );
        }).collect(Collectors.toList());

        // Set items in table
        timetableView.setItems(FXCollections.observableArrayList(displayItems));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
