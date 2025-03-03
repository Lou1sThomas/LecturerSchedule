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
    private final String module;
    private final LocalDate date;
    private final LocalTime time;
    private final String room;
    
    public Lecturer (String module, LocalDate date, LocalTime time, String room) {
        this.module = module;
        this.date = date;
        this.time = time;
        this.room = room;
    }
    
    public String getModule(){
        return module;
    }
    
    public LocalDate getDate(){
        return date;
    }
    
    public LocalTime getTime(){
        return time;
    }
    
    public String getRoom(){
        return room;
    }
    
    @Override
    public String toString(){
        return String.format("Module: %s, Date: %s, Time: %s, Room: %s", module, date, time, room);
    }
}
