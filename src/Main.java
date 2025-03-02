/**********************************************************************************************
 * @file : PutCourseInCatalogue.java
 * @description : Takes information from a text file and assigns it to a course object. Once
 *                all courses are added, they are printed out.
 * @author : Ella Shipman
 * @date : 10 February 2023
 *********************************************************************************************/

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<Section> courses = putCoursesInCatalogue();
        createCalendarView(courses);
    }

    public static void createCalendarView(ArrayList<Section> courses_selected) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(1.0);
        StdDraw.setCanvasSize(1600, 400);

        //Header
        StdDraw.text(0.5, 0.9, "Your Course Schedule");

        //Horizontals
        StdDraw.line(0.114, 0.1, 0.919, 0.1);
        StdDraw.line(0.114, 0.8, 0.919, 0.8);
        StdDraw.line(0.114, 0.7, 0.919, 0.7);

        //Verticals
        String[] days_of_the_week = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 1; i < 9; i++) {
            StdDraw.line((((double)(i) / 10) * 1.15), 0.1, (((double)(i) / 10) * 1.15), 0.8);
            if (i != 8) {
                StdDraw.text(((((double) (i) / 10) * 1.15) + 0.055), 0.75, days_of_the_week[i - 1]);
            }
        }

        //Class blocks
        System.out.println("Courses list Size: " + courses_selected.size());
        for (int i = 0; i < courses_selected.size(); i++) {
            for (int j = 0; j < courses_selected.get(i).getDayOfTheWeek().length; j++) {
                if (courses_selected.get(i).getDayOfTheWeek()[j] == 1) {
                    StdDraw.circle(((((double)(j + 1) / 10) * 1.15) + 0.055), (0.65 - (double)(i) / 10), 0.01);
                }
            }
        }

        StdDraw.show();
        StdDraw.pause(20);
    }

    public static ArrayList<Section> putCoursesInCatalogue() {
        FileInputStream courseFile = null;
        try {
            courseFile = new FileInputStream("C:\\Users\\lminn\\IdeaProjects\\Course_Schedule_Maker\\src\\courseFile.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open course file!");
            System.exit(1);
        }

        //Scanner
        Scanner fileReader = new Scanner(courseFile);

        //List of classes
        ArrayList<Section> courseCatalogue = new ArrayList<>();

        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            String[] wordsList = data.split(";");

            //Setting up section
            Section newSect = new Section();
            newSect.fillIn(wordsList[0], wordsList[1], wordsList[2],wordsList[3], wordsList[5], wordsList[6], wordsList[7], wordsList[9]);
            courseCatalogue.add(newSect);

            //Adding grading bases
            ArrayList<String> grade = new ArrayList<>();
            String[] basisList = wordsList[4].split(",");
            for (int i = 0; i < basisList.length; i++) {
                grade.add(basisList[i]);
            }
            newSect.setGradingBasis(grade);

            //Adding equivalent courses
            ArrayList<String> equivalents = new ArrayList<>();
            String[] courseList = wordsList[8].split(",");
            for (int i = 0; i < courseList.length; i++) {
                equivalents.add(courseList[i]);
            }
            newSect.setEquivalentCourses(equivalents);

            //Adding course materials
            ArrayList<String> materials = new ArrayList<>();
            String[] materialsList;
            try {
                materialsList = wordsList[10].split(",");
            } catch (IndexOutOfBoundsException e) {
                materialsList = null;
            }

            if (materialsList != null) {
                for (int i = 0; i < materialsList.length; i++) {
                    materials.add(materialsList[i]);
                }
                newSect.setCourseMaterials(materials);
            }

            //MEETING DAYYYY
            newSect.setMeetingDay("TR");

            newSect.printCourse();
            }
            return courseCatalogue;
        }
    }

