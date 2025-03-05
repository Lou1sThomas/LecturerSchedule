/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

/**
 *
 * @author louis
 */
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LectureDisplay {
    private final StringProperty module;
    private final StringProperty lecturer;
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty room;

    public LectureDisplay(String module, String lecturer, String date, String time, String room) {
        this.module = new SimpleStringProperty(module);
        this.lecturer = new SimpleStringProperty(lecturer);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.room = new SimpleStringProperty(room);
    }

    // Getters for properties
    public StringProperty moduleProperty() { return module; }
    public StringProperty lecturerProperty() { return lecturer; }
    public StringProperty dateProperty() { return date; }
    public StringProperty timeProperty() { return time; }
    public StringProperty roomProperty() { return room; }

    // Regular getters
    public String getModule() { return module.get(); }
    public String getLecturer() { return lecturer.get(); }
    public String getDate() { return date.get(); }
    public String getTime() { return time.get(); }
    public String getRoom() { return room.get(); }
}
