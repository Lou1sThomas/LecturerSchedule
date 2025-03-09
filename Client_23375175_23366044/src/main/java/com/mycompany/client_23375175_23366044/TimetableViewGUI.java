/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

//View Class
public class TimetableViewGUI extends Application {
    private final LecturerManager lectureManager;
    private GridPane timetableGrid;
    private LocalDate currentWeekStart;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private Label weekLabel;
    private Map<String, Color> moduleColors = new HashMap<>();
    private final Color[] colorPalette = {
            Color.LIGHTBLUE, Color.LIGHTGREEN, Color.LIGHTSALMON, 
            Color.LIGHTPINK, Color.LIGHTYELLOW, Color.LIGHTCORAL, 
            Color.PLUM, Color.PALETURQUOISE, Color.PEACHPUFF
    };
    private int colorIndex = 0;
    private Stage primaryStage;

    public TimetableViewGUI() {
        // Use the singleton to get the shared instance
        this.lectureManager = LecturerManagerSingleton.getInstance();
        // Initialize to the current week's Monday
        currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Weekly Timetable View");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Create top navigation bar
        HBox navigationBar = createNavigationBar();
        mainLayout.setTop(navigationBar);

        // Create timetable grid
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        timetableGrid = createTimetableGrid();
        scrollPane.setContent(timetableGrid);
        mainLayout.setCenter(scrollPane);

        // Create bottom controls
        HBox bottomControls = createBottomControls();
        mainLayout.setBottom(bottomControls);

        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Populate timetable with initial data
        populateTimetable();
    }

    private HBox createNavigationBar() {
        HBox navbar = new HBox(10);
        navbar.setPadding(new Insets(10));
        navbar.setAlignment(Pos.CENTER);

        Button prevWeekBtn = new Button("← Previous Week");
        Button nextWeekBtn = new Button("Next Week →");
        Button currentWeekBtn = new Button("Current Week");

        weekLabel = new Label();
        updateWeekLabel();

        prevWeekBtn.setOnAction(e -> {
            currentWeekStart = currentWeekStart.minusWeeks(1);
            updateWeekLabel();
            populateTimetable();
        });

        nextWeekBtn.setOnAction(e -> {
            currentWeekStart = currentWeekStart.plusWeeks(1);
            updateWeekLabel();
            populateTimetable();
        });

        currentWeekBtn.setOnAction(e -> {
            currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            updateWeekLabel();
            populateTimetable();
        });

        navbar.getChildren().addAll(prevWeekBtn, weekLabel, nextWeekBtn, currentWeekBtn);
        return navbar;
    }

    private void updateWeekLabel() {
        LocalDate weekEnd = currentWeekStart.plusDays(4); // Friday
        weekLabel.setText(String.format("Week: %s - %s", 
                dateFormatter.format(currentWeekStart), 
                dateFormatter.format(weekEnd)));
    }

    private GridPane createTimetableGrid() {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(5));

        // Configure column constraints
        // First column for time labels
        ColumnConstraints timeColumn = new ColumnConstraints();
        timeColumn.setMinWidth(80);
        timeColumn.setPrefWidth(80);
        grid.getColumnConstraints().add(timeColumn);

        // Columns for days of the week
        for (int i = 0; i < 5; i++) { // Monday to Friday
            ColumnConstraints dayColumn = new ColumnConstraints();
            dayColumn.setMinWidth(180);
            dayColumn.setPrefWidth(180);
            dayColumn.setHgrow(Priority.SOMETIMES);
            grid.getColumnConstraints().add(dayColumn);
        }

