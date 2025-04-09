/**********************************************************************************************
 * @file : Course.java
 * @description : Contains all of the information for a course offerred at WFU. Parent class
 *                of Section.
 * @author : Ella Shipman
 * @date : 7 March 2025
 *********************************************************************************************/

import java.util.*;

public class Course {
    private String short_id;
    private String title;
    private String academic_level;
    private String course_owner;
    private ArrayList<String> grading_basis = new ArrayList<>();
    private String hours;
    private String description;
    private String format;
    private ArrayList<String> equivalent_courses = new ArrayList<>();
    private String location;
    private ArrayList<String> course_materials = new ArrayList<>();

    public Course() {
        short_id = null;
        title = null;
        academic_level = null;
        course_owner = null;
        hours = null;
        description = null;
        format = null;
        location = null;

        grading_basis = null;
        equivalent_courses = null;
        course_materials = null;
    }

    public void fillIn(String id, String courseName, String level, String department, String time,
                       String desc, String class_style, String place) {
        this.setShortID(id);
        this.setTitle(courseName);
        this.setAcademicLevel(level);
        this.setCourseOwner(department);
        this.setHours(time);
        this.setDescription(desc);
        this.setFormat(class_style);
        this.setLocation(place);
    }

    public void setShortID(String id) {
        short_id = id;
    }
    public String getShortID() {
        return short_id;
    }

    public void setTitle(String name) {
        title = name;
    }
    public String getTitle() {
        return title;
    }

    public void setAcademicLevel(String level) {
        academic_level = level;
    }
    public String getAcademicLevel() {
        return academic_level;
    }

    public void setCourseOwner(String dept) {
        course_owner = dept;
    }
    public String getCourseOwner() {
        return course_owner;
    }

    public void setHours(String num) {
        hours = num;
    }
    public String getHours() {
        return hours;
    }
    public Double calculateCreditHours() {
        Double x;
        char currChar;
        String hrString = "";
        int i = 0;

        if (hours != null) {
            currChar = hours.charAt(0);
            while (currChar != ' ') {
                hrString = hrString + currChar;
                currChar = hours.charAt(i);
            }
        }
        try {
            return Double.parseDouble(hrString);
        } catch (NumberFormatException e) {
            System.out.println("Cannot determine hour load for course.");
            System.exit(1);
        }
        return -1.0;
    }

    public void setDescription(String desc) { description = desc; }
    public String getDescription() { return description; }

    public void setFormat(String type) { format = type; }
    public String getFormat() { return format; }

    public void setLocation(String place) { location = place; }
    public String getLocation() { return location; }

    public void setGradingBasis(ArrayList<String> type) { grading_basis = type; }
    public ArrayList<String> getGradingBasis() { return grading_basis; }

    public void setEquivalentCourses(ArrayList<String> courses) { equivalent_courses = courses; }
    public ArrayList<String> getEquivalentCourses() { return equivalent_courses; }

    public void setCourseMaterials(ArrayList<String> materials) { course_materials = materials; }
    public ArrayList<String> getCourseMaterials() { return course_materials; }

    public void printFullCourse() {
        System.out.println("=================================");
        System.out.println(short_id + " - " + title);
        System.out.println("Academic Level: " + academic_level);
        System.out.println("Course Owner: " + course_owner);
        System.out.println("Grading Basis: " + grading_basis);
        System.out.println("Hours: " + hours);
        System.out.println("Description: " + description);
        System.out.println("Instructional formats: " + format);
        System.out.println("Equivalent Courses: " + equivalent_courses);
        System.out.println("Locations Offered: " + location);
        System.out.print("Course Materials: ");
        if (course_materials == null) {
            System.out.println("Not listed");
        } else {
            System.out.println(course_materials);
        }
    }

    public void shortPrint() { System.out.print(short_id + ": " + title); }


}
