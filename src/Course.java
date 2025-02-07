import java.util.*;

public class Course {
    private String short_id;
    private String title;
    private String academic_level;
    private String course_owner;
    private ArrayList<String> grading_basis = new ArrayList<>();
    private int hours;
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
        hours = -1;
        description = null;
        format = null;
        location = null;

        grading_basis = null;
        equivalent_courses = null;
        course_materials = null;
    }

    public Course(String id, String name, String level, String department, int time,
                  String desc, String class_style, String place) {
        short_id = id;
        title = name;
        academic_level = level;
        course_owner = department;
        hours = time;
        description = desc;
        format = class_style;
        location = place;

        grading_basis = null;
        equivalent_courses = null;
        course_materials = null;
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

    public void setHours(int num) {
        hours = num;
    }
    public int getHours() {
        return hours;
    }

    public void setDescription(String desc) {
        description = desc;
    }
    public String getDescription() {
        return description;
    }

    public void setFormat(String type) {
        format = type;
    }
    public String getFormat() {
        return format;
    }

    public void setLocation(String place) {
        location = place;
    }
    public String getLocation() {
        return location;
    }

    public void setGradingBasis(ArrayList<String> type) {
        grading_basis = type;
    }
    public ArrayList<String> getGradingBasis() {
        return grading_basis;
    }

    public void setEquivalentCourses(ArrayList<String> courses) {
        equivalent_courses = courses;
    }
    public ArrayList<String> getEquivalentCourses() {
        return equivalent_courses;
    }

    public void setCourseMaterials(ArrayList<String> materials) {
        course_materials = materials;
    }
    public ArrayList<String> getCourseMaterials() {
        return course_materials;
    }


    public void printCourse() {
        System.out.println(short_id + " - " + title);
        System.out.println("Academic Level: " + academic_level);
        System.out.println("Course Owner: " + course_owner);
        System.out.println("Grading Basis: " + grading_basis);
        System.out.println("Hours: " + hours);
        System.out.println("Description: " + description);
        System.out.println("Instructional formats: " + format);
        System.out.println("Equivalent Courses: " + equivalent_courses);
        System.out.println("Locations Offered: " + location);
        System.out.println("Course Materials: " + course_materials);
    }


}
