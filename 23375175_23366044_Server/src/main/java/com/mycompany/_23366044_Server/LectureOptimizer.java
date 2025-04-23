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
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LectureOptimizer {
    private static final LocalTime EARLIEST_TIME = LocalTime.of(9, 0);
    private final List<Map<String, String>> lectures;
    private static final Logger logger = Logger.getLogger(LectureOptimizer.class.getName());
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ForkJoinPool forkJoinPool;

    public LectureOptimizer(List<Map<String, String>> lectures) {
        this.lectures = new ArrayList<>(lectures);
        this.forkJoinPool = new ForkJoinPool();
    }

    public List<Map<String, String>> optimizeSchedule() {
        try {
            Map<DayOfWeek, List<Map<String, String>>> lecturesByDay = groupLecturesByDay();
            
            // Create a task to optimize all days using Fork/Join
            OptimizeDaysTask task = new OptimizeDaysTask(new ArrayList<>(lecturesByDay.entrySet()));
            List<Map<String, String>> optimizedLectures = forkJoinPool.invoke(task);

            synchronized(lectures) {
                lectures.clear();
                lectures.addAll(optimizedLectures);
            }
            
            return new ArrayList<>(optimizedLectures);
        } catch (Exception e) {
            logger.warning("Optimization failed: " + e.getMessage());
            throw new RuntimeException("Failed to optimize schedule", e);
        } finally {
            forkJoinPool.shutdown();
        }
    }

    // Fork/Join task for optimizing multiple days
    private class OptimizeDaysTask extends RecursiveTask<List<Map<String, String>>> {
        private static final int THRESHOLD = 2; // Process days in parallel if more than this number
        private final List<Map.Entry<DayOfWeek, List<Map<String, String>>>> daysToProcess;

        public OptimizeDaysTask(List<Map.Entry<DayOfWeek, List<Map<String, String>>>> daysToProcess) {
            this.daysToProcess = daysToProcess;
        }

        @Override
        protected List<Map<String, String>> compute() {
            if (daysToProcess.size() <= THRESHOLD) {
                // Base case: directly process a small number of days
                return processSequentially();
            } else {
                // Divide the work into smaller subtasks
                return processInParallel();
            }
        }

        private List<Map<String, String>> processSequentially() {
            List<Map<String, String>> result = new ArrayList<>();
            for (Map.Entry<DayOfWeek, List<Map<String, String>>> entry : daysToProcess) {
                List<Map<String, String>> dayLectures = entry.getValue();
                if (!dayLectures.isEmpty()) {
                    result.addAll(optimizeDaySchedule(entry.getKey(), dayLectures));
                }
            }
            return result;
        }

        private List<Map<String, String>> processInParallel() {
            int mid = daysToProcess.size() / 2;
            
            // Divide the work
            OptimizeDaysTask leftTask = new OptimizeDaysTask(
                    daysToProcess.subList(0, mid));
            OptimizeDaysTask rightTask = new OptimizeDaysTask(
                    daysToProcess.subList(mid, daysToProcess.size()));
            
            // Fork one task
            leftTask.fork();
            
            // Compute the other task directly
            List<Map<String, String>> rightResult = rightTask.compute();
            
            // Join the forked task
            List<Map<String, String>> leftResult = leftTask.join();
            
            // Combine results
            List<Map<String, String>> combinedResults = new ArrayList<>(leftResult);
            combinedResults.addAll(rightResult);
            return combinedResults;
        }
    }

    // Task to optimize a single day's schedule
    private List<Map<String, String>> optimizeDaySchedule(DayOfWeek day, List<Map<String, String>> dayLectures) {
        dayLectures.sort((l1, l2) -> 
            LocalTime.parse(l1.get("time")).compareTo(LocalTime.parse(l2.get("time"))));
        
        LocalTime currentTime = EARLIEST_TIME;
        List<Map<String, String>> optimizedLectures = new ArrayList<>();
        
        for (Map<String, String> lecture : dayLectures) {
            Map<String, String> optimizedLecture = new HashMap<>(lecture);
            optimizedLecture.put("time", currentTime.toString());
            optimizedLectures.add(optimizedLecture);
            currentTime = currentTime.plusHours(1);
        }
        
        return optimizedLectures;
    }

    private Map<DayOfWeek, List<Map<String, String>>> groupLecturesByDay() {
        return lectures.stream()
            .collect(Collectors.groupingBy(lecture -> {
                String dateStr = lecture.get("date");
                try {
                    return LocalDate.parse(dateStr, formatter).getDayOfWeek();
                } catch (Exception e) {
                    logger.warning("Invalid date format: " + dateStr);
                    return DayOfWeek.MONDAY;
                }
            }));
    }

    public boolean canOptimize() {
        Map<DayOfWeek, List<Map<String, String>>> lecturesByDay = groupLecturesByDay();
        
        for (List<Map<String, String>> dayLectures : lecturesByDay.values()) {
            if (dayLectures.isEmpty()) continue;
            
            List<LocalTime> times = dayLectures.stream()
                .map(lecture -> LocalTime.parse(lecture.get("time")))
                .sorted()
                .collect(Collectors.toList());
            
            if (times.stream().anyMatch(time -> time.isAfter(EARLIEST_TIME))) {
                return true;
            }
        }
        return false;
    }
}