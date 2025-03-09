/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client_23375175_23366044;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author louis
 */

// View Class
public class UIConstants {
    
public static final List<String> TIME_SLOTS = Arrays.asList(
            "09:00", "10:00", "11:00", "12:00", "13:00",
            "14:00", "15:00", "16:00", "17:00", "18:00"
    );

    public static final List<String> ROOMS = Arrays.asList(
            "CSG001", "CSG002", "CSG003", "CSG004", "CSG005",
            "CS1001", "CS1002", "CS1003", "CS1004", "CS1005",
            "CS2001", "CS2002", "CS2003", "CS2004", "CS2005"
    );

    public static final List<String> LECTURER_ACTIONS = Arrays.asList(
            "Add Lecturer", "Remove Lecturer", "Display Lecturers"
    );

    public static final List<String> LECTURE_ACTIONS = Arrays.asList(
            "Add Lecture", "Remove Lecture", "Display Lectures"
    );
    
}
