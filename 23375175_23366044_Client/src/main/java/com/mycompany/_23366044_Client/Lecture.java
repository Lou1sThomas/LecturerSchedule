/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._23366044_Client;

import java.time.LocalDate;
import java.time.LocalTime;

// Model class
public class Lecture {
    private final String module;
    private final LocalDate date;
    private final LocalTime time;
    private final String room;
    private final String sessionType; // New field for session type

    public Lecture(String module, LocalDate date, LocalTime time, String room, String sessionType) {
        this.module = module;
        this.date = date;
        this.time = time;
        this.room = room;
        this.sessionType = sessionType != null ? sessionType : "Lecture"; // Default to "Lecture" if null
    }

    public String getModule() {
        return module;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }
    
    public String getSessionType() {
        return sessionType;
    }

    @Override
    public String toString() {
        return String.format("Module: %s, Date: %s, Time: %s, Room: %s, Type: %s",
                module, date, time, room, sessionType);
    }
}
