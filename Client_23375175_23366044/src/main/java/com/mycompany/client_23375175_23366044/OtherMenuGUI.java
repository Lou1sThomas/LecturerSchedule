/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// View Class
public class OtherMenuGUI extends Application {
    private Stage primaryStage;
    private ClientServer clientServer;
    
    public OtherMenuGUI() {
        this.clientServer = ClientGUIWrapper.getClientServer();
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Other Services");
        
        // Create main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");
        
        // Title
        Label titleLabel = new Label("Request Other Service");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2E7D32;");
        
        // Description
        Label descLabel = new Label("Enter a service request that is not in the main menu:");
        descLabel.setFont(Font.font("Arial", 14));
        
        // Input field
        TextField serviceRequestField = new TextField();
        serviceRequestField.setPromptText("Enter your service request");
        serviceRequestField.setPrefWidth(300);
        
        // Buttons
        Button submitButton = createStyledButton("Submit Request");
        submitButton.setOnAction(e -> {
            String request = serviceRequestField.getText().trim();
            if (!request.isEmpty()) {
                if (clientServer != null) {
                    clientServer.sendMessage("REQUEST_OTHER_SERVICE:" + request);
                }
            }
        });
        
        Button backButton = createStyledButton("Back to Main Menu");
        backButton.setOnAction(e -> returnToMainMenu());
        
        // Add components to layout
        mainLayout.getChildren().addAll(
                titleLabel,
                descLabel,
                serviceRequestField,
                submitButton,
                backButton
        );
        
        // Create scene
        Scene scene = new Scene(mainLayout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void returnToMainMenu() {
        try {
            if (clientServer != null) {
                clientServer.sendMessage("MAIN_MENU");
            }
            MainMenu mainMenu = new MainMenu();
            mainMenu.start(new Stage());
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Helper method to create styled buttons (same as MainMenu)
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        
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
                
        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
        
        return button;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
