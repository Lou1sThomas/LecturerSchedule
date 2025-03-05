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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lecture Scheduler - Main Menu");

        // Create main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Title
        Label titleLabel = new Label("Lecture Scheduler");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        titleLabel.setStyle("-fx-text-fill: #2e5b7d;");

        // Buttons
        Button viewScheduleButton = createStyledButton("View Timetable");
        Button manageLecturersButton = createStyledButton("Manage Lecturers");

        // Button actions
        viewScheduleButton.setOnAction(e -> {
            TimetableViewGUI timetableView = new TimetableViewGUI();
            try {
                timetableView.start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        manageLecturersButton.setOnAction(e -> {
            LectureSchedulerGUI lecturerManager = new LectureSchedulerGUI();
            try {
                lecturerManager.start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add components to layout
        mainLayout.getChildren().addAll(
                titleLabel,
                viewScheduleButton,
                manageLecturersButton
        );

        // Create scene
        Scene scene = new Scene(mainLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to create styled buttons
    private Button createStyledButton(String text) {
    Button button = new Button(text);

    // Define the default style as a regular string
    String defaultStyle = "-fx-background-color: #2E7D32;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 16px;"
                        + "-fx-padding: 10 20;"
                        + "-fx-background-radius: 5;"
                        + "-fx-cursor: hand;";

    // Define the hover style
    String hoverStyle = "-fx-background-color: #1B5E20;"
                      + "-fx-text-fill: white;"
                      + "-fx-font-size: 16px;"
                      + "-fx-padding: 10 20;"
                      + "-fx-background-radius: 5;"
                      + "-fx-cursor: hand;";

    // Set the default style
    button.setStyle(defaultStyle);

    // Hover effects
    button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
    button.setOnMouseExited(e -> button.setStyle(defaultStyle));

    return button;
}


    public static void main(String[] args) {
        launch(args);
    }
}
