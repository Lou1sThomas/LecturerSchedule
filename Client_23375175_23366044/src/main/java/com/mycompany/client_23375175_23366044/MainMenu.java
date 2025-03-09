    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// View class
public class MainMenu extends Application {
    private Stage primaryStage;
    private ClientServer clientServer;
    
    public MainMenu() {
    this.clientServer = ClientGUIWrapper.getClientServer();
    }
    

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Timetable Manager");
        
        if (clientServer != null) {
    }

        

        // Create main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Title
        Label titleLabel = new Label("Timetable Manager");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        titleLabel.setStyle("-fx-text-fill: #2E7D32;");

        // Buttons
        Button schedulerButton = createStyledButton("Add Lecturer");
        schedulerButton.setOnAction(e -> openLectureScheduler());

        Button removeLecturerButton = createStyledButton("Remove Lecturer");
        removeLecturerButton.setOnAction(e -> openRemoveLecturer());
        
        Button weeklyViewButton = createStyledButton("Weekly Timetable");
        weeklyViewButton.setOnAction(e -> openWeeklyTimetableView());
        
        Button otherButton = createStyledButton("Other");
        otherButton.setOnAction(e -> openOtherMenu());

        Button exitButton = createStyledButton("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        // Add components to layout
        mainLayout.getChildren().addAll(
                titleLabel,
                schedulerButton,
                removeLecturerButton,
                weeklyViewButton,
                otherButton,
                exitButton
        );

        // Create scene
        Scene scene = new Scene(mainLayout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openLectureScheduler() {
        try {
            if (clientServer != null) {
                clientServer.sendMessage("OPEN_ADD_LECTURE");
            }
            LectureSchedulerGUI scheduler = new LectureSchedulerGUI();
            scheduler.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openRemoveLecturer() {
        try {
            if (clientServer != null) {
                clientServer.sendMessage("OPEN_REMOVE_LECTURE");
            }
            RemoveLecturerGUI removeLecturer = new RemoveLecturerGUI();
            removeLecturer.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openTimetableView() {
        try {
            TimetableViewGUI timetable = new TimetableViewGUI();
            timetable.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openWeeklyTimetableView() {
        try {
            if (clientServer != null) {
                clientServer.sendMessage("OPEN_LECTURE_TIMETABLE");
            }
            TimetableViewGUI weeklyView = new TimetableViewGUI();
            weeklyView.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openOtherMenu() {
    try {
        if (clientServer != null) {
            clientServer.sendMessage("OPEN_OTHER_MENU");
        }
        OtherMenuGUI otherMenu = new OtherMenuGUI();
        otherMenu.start(new Stage());
        primaryStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // Helper method to create styled buttons
    private Button createStyledButton(String text) {
        Button button = new Button(text);

        // Define default and hover styles
        String defaultStyle = "-fx-background-color: #2E7D32;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 16px;"
                + "-fx-padding: 10 20;"
                + "-fx-background-radius: 5;"
                + "-fx-cursor: hand;";

        String hoverStyle = "-fx-background-color: #1B5E20;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 16px;"
                + "-fx-padding: 10 20;"
                + "-fx-background-radius: 5;"
                + "-fx-cursor: hand;";

        // Set default style and hover effect
        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));

        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
