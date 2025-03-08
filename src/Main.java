/**********************************************************************************************
 * @file : Main.java
 * @description : Takes information from a text file and assigns it to a course object. Once
 *                all courses are added, they are printed out.
 * @author : Ella Shipman
 * @date : 7 March 2025
 *********************************************************************************************/

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        populateProfessors("C:/Users/lminn/IdeaProjects/Course_Schedule_Maker/src/professors.txt");
        ArrayList<Section> courses = putCoursesInCatalogue();

        sortCourses(courses);

        FileOutputStream sortedCourseFile = null;
        try {
            sortedCourseFile = new FileOutputStream("src/sortedCourseFile.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Output file not found!");
            System.exit(1);
        }

        PrintWriter courseWriter = new PrintWriter(sortedCourseFile);

        printCourses(courses, courses.size(), courseWriter);

        //Flushing and closing output file
        courseWriter.flush();
        courseWriter.close();

        createCalendarView(courses);
    }

    public static void populateProfessors(String fileName) {
        FileInputStream profFile = null;
        try {
            profFile = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Professor File Not Found!");
        }

        Scanner profReader = new Scanner(profFile);

        String data;
        String[] dataLine;
        while (profReader.hasNextLine()) {
            data = profReader.nextLine();
            dataLine = data.split("~~");

            for (int i = 0; i <= dataLine.length - 1; i++) {
                if ((dataLine[i].equals("N/A")) && (i > 4)) {
                    dataLine[i] = "-1";
                }
            }

            Professor tempProf = new Professor(dataLine[0], dataLine[1], dataLine[2], dataLine[3], dataLine[4], dataLine[5],
                    Double.parseDouble(dataLine[6]), Integer.parseInt(dataLine[7]),
                    Double.parseDouble(dataLine[8]));
        }
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
            String[] wordsList = data.split("~~");

            //Setting up section
            Section newSect = new Section();
            newSect.fillIn(wordsList[0], wordsList[4], wordsList[5], wordsList[6], wordsList[8], wordsList[9], wordsList[10], wordsList[12]);
            courseCatalogue.add(newSect);

            //Adding grading bases
            ArrayList<String> grade = new ArrayList<>();
            String[] basisList = wordsList[7].split(",");
            for (int i = 0; i < basisList.length; i++) {
                grade.add(basisList[i]);
            }
            newSect.setGradingBasis(grade);

            //Adding equivalent courses
            ArrayList<String> equivalents = new ArrayList<>();
            String[] courseList = wordsList[11].split(",");
            for (int i = 0; i < courseList.length; i++) {
                equivalents.add(courseList[i]);
            }
            newSect.setEquivalentCourses(equivalents);

            //Adding course materials
            ArrayList<String> materials = new ArrayList<>();
            String[] materialsList;
            try {
                materialsList = wordsList[13].split(",");
            } catch (IndexOutOfBoundsException e) {
                materialsList = null;
            }

            if (materialsList != null) {
                for (int i = 0; i < materialsList.length; i++) {
                    materials.add(materialsList[i]);
                }
                newSect.setCourseMaterials(materials);
            }

            //Section number
            newSect.setSectionID(wordsList[1]);

            //Meeting day(s)
            newSect.setMeetingDay(wordsList[2]);

            //Meeting times
            newSect.setMeetingTime(wordsList[3]);

            //Instructor
            newSect.setInstructor(wordsList[14]);

            //Tags
            newSect.setTags(wordsList[15]);
        }
        return courseCatalogue;

        /*
        0 - id *1
        1 - section
        2 - days
        3 - time
        4 - title *2
        5 - level *3
        6 - dept *4
        7 - grading @
        8 - hours *5
        9 - desc *6
        10 - format *7
        11 - equivalent courses @
        12 - campus *8
        13 - materials @
        14 - instructor
        15 - tags
         */
    }

    //Sort courses cased off of SectionComparator
    public static void sortCourses(ArrayList<Section> classes) {
        SectionComparator sectionCompare = new SectionComparator();

        while (!isSorted(classes, classes.size(), sectionCompare)) {
            for (int i = 0; i < classes.size() - 1; i++) {
                if (sectionCompare.compare(classes.get(i), classes.get(i+1)) < 0) {
                    swap(classes, i, i+1);
                }
            }
        }
    }

    //Checks if classes is sorted based off of SectionComparator
    public static boolean isSorted(ArrayList<Section> classes, int n, SectionComparator SComparing) {
        for (int i = 0; i < n - 1; i++) {
            if (SComparing.compare(classes.get(i), classes.get(i+1)) < 0) {
                return false;
            }
        }
        return true;
    }

    //Swaps two elements in the class list
    public static void swap(ArrayList<Section> classes, int index1, int index2) {
        Section placeholder = new Section(classes.get(index1));
        classes.set(index1, new Section(classes.get(index2)));
        classes.set((index2), new Section(placeholder));
    }

    //Prints courses into file "sortedCourseFile.txt"
    public static void printCourses(ArrayList<Section> classes, int length, PrintWriter courseWriter) {
        if (length == 1) {
            courseWriter.println(classes.get(length - 1).toString());
        } else {
            courseWriter.println(classes.get(length - 1).toString());
            printCourses(classes, length - 1, courseWriter);
        }

    }

    //Creates week-view calendar pop-up for selected sections
    public static void createCalendarView(ArrayList<Section> sections_selected) {
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
        for (int i = 0; i < sections_selected.size(); i++) {
            for (int j = 0; j < sections_selected.get(i).getDayOfTheWeek().length; j++) {
                if (sections_selected.get(i).getDayOfTheWeek()[j] == 1) {
                    StdDraw.rectangle(((((double)(j + 1) / 10) * 1.15) + 0.058), (0.65 - (double)(i) / 10), 0.055, 0.045);
                    StdDraw.text(((((double)(j + 1) / 10) * 1.15) + 0.058), (0.65 - (double)(i) / 10), sections_selected.get(i).getShortID());
                }
            }
        }

        StdDraw.show();
        StdDraw.pause(20);
    }
}


