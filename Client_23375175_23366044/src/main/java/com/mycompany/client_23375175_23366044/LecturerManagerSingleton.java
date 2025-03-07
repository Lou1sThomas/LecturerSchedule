/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

/**
 * Singleton class to manage a single instance of LecturerManager
 * This ensures data is shared across different parts of the application
 */
public class LecturerManagerSingleton {
    private static LecturerManager instance;
    
    private LecturerManagerSingleton() {
        // Private constructor to prevent instantiation
    }
    
    public static synchronized LecturerManager getInstance() {
        if (instance == null) {
            instance = new LecturerManager();
        }
        return instance;
    }
}
