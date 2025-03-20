/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._23366044_Client; 

import java.time.LocalDate; 
import java.time.LocalTime; 

/**
 * The Lecturer class represents a lecturer with a name and the module they teach.
 * This class provides getter methods to access these details and a toString method for easy display.
 * 
 * @author louis
 */
public class Lecturer {
    // Private final variables to store lecturer details
    private final String name;
    private final String module;

    /**
     * Constructor to initialize a Lecturer object.
     * 
     * @param name   The name of the lecturer
     * @param module The module they teach
     */
    public Lecturer(String name, String module) {
        this.name = name;
        this.module = module;
    }

    /**
     * Gets the name of the lecturer.
     * 
     * @return The lecturer's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the module the lecturer teaches.
     * 
     * @return The module name
     */
    public String getModule() {
        return module;
    }

    /**
     * Returns a formatted string representation of the Lecturer object.
     * 
     * @return A string with the lecturer's name and module
     */
    @Override
    public String toString() {
        return String.format("Lecturer: %s, Module: %s", name, module);
    }
}