        // Add day headers
        for (int day = 0; day < 5; day++) {
            DayOfWeek dayOfWeek = DayOfWeek.of(day + 1); // Monday=1, Friday=5
            LocalDate date = currentWeekStart.plusDays(day);
            Label dayLabel = new Label(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()) + 
                                      "\n" + date.format(DateTimeFormatter.ofPattern("dd/MM")));
            dayLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setPrefHeight(40);
            GridPane.setHalignment(dayLabel, javafx.geometry.HPos.CENTER);
            grid.add(dayLabel, day + 1, 0); // +1 because first column is for time labels
        }

        // Add time slots
        for (int slot = 0; slot < UIConstants.TIME_SLOTS.size(); slot++) {
            String timeSlot = UIConstants.TIME_SLOTS.get(slot);
            Label timeLabel = new Label(timeSlot);
            timeLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
            timeLabel.setAlignment(Pos.CENTER);
            timeLabel.setMaxHeight(Double.MAX_VALUE);
            GridPane.setValignment(timeLabel, javafx.geometry.VPos.CENTER);
            grid.add(timeLabel, 0, slot + 1); // +1 because first row is for day headers
        }

        // Initialize grid cells
        for (int day = 0; day < 5; day++) {
            for (int slot = 0; slot < UIConstants.TIME_SLOTS.size(); slot++) {
                Pane emptyCell = new Pane();
                emptyCell.setStyle("-fx-background-color: white; -fx-border-color: lightgray;");
                emptyCell.setMinHeight(60);
                grid.add(emptyCell, day + 1, slot + 1);
            }
        }

        return grid;
    }

    private HBox createBottomControls() {
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER);

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

        Button refreshButton = new Button("Refresh Timetable");
        refreshButton.setOnAction(e -> populateTimetable());

        controls.getChildren().addAll(refreshButton, backButton);
        return controls;
    }

    private void populateTimetable() {
    // Clear existing color assignments when refreshing
    moduleColors.clear();
    colorIndex = 0;
    
    // Reset all cells to empty
    for (int day = 0; day < 5; day++) {
        for (int slot = 0; slot < UIConstants.TIME_SLOTS.size(); slot++) {
            Pane emptyCell = new Pane();
            emptyCell.setStyle("-fx-background-color: white; -fx-border-color: lightgray;");
            emptyCell.setMinHeight(60);
            timetableGrid.add(emptyCell, day + 1, slot + 1);
        }
    }

    // Get all lectures for the current week
    List<Lecture> allLectures = lectureManager.getAllLectures();
    
    // Filter lectures for the current week (Monday to Friday)
    LocalDate weekEnd = currentWeekStart.plusDays(6); // Sunday
    List<Lecture> weekLectures = allLectures.stream()
            .filter(lecture -> !lecture.getDate().isBefore(currentWeekStart) && !lecture.getDate().isAfter(weekEnd))
            .collect(Collectors.toList());

    // Get all lecturers
    List<Lecturer> lecturers = lectureManager.getAllLecturers();
    
    // Create a map of module to lecturer for quick lookup
    Map<String, List<String>> moduleToLecturers = lecturers.stream()
            .collect(Collectors.groupingBy(
                    Lecturer::getModule,
                    Collectors.mapping(Lecturer::getName, Collectors.toList())
            ));

    // Place lectures in the grid
    for (Lecture lecture : weekLectures) {
        // Determine day of week (0 = Monday, 4 = Friday)
        LocalDate lectureDate = lecture.getDate();
        int dayOfWeek = lectureDate.getDayOfWeek().getValue() - 1; // Convert 1-7 to 0-6
        
        // Skip weekend lectures in this view
        if (dayOfWeek > 4) continue;
        
        // Determine time slot index
        String timeStr = lecture.getTime().toString();
        int timeSlotIndex = UIConstants.TIME_SLOTS.indexOf(timeStr);
        
        if (timeSlotIndex != -1) {
            // Get lecturer names
            List<String> lecturerNames = moduleToLecturers.getOrDefault(lecture.getModule(), 
                                                                      Collections.singletonList("Unassigned"));
            String lecturerString = String.join(", ", lecturerNames);
            
            // Create cell content - pass the session type
            VBox cellContent = createLectureCell(
                lecture.getModule(), 
                lecturerString, 
                lecture.getRoom(), 
                lecture.getSessionType());
            
            // Add to grid
            timetableGrid.add(cellContent, dayOfWeek + 1, timeSlotIndex + 1);
        }
    }
}

        private VBox createLectureCell(String module, String lecturer, String room, String sessionType) {
        VBox cell = new VBox(5);
        cell.setPadding(new Insets(5));
        cell.setAlignment(Pos.CENTER);

        // Assign a consistent color to each module
        if (!moduleColors.containsKey(module)) {
            moduleColors.put(module, colorPalette[colorIndex % colorPalette.length]);
            colorIndex++;
        }

        Color moduleColor = moduleColors.get(module);

        // Adjust color opacity based on session type
        double opacity = 0.7; // Default for lecture

        // Use different opacity for different session types for visual distinction
        if ("Lab".equals(sessionType)) {
            opacity = 0.85;
        } else if ("Tutorial".equals(sessionType)) {
            opacity = 0.55;
        }

        String colorStyle = String.format("-fx-background-color: rgba(%d, %d, %d, %.2f);", 
                (int)(moduleColor.getRed() * 255), 
                (int)(moduleColor.getGreen() * 255), 
                (int)(moduleColor.getBlue() * 255),
                opacity);

        cell.setStyle(colorStyle + "-fx-border-color: gray; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label moduleLabel = new Label(module);
        moduleLabel.setStyle("-fx-font-weight: bold;");
        moduleLabel.setWrapText(true);

        Label typeLabel = new Label(sessionType);
        typeLabel.setStyle("-fx-font-style: italic;");
        typeLabel.setWrapText(true);

        Label lecturerLabel = new Label(lecturer);
        lecturerLabel.setWrapText(true);

        Label roomLabel = new Label("Room: " + room);
        roomLabel.setWrapText(true);

        cell.getChildren().addAll(moduleLabel, typeLabel, lecturerLabel, roomLabel);

        // Add tooltip for more details
        Tooltip tooltip = new Tooltip(
                "Module: " + module + "\n" +
                "Type: " + sessionType + "\n" +
                "Lecturer: " + lecturer + "\n" +
                "Room: " + room
        );
        Tooltip.install(cell, tooltip);

        return cell;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
