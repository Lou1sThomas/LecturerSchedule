/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._23366044_Server;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LectureOptimizer {
    private static final LocalTime EARLIEST_TIME = LocalTime.of(9, 0);
    private final List<Map<String, String>> lectures;
    private final ExecutorService executorService;
    private static final Logger logger = Logger.getLogger(LectureOptimizer.class.getName());
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LectureOptimizer(List<Map<String, String>> lectures) {
        // Create a defensive copy of the lectures list
        this.lectures = new ArrayList<>();
        for (Map<String, String> lecture : lectures) {
            this.lectures.add(new HashMap<>(lecture));
        }
        
        // Create a thread pool for parallel processing
        this.executorService = Executors.newFixedThreadPool(5);
        
        // Set up better logging
        logger.setLevel(Level.ALL);
    }

    public List<Map<String, String>> optimizeSchedule() {
        try {
            logger.info("Starting schedule optimization with " + lectures.size() + " lectures");
            
            // Group lectures by day of week
            Map<DayOfWeek, List<Map<String, String>>> lecturesByDay = groupLecturesByDay();
            
            // Print debug info
            for (Map.Entry<DayOfWeek, List<Map<String, String>>> entry : lecturesByDay.entrySet()) {
                logger.info(entry.getKey() + " has " + entry.getValue().size() + " lectures");
            }
            
            // List to hold tasks for each day
            List<Callable<List<Map<String, String>>>> optimizationTasks = new ArrayList<>();
            
            // Create tasks for each day
            for (Map.Entry<DayOfWeek, List<Map<String, String>>> entry : lecturesByDay.entrySet()) {
                final DayOfWeek day = entry.getKey();
                final List<Map<String, String>> dayLectures = new ArrayList<>(entry.getValue());
                
                if (!dayLectures.isEmpty()) {
                    // Create optimization task for this day
                    Callable<List<Map<String, String>>> task = () -> {
                        logger.info("Thread for " + day + " started optimization");
                        List<Map<String, String>> result = optimizeDaySchedule(day, dayLectures);
                        logger.info("Thread for " + day + " completed optimization");
                        return result;
                    };
                    
                    optimizationTasks.add(task);
                }
            }
            
            // Execute all tasks and wait for results
            List<Map<String, String>> optimizedSchedule = new ArrayList<>();
            try {
                // Use invokeAll to run all tasks
                List<Future<List<Map<String, String>>>> results = 
                    executorService.invokeAll(optimizationTasks, 30, TimeUnit.SECONDS);
                
                // Collect results
                for (Future<List<Map<String, String>>> future : results) {
                    try {
                        List<Map<String, String>> dayResult = future.get();
                        optimizedSchedule.addAll(dayResult);
                        logger.info("Added " + dayResult.size() + " optimized lectures");
                    } catch (ExecutionException e) {
                        logger.log(Level.SEVERE, "Error in optimization task", e);
                    }
                }
            } catch (InterruptedException e) {
                
                logger.log(Level.SEVERE, "Failed to complete optimization tasks", e);
                throw new RuntimeException("Optimization tasks interrupted or timed out", e);
            }
            
            logger.info("Total optimized lectures: " + optimizedSchedule.size());
            
            // Return the optimized schedule
            return optimizedSchedule;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Optimization failed with exception", e);
            throw new RuntimeException("Failed to optimize schedule", e);
        } finally {
            executorService.shutdown();
        }
    }

    private List<Map<String, String>> optimizeDaySchedule(DayOfWeek day, List<Map<String, String>> dayLectures) {
        logger.info("Optimizing " + dayLectures.size() + " lectures for " + day);
        
        try {
            // Make deep copies of all lectures
            List<Map<String, String>> optimizedLectures = new ArrayList<>();
            for (Map<String, String> lecture : dayLectures) {
                optimizedLectures.add(new HashMap<>(lecture));
            }
            
            // Sort lectures by time
            optimizedLectures.sort(Comparator.comparing(l -> LocalTime.parse(l.get("time"))));
            
            // Track occupied time slots for each room
            Map<String, Set<LocalTime>> occupiedTimeSlots = new HashMap<>();
            
            // Start with the earliest time
            LocalTime currentTimeSlot = EARLIEST_TIME;
            
            // Process each lecture
            for (Map<String, String> lecture : optimizedLectures) {
                String room = lecture.get("room");
                LocalTime originalTime = LocalTime.parse(lecture.get("time"));
                
                // Initialize tracking for this room if needed
                occupiedTimeSlots.putIfAbsent(room, new HashSet<>());
                
                // Find the earliest available time slot for this room
                LocalTime newTimeSlot = findEarliestAvailableSlot(currentTimeSlot, room, occupiedTimeSlots);
                
                // Update the lecture time if we found an earlier slot
                if (newTimeSlot.isBefore(originalTime)) {
                    logger.info("Moving lecture in room " + room + " from " + originalTime + " to " + newTimeSlot);
                    lecture.put("time", newTimeSlot.toString());
                    
                    // Mark this time slot as occupied for this room
                    occupiedTimeSlots.get(room).add(newTimeSlot);
                } else {
                    // Keep original time and mark it as occupied
                    occupiedTimeSlots.get(room).add(originalTime);
                }
                
                // Move to next time slot for next lecture
                currentTimeSlot = currentTimeSlot.plusHours(1);
                if (currentTimeSlot.isAfter(LocalTime.of(16, 0))) {
                    currentTimeSlot = EARLIEST_TIME; // Reset if we reached end of day
                }
            }
            
            return optimizedLectures;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error optimizing day " + day, e);
            return dayLectures; // Return original lectures if optimization fails
        }
    }
    
    private LocalTime findEarliestAvailableSlot(LocalTime startTime, String room, 
                                               Map<String, Set<LocalTime>> occupiedTimeSlots) {
        LocalTime currentSlot = startTime;
        Set<LocalTime> roomOccupiedSlots = occupiedTimeSlots.getOrDefault(room, new HashSet<>());
        
        // Find the earliest slot that's not occupied
        while (roomOccupiedSlots.contains(currentSlot) && currentSlot.isBefore(LocalTime.of(17, 0))) {
            currentSlot = currentSlot.plusHours(1);
        }
        
        return currentSlot;
    }

    private Map<DayOfWeek, List<Map<String, String>>> groupLecturesByDay() {
        Map<DayOfWeek, List<Map<String, String>>> result = new HashMap<>();
        
        for (Map<String, String> lecture : lectures) {
            try {
                String dateStr = lecture.get("date");
                if (dateStr == null || dateStr.isEmpty()) {
                    logger.warning("Lecture has no date: " + lecture);
                    continue;
                }
                
                // Parse the date
                LocalDate date = LocalDate.parse(dateStr, formatter);
                DayOfWeek day = date.getDayOfWeek();
                
                // Add lecture to the appropriate day group
                result.computeIfAbsent(day, k -> new ArrayList<>()).add(lecture);
                
            } catch (Exception e) {
                logger.warning("Failed to process lecture: " + lecture + " - " + e.getMessage());
            }
        }
        
        return result;
    }

    public boolean canOptimize() {
        logger.info("Checking if optimization is possible");
        
        try {
            Map<DayOfWeek, List<Map<String, String>>> lecturesByDay = groupLecturesByDay();
            
            // Check if any lectures can be moved earlier
            for (List<Map<String, String>> dayLectures : lecturesByDay.values()) {
                if (dayLectures.isEmpty()) continue;
                
                // Get all lecture times
                List<LocalTime> times = dayLectures.stream()
                    .map(lecture -> {
                        try {
                            return LocalTime.parse(lecture.get("time"));
                        } catch (Exception e) {
                            logger.warning("Invalid time format in lecture: " + lecture);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());
                
                // If any lecture starts after 9:00, optimization is possible
                if (times.stream().anyMatch(time -> time.isAfter(EARLIEST_TIME))) {
                    logger.info("Optimization is possible - found lectures after " + EARLIEST_TIME);
                    return true;
                }
            }
            
            logger.info("No optimization possible - all lectures already at earliest times");
            return false;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error checking if optimization is possible", e);
            return false;
        }
    }
}