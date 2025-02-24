/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author oranc
 */
public class ClientGUI_23375175_23366044 {
    private static final int PORT = 6754;
    private static InetAddress host;
    
    public static void main(String[] args){
        try{
            host = InetAddress.getLocalHost();
    }
        catch (UnknownHostException e) {
            System.out.println("Host ID not found.");
            System.exit(1);
        }
        run();
    }

    
    private static void run(){
        Socket link = null;
        try {
            link = new Socket(host, PORT);
            BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter out = new PrintWriter(link.getOutputStream(),true);
            BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));
            String message = null; String response = null;
            
            System.out.println("Enter message:");
            message = userEntry.readLine();
            out.print(message);
            response = input.readLine();
            System.out.println("\nServer response: " + response);
          
        }
            catch(IOException e) {
                e.printStackTrace();
            }
                finally{
                    try{
                        System.out.println("Closing Connection...");
                        link.close();
                    }
                        catch (IOException e) {
                            System.out.println("Unable to disconnect.");
                            System.exit(1);
                        }
        }
    }
}
        

