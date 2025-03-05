/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.client_23375175_23366044;
import java.io.*;
import java.net.*;

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

    public static void main(String[] args) {
     try 
     {
        host = InetAddress.getLocalHost();
     } 
     catch(UnknownHostException e) 
     {
	System.out.println("Host ID not found!");
	System.exit(1);
     }
     run();
   }
    
   private static void run() {				
    try 
    {
	link = new Socket(host,PORT);		
        
	in = new BufferedReader(new InputStreamReader(link.getInputStream()));
	out = new PrintWriter(link.getOutputStream(),true);	 
        
        //Manual console input for now
	BufferedReader userEntry =new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Enter message to send to server: ");
        
        Boolean running = true;
        while (running) {
            String message = userEntry.readLine();
            
            if(message.equals("QUIT")) {
                out.println("QUIT");
                System.out.println("Closing connection...");
                running = false;
                continue;
            }
            out.println(message);
            
            String response = in.readLine();
            if(response == null) {
                System.out.println("Server disconnected");
            } else {
                System.out.println("Server response: " + response);
            }
        }
	
        
    } 
    catch(IOException e)
    {
	e.printStackTrace();
    } 
    finally 
    {
        closeConnection();
        } 
    }
   
   private static void sendMessage(String message){
       out.println(message);
       System.out.println("Message sent to server: " + message);
       
       listenForResponse(); // waits for server to respond
   }
   
   private static void listenForResponse() {
       try{
           String response = in.readLine();
           System.out.println("Server Response: " + response);
           
           handleServerResponse(response);
           
       } 
       catch (IOException e) {
           System.out.println("Error reading response.");
           e.printStackTrace();
       }
   }
   
   private static void handleServerResponse(String response) {
       switch (response) {
           case "SHOW_LECTURE_MENU":
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
}
