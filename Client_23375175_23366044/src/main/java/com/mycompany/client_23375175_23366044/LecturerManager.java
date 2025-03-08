package com.mycompany.client_23375175_23366044;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LecturerManager {
    private final List<String> modules;
    private final List<Lecture> lectures;
    private final List<Lecturer> lecturers;

    public LecturerManager() {
        this.modules = new ArrayList<>();
        this.lectures = new ArrayList<>();
        this.lecturers = new ArrayList<>();
    }

    // Module Management
    public boolean addModule(String module) {
        if (modules.size() < 5 && !module.trim().isEmpty() && !modules.contains(module)) {
            modules.add(module);
            return true;
        }
        return false;
    }

    public List<String> getModules() {
        return new ArrayList<>(modules);
    }

    // Lecturer Management
    public boolean addLecturer(String name, String module) {
        // Check if module exists and lecturer limit is not exceeded
        if (modules.contains(module) && lecturers.size() < 10) {
            // Check if lecturer with same name and module doesn't already exist
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

    public boolean removeLecturer(String name, String module) {
        return lecturers.removeIf(l ->
                l.getName().equals(name) && l.getModule().equals(module)
        );
    }

    public List<Lecturer> getAllLecturers() {
        return new ArrayList<>(lecturers);
    }

    // Lecture Management with session type
    public boolean addLecture(String module, LocalDate date, LocalTime time, String room, String sessionType) {
        // Check if module exists
        if (!modules.contains(module)) {
            return false;
        }

        // Check for lecture conflicts
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
    
    // For backward compatibility
    public boolean addLecture(String module, LocalDate date, LocalTime time, String room) {
        return addLecture(module, date, time, room, "Lecture");
    }

    public boolean removeLecture(String module, LocalDate date, LocalTime time, String room, String sessionType) {
        return lectures.removeIf(l ->
                l.getModule().equals(module) &&
                l.getDate().equals(date) &&
                l.getTime().equals(time) &&
                l.getRoom().equals(room)
        );
    }

    public List<Lecture> getAllLectures() {
        return new ArrayList<>(lectures);
    }

    // Find lectures by module
    public List<Lecture> getLecturesByModule(String module) {
        return lectures.stream()
                .filter(l -> l.getModule().equals(module))
                .collect(Collectors.toList());
    }
}
