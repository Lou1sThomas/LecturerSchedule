/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import java.time.LocalDate;
import java.time.LocalTime;
/**
 *
 * @author louis
 */

public class Lecturer {
    private final String name;
    private final String module;

    public Lecturer(String name, String module) {
        this.name = name;
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public String getModule() {
        return module;
    }

    @Override
    public String toString() {
        return String.format("Lecturer: %s, Module: %s", name, module);
    }
}

