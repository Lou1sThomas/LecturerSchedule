/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.client_23375175_23366044;
import java.io.*;
import java.net.*;
import javafx.application.Application;

/**
 *
 * @author oranc
 */
public class ClientServer {
 private static InetAddress host;
    private static final int PORT = 6754;
    private static Socket link;
    private static BufferedReader in;
    private static PrintWriter out;

    public ClientServer(){
        try{
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException e){
            System.out.println("Host not found");
            System.exit(1);
        }
    }
    
   public void connectToServer(){				
    try 
    {
	link = new Socket(host,PORT);		
        
	in = new BufferedReader(new InputStreamReader(link.getInputStream()));
	out = new PrintWriter(link.getOutputStream(),true);	 
        System.out.println("Connected to server");
    } catch (IOException e ){
        System.out.println("Unable to connect to server");
        e.printStackTrace();
        System.exit(1);
    }
 }
   private void sendMessage(String message){
       if (out != null){
       out.println(message);
       System.out.println("Message sent to server: " + message);
       listenForResponse(); // waits for server to respond
    }else {
           System.out.println("Not connected to server");
       }
   }
   
   private void listenForResponse() {
       try{
           String response = in.readLine();
           if(response != null) {
               System.out.println("Server response: " + response);
               handleServerResponse(response);
           }else {
               System.out.println("Server disconnected");
               closeConnection();
           }
       } 
       catch (IOException e) {
           System.out.println("Error reading response.");
           e.printStackTrace();
       }
   }
   
   private void handleServerResponse(String response) {
       switch (response) {
           case "SHOW_LECTURE_TIMETABLE":
               System.out.println("Fill in later");
               break;
               
           case "REMOVE_LECTURE_MENU":
               System.out.println("Fill in later");
               break;
           
           case "MAIN_MENU":
               System.out.println("Fill in later");
               break;
               
          
               
           default:
               System.out.println("Error, unknown server response: " + response);
               break;
       }
   }
   
   private static void closeConnection() {
       try{
           System.out.println("\n Closing Conecction...");
           if(link != null) {
               link.close();
               }
            }
           catch (IOException e) {
                   System.out.println("Unable to disconnect from server");
                   System.exit(1);
            }
    }
   
   public static void main(String[] args) {
        ClientServer clientServer = new ClientServer();
        clientServer.connectToServer();
        
        ClientGUIWrapper.setClientServer(clientServer);
        Application.launch(ClientGUIWrapper.class);
   }
}
