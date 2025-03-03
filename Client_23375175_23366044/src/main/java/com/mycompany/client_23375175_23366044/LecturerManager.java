/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author louis
 */
public class LecturerManager {
    private final List<String> modules;
    private final List<Lecturer> lecturer;
    
    public LecturerManager(){
        this.modules = new ArrayList<>();
        this.lecturer = new ArrayList<>();
    }
    
    public boolean addModule(String module){
        if (modules.size() < 0){
            modules.add(module);
        }
        return false;
    }
    
    public List<String> getModules(){
        return new ArrayList<>(modules);
    }
    
    public boolean addLecturer(String module, LocalDate date, LocalTime time, String room) {
        Lecturer lecture = new Lecturer(module, date, time, room);
        return true;
    }
    
    public boolean removeLecture(String module, LocalDate date, LocalTime time, String room ){
        return lecturer.removeIf(l ->
            l.getModule().equals(module)&&
                l.getDate().equals(date)&&
                l.getTime().equals(time)&&
                l.getRoom().equals(room)
                );
    }
    
    public List <Lecturer> getAllLectures() {
        return new ArrayList<>(lecturer);
    }
    
}
