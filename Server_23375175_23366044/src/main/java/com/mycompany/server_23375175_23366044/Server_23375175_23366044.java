/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.server_23375175_23366044;

/**
 *
 * @author oranc
 */
import java.io.*;
import java.net.*;
public class Server_23375175_23366044{

    private static ServerSocket servSock;
    private static final int PORT = 6754;
    private static int clientConnections = 0;
    
    public static void main(String[] args) {
        System.out.println("Opening Port.../n");
        try{
            servSock = new ServerSocket(PORT);
        }
        catch (IOException e){
            System.out.println("Unable to attach to port.");
            System.exit(1);
        }
        do {
            run();
        } while (true);
    }
    
    private static void run() {
        Socket link = null;
        
        try{
            link = servSock.accept();
            clientConnections++;
            
            BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter out = new PrintWriter(link.getOutputStream(),true);
            String message = in.readLine();
            System.out.println("Message recieved from client: " + clientConnections + " " + message);
            out.println("Echo message " + message);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                System.out.println("/n Closing Connection");
                link.close();
                
            } catch(IOException e) {
                System.out.println("Unable to disconnect");
                System.exit(1);
            }
        }
    }
    
}
