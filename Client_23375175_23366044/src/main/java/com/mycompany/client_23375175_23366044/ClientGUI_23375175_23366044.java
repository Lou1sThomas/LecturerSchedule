package com.mycompany.client_23375175_23366044;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class ClientGUI_23375175_23366044 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create main container
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: white;");

        //Big box
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        // App name
        Label appLabel = new Label("Timetable Scheduler");
        appLabel.setFont(Font.font("System", FontWeight.BOLD, 45));
        appLabel.setTextFill(Color.valueOf("#2E7D32"));

        Button enterButton = new Button("Enter System");
        
        // Enter button
        enterButton.setStyle(
            "-fx-background-color: #2E7D32; "+
            "-fx-text-fill: white; "+
            "-fx-font-size: 16px; "+
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand; "
        );

        // Hover effect for the button
        enterButton.setOnMouseEntered(e ->
                enterButton.setStyle(enterButton.getStyle() + "-fx-background-color: #1B5E20;")
        );
        enterButton.setOnMouseExited(e ->
                enterButton.setStyle(enterButton.getStyle() + "-fx-background-color: #2E7D32;")
        );

        // Add all elements to content container
        content.getChildren().addAll(appLabel, enterButton);

        // Add content to root
        root.getChildren().add(content);

        // Create scene
        Scene scene = new Scene(root, 600, 400);

        // Set up the stage
        primaryStage.setTitle("Timetable App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), content);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Handle enter button click
        enterButton.setOnAction(e -> {
            // Create fade-out transition
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), content);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(event -> {
                // Launch the main application
                ClientGUI_23375175_23366044 mainApp = new ClientGUI_23375175_23366044();
                try {
                    mainApp.start(new Stage());
                    primaryStage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            fadeOut.play();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
