/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._23366044_Client;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to manage a single instance of LecturerManager
 * This ensures data is shared across different parts of the application
 */
// Controller Class
public class LecturerManagerSingleton {
    // Static instance variable - holds the single instance of LecturerManager
    private static LecturerManager instance;
    
    private final List<String> modules = new ArrayList<>();
    
    /**
     * This enforces the singleton pattern by requiring use of getInstance()
     */
    private LecturerManagerSingleton() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Returns the list of modules
     * @return List of module names as strings
     */
    public List<String> getModules() {
        return modules;
    }
    
    /**
     * Returns the single instance of LecturerManager
     * If the instance doesn't exist, it creates one
     * The synchronized keyword ensures thread safety
     * 
     * @return The singleton instance of LecturerManager
     */
    public static synchronized LecturerManager getInstance() {
        if (instance == null) {
            instance = new LecturerManager();
        }
        return instance;
    }
    
    /**
     * Adds a new module to the list if it doesn't already exist
     * Performs duplicate checking to maintain data integrity
     * 
     * @param moduleName The name of the module to add
     */
    public void addModule(String moduleName) {
        // Check if module already exists to avoid duplicates
        boolean moduleExists = false;
        for (String existingModule : getModules()) {
            if (existingModule.equals(moduleName)) {
                moduleExists = true;
                break;
            }
        }
        
        // Only add the module if it's not already in the list
        if (!moduleExists) {
            modules.add(moduleName);
        }
    }
}