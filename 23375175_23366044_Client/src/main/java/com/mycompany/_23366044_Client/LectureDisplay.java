/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._23366044_Client; // Defines the package name for the class

/**
 * LectureDisplay class represents a lecture with details such as module, lecturer, date, time, and room.
 * It uses JavaFX properties to allow for easy binding in UI applications.
 *
 * @author louis
 */

// Import JavaFX property classes for binding functionality
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LectureDisplay {
    // Properties for storing lecture details
    private final StringProperty module;
    private final StringProperty lecturer;
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty room;

    /**
     * Constructor to initialize lecture details.
     *
     * @param module   The name of the module
     * @param lecturer The name of the lecturer
     * @param date     The date of the lecture
     * @param time     The time of the lecture
     * @param room     The room where the lecture is held
     */
    public LectureDisplay(String module, String lecturer, String date, String time, String room) {
        this.module = new SimpleStringProperty(module);
        this.lecturer = new SimpleStringProperty(lecturer);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.room = new SimpleStringProperty(room);
    }

    // Getters for JavaFX properties to allow data binding in UI
    public StringProperty moduleProperty() { return module; }
    public StringProperty lecturerProperty() { return lecturer; }
    public StringProperty dateProperty() { return date; }
    public StringProperty timeProperty() { return time; }
    public StringProperty roomProperty() { return room; }

    // Standard getter methods to retrieve property values
    public String getModule() { return module.get(); }
    public String getLecturer() { return lecturer.get(); }
    public String getDate() { return date.get(); }
    public String getTime() { return time.get(); }
    public String getRoom() { return room.get(); }
}
