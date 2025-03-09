/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to manage a single instance of LecturerManager
 * This ensures data is shared across different parts of the application
 */

// Controller Class
public class LecturerManagerSingleton {
    private static LecturerManager instance;
    private final List<String> modules = new ArrayList<>();
    
    private LecturerManagerSingleton() {
        // Private constructor to prevent instantiation
    }
    
    public List<String> getModules() {
    return modules;
}
    
    public static synchronized LecturerManager getInstance() {
        if (instance == null) {
            instance = new LecturerManager();
        }
        return instance;
    }
    
    public void addModule(String moduleName) {
    // Check if module already exists to avoid duplicates
    boolean moduleExists = false;
    for (String existingModule : getModules()) {
        if (existingModule.equals(moduleName)) {
            moduleExists = true;
            break;
        }
    }
    
    if (!moduleExists) {
        modules.add(moduleName);
    }
}
}
