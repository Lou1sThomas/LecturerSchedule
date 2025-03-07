/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TimetableViewGUI extends Application {
    private final LecturerManager lectureManager;
    private TableView<LectureDisplay> timetableView;
    private Button refreshButton;

    public TimetableViewGUI() {
        // Use the singleton to get the shared instance
        this.lectureManager = LecturerManagerSingleton.getInstance();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lecturer Timetable");

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));

        // Create timetable view
        createTimetableView();
        
        // Add refresh button
        refreshButton = new Button("Refresh Timetable");
        refreshButton.setOnAction(e -> populateTimetable());

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

        Label titleLabel = new Label("Lecturer Timetable");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        mainLayout.getChildren().addAll(
                titleLabel,
                refreshButton,
                timetableView,
                backButton
        );

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Populate table when opening
        populateTimetable();
    }

    private void createTimetableView() {
        // Create table columns
        TableColumn<LectureDisplay, String> moduleColumn = new TableColumn<>("Module");
        moduleColumn.setCellValueFactory(cellData -> cellData.getValue().moduleProperty());
        moduleColumn.setPrefWidth(150);

        TableColumn<LectureDisplay, String> lecturerColumn = new TableColumn<>("Lecturer");
        lecturerColumn.setCellValueFactory(cellData -> cellData.getValue().lecturerProperty());
        lecturerColumn.setPrefWidth(150);

        TableColumn<LectureDisplay, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        dateColumn.setPrefWidth(120);

        TableColumn<LectureDisplay, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        timeColumn.setPrefWidth(100);

        TableColumn<LectureDisplay, String> roomColumn = new TableColumn<>("Room");
        roomColumn.setCellValueFactory(cellData -> cellData.getValue().roomProperty());
        roomColumn.setPrefWidth(100);

        // Create table view
        timetableView = new TableView<>();
        timetableView.getColumns().addAll(
                moduleColumn, lecturerColumn, dateColumn, timeColumn, roomColumn
        );
    }

    private void populateTimetable() {
        // Get all lectures
        List<Lecture> lectures = lectureManager.getAllLectures();

        // Get all lecturers
        List<Lecturer> lecturers = lectureManager.getAllLecturers();
        
        // Create a map of module to lecturer for quick lookup
        Map<String, List<String>> moduleToLecturers = lecturers.stream()
                .collect(Collectors.groupingBy(
                        Lecturer::getModule,
                        Collectors.mapping(Lecturer::getName, Collectors.toList())
                ));

        // Create display items
        List<LectureDisplay> displayItems = new ArrayList<>();
        
        for (Lecture lecture : lectures) {
            String module = lecture.getModule();
            
            // Find lecturers for this module
            List<String> lecturerNames = moduleToLecturers.getOrDefault(module, List.of("Unassigned"));
            
            // Create a display item for each lecturer teaching this module
            for (String lecturerName : lecturerNames) {
                displayItems.add(new LectureDisplay(
                        module,
                        lecturerName,
                        lecture.getDate().toString(),
                        lecture.getTime().toString(),
                        lecture.getRoom()
                ));
            }
        }
        
        // If no data, show a message
        if (displayItems.isEmpty() && (lectures.isEmpty() || lecturers.isEmpty())) {
            // For empty timetable, we'll add a placeholder row
            displayItems.add(new LectureDisplay(
                    "No modules scheduled",
                    "No lecturers assigned",
                    "-",
                    "-",
                    "-"
            ));
        }

        // Set items in table
        timetableView.setItems(FXCollections.observableArrayList(displayItems));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
