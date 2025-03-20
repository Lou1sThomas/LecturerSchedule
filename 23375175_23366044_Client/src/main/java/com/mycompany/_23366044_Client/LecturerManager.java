package com.mycompany._23366044_Client;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Controller Class responsible for managing lecturers, modules, and lectures
public class LecturerManager {
    private final List<String> modules; // List of available modules
    private final List<Lecture> lectures; // List of scheduled lectures
    private final List<Lecturer> lecturers; // List of lecturers

    public LecturerManager() {
        this.modules = new ArrayList<>();
        this.lectures = new ArrayList<>();
        this.lecturers = new ArrayList<>();
    }

    // Module Management
    /**
     * Adds a module if the total count is less than 5 and the module is unique.
     * @param module The module name
     * @return true if the module was added, false otherwise
     */
    public boolean addModule(String module) {
        if (modules.size() < 5 && !module.trim().isEmpty() && !modules.contains(module)) {
            modules.add(module);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the list of all modules.
     * @return A new list containing all modules
     */
    public List<String> getModules() {
        return new ArrayList<>(modules);
    }

    // Lecturer Management
    /**
     * Adds a lecturer to the system if the module exists and the total lecturer count is below 10.
     * Ensures no duplicate lecturers for the same module.
     * @param name Lecturer's name
     * @param module Module assigned to the lecturer
     * @return true if added, false otherwise
     */
    public boolean addLecturer(String name, String module) {
        if (modules.contains(module) && lecturers.size() < 10) {
            boolean lecturerExists = lecturers.stream()
                    .anyMatch(l -> l.getName().equals(name) && l.getModule().equals(module));
            if (!lecturerExists) {
                Lecturer lecturer = new Lecturer(name, module);
                lecturers.add(lecturer);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a lecturer by name and module.
     * @param name Lecturer's name
     * @param module Lecturer's module
     * @return true if removed, false otherwise
     */
    public boolean removeLecturer(String name, String module) {
        return lecturers.removeIf(l ->
                l.getName().equals(name) && l.getModule().equals(module)
        );
    }
    
    /**
     * Removes all lectures associated with a specific module.
     * @param module The module name
     * @return The number of lectures removed
     */
    public int removeAllLecturesForModule(String module) {
        int count = 0;
        List<Lecture> lecturesToRemove = lectures.stream()
                .filter(l -> l.getModule().equals(module))
                .collect(Collectors.toList());
        
        for (Lecture lecture : lecturesToRemove) {
            if (removeLecture(lecture.getModule(), lecture.getDate(), lecture.getTime(), 
                           lecture.getRoom(), lecture.getSessionType())) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Finds a lecturer by name and module.
     * @param name Lecturer's name
     * @param module Module assigned to the lecturer
     * @return The Lecturer object if found, otherwise null
     */
    public Lecturer findLecturer(String name, String module) {
        return lecturers.stream()
                .filter(l -> l.getName().equals(name) && l.getModule().equals(module))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all lecturers in the system.
     * @return A new list containing all lecturers
     */
    public List<Lecturer> getAllLecturers() {
        return new ArrayList<>(lecturers);
    }

    // Lecture Management with session type
    /**
     * Adds a lecture if the module exists and there are no scheduling conflicts.
     * @param module The module name
     * @param date The lecture date
     * @param time The lecture time
     * @param room The lecture room
     * @param sessionType The type of session (e.g., Lecture, Seminar)
     * @return true if the lecture was added, false otherwise
     */
    public boolean addLecture(String module, LocalDate date, LocalTime time, String room, String sessionType) {
        if (!modules.contains(module)) {
            return false;
        }

        boolean hasConflict = lectures.stream().anyMatch(l ->
                l.getDate().equals(date) &&
                l.getTime().equals(time) &&
                l.getRoom().equals(room)
        );
        if (hasConflict) {
            return false;
        }

        Lecture lecture = new Lecture(module, date, time, room, sessionType);
        lectures.add(lecture);
        return true;
    }
    
    /**
     * Overloaded method for adding a lecture with a default session type of "Lecture".
     */
    public boolean addLecture(String module, LocalDate date, LocalTime time, String room) {
        return addLecture(module, date, time, room, "Lecture");
    }

    /**
     * Removes a lecture based on the specified parameters.
     * @param module The module name
     * @param date The lecture date
     * @param time The lecture time
     * @param room The lecture room
     * @param sessionType The session type
     * @return true if removed, false otherwise
     */
    public boolean removeLecture(String module, LocalDate date, LocalTime time, String room, String sessionType) {
        return lectures.removeIf(l ->
                l.getModule().equals(module) &&
                l.getDate().equals(date) &&
                l.getTime().equals(time) &&
                l.getRoom().equals(room)
        );
    }

    /**
     * Retrieves all lectures in the system.
     * @return A new list containing all lectures
     */
    public List<Lecture> getAllLectures() {
        return new ArrayList<>(lectures);
    }

    /**
     * Retrieves lectures for a specific module.
     * @param module The module name
     * @return A list of lectures associated with the module
     */
    public List<Lecture> getLecturesByModule(String module) {
        return lectures.stream()
                .filter(l -> l.getModule().equals(module))
                .collect(Collectors.toList());
    }
}
