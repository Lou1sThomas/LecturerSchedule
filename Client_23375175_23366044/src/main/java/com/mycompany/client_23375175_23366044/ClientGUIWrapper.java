/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.client_23375175_23366044;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author oranc
 */
public class ClientGUIWrapper extends Application {
    
    private static ClientServer clientServerInstance;
    
    public static void setClientServer(ClientServer clientServer){
        clientServerInstance = clientServer;
    }
    
    @Override
    public void start(Stage primaryStage) {
        System.out.println("Starting ClientGUIWrapper");
        ClientGUI_23375175_23366044 gui = new ClientGUI_23375175_23366044(clientServerInstance);
        gui.start(primaryStage);
    }
    
}
