/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._23366044_Server;

import javafx.concurrent.Task;
import java.util.List;
import java.util.Map;

public class OptimizationTask extends Task<List<Map<String, String>>> {
    private final List<Map<String, String>> lectures;
    private final LectureOptimizer optimizer;

    public OptimizationTask(List<Map<String, String>> lectures) {
        this.lectures = lectures;
        this.optimizer = new LectureOptimizer(lectures);
    }

    @Override
    protected List<Map<String, String>> call() throws Exception {
        updateMessage("Starting optimization...");
        updateProgress(0, 100);

        try {
            if (!optimizer.canOptimize()) {
                updateMessage("Lectures already optimized");
                updateProgress(100, 100);
                return lectures;
            }

            updateProgress(25, 100);
            updateMessage("Analyzing current schedule...");
            
            Thread.sleep(500); // Simulate work
            updateProgress(50, 100);
            updateMessage("Optimizing time slots...");
            
            List<Map<String, String>> optimizedSchedule = optimizer.optimizeSchedule();
            
            updateProgress(75, 100);
            updateMessage("Finalizing changes...");
            Thread.sleep(500); // Simulate work
            
            updateProgress(100, 100);
            updateMessage("Optimization complete");
            
            return optimizedSchedule;
        } catch (Exception e) {
            updateMessage("Optimization failed: " + e.getMessage());
            throw e;
        }
    }
}