/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// View/ helper class
package com.mycompany._23366044_Client;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * ClientGUIWrapper serves as a wrapper for the client GUI application.
 * This class extends JavaFX's Application class and acts as an intermediary
 * between the client-server logic and the GUI interface.
 *
 * @author oranc
 */
public class ClientGUIWrapper extends Application {
    
    /**
     * Static instance of the ClientServer that will be accessed by the GUI.
     * Used to maintain a single connection to the server across the application.
     */
    private static ClientServer clientServerInstance;
    
    /**
     * Sets the ClientServer instance that will be used by the GUI.
     * This allows the ClientServer to be initialized elsewhere and then
     * passed to the wrapper before launching the application.
     *
     * @param clientServer The ClientServer instance to be used
     */
    public static void setClientServer(ClientServer clientServer) {
        clientServerInstance = clientServer;
    }
    
    /**
     * Getter method for the ClientServer instance.
     *
     * @return The current ClientServer instance being used
     */
    public static ClientServer getClientServer() {
        return clientServerInstance;
    }
    
    /**
     * Overridden start method from the Application class.
     * This is the main entry point for the JavaFX application.
     * Creates a new GUI instance and passes the ClientServer to it.
     *
     * @param primaryStage The primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        System.out.println("Starting ClientGUIWrapper");
        ClientGUI_23375175_23366044 gui = new ClientGUI_23375175_23366044(clientServerInstance);
        gui.start(primaryStage);
    }
}
