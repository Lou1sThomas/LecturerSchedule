/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._23366044_Server;

/**
 *
 * @author louis
 */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerGUI extends Application {
    private static TextArea logArea;
    private static Label statusLabel;
    private static Label clientCountLabel;
    private static ProgressBar progressBar;
    private static Label progressLabel;
    private static Server_23375175_23366044 server;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Lecture Scheduler Server");

        // Create main layout
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        // Status section
        statusLabel = new Label("Server Status: Stopped");
        clientCountLabel = new Label("Connected Clients: 0");

        // Log section
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(20);
        logArea.setWrapText(true);

        // Progress bar and label
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(580);
        progressBar.setVisible(false);

        progressLabel = new Label("");
        progressLabel.setVisible(false);

        // Start server button
        Button startButton = new Button("Start Server");
        startButton.setOnAction(e -> {
            startServer();
            startButton.setDisable(true);
            statusLabel.setText("Server Status: Running");
        });

        // Add components to layout
        mainLayout.getChildren().addAll(
            statusLabel,
            clientCountLabel,
            new Label("Server Log:"),
            logArea,
            progressBar,
            progressLabel,
            startButton
        );

        Scene scene = new Scene(mainLayout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void startServer() {
        new Thread(() -> {
            server = new Server_23375175_23366044();
            server.startServer(this);
        }).start();
    }

    public static void updateLog(String message) {
        Platform.runLater(() -> logArea.appendText(message + "\n"));
    }

    public static void updateClientCount(int count) {
        Platform.runLater(() -> clientCountLabel.setText("Connected Clients: " + count));
    }

    public static void updateProgress(double progress) {
        Platform.runLater(() -> {
            progressBar.setVisible(true);
            progressBar.setProgress(progress);
        });
    }

    public static void updateProgressLabel(String message) {
        Platform.runLater(() -> {
            progressLabel.setVisible(true);
            progressLabel.setText(message);
        });
    }

    public static void hideProgress() {
        Platform.runLater(() -> {
            progressBar.setVisible(false);
            progressLabel.setVisible(false);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}