/**********************************************************************************************
 * @file : Main.java
 * @description : Takes information from a text file and assigns it to a course object. Once
 *                all courses are added, they are printed out.
 * @author : Ella Shipman
 * @date : 30 March 2025
 *********************************************************************************************/

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        populateProfessors("C:/Users/lminn/IdeaProjects/Course_Schedule_Maker/src/professors.txt");
        Catalogue catalogue = putSectionsInCatalogue();

        catalogue.sortCourses();

        FileOutputStream sortedCourseFile = null;
        try {
            sortedCourseFile = new FileOutputStream("src/sortedCourseFile.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Output file not found!");
            System.exit(1);
        }

        PrintWriter courseWriter = new PrintWriter(sortedCourseFile);

        printCourses(catalogue.head, courseWriter);

        //Flushing and closing output file
        courseWriter.flush();
        courseWriter.close();

        Catalogue userSections = fillSections(catalogue, args);
        userSections.print();
        userSections.sortCourses();

        //Generating calendar view
        createCalendarView(userSections);
        userSections.print();
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

    public static Catalogue putSectionsInCatalogue() {
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
        Catalogue courseCatalogue = new Catalogue();

        //Filling in section
        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            String[] wordsList = data.split("~~");

            //Setting up section
            Section newSect = new Section();
            newSect.fillIn(wordsList[0], wordsList[4], wordsList[5], wordsList[6], wordsList[8], wordsList[9], wordsList[10], wordsList[12]);
            courseCatalogue.add_at_tail(newSect);

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

        courseCatalogue.print();
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

    //Prints courses into file "sortedCourseFile.txt"
    public static void printCourses(Node listHead, PrintWriter courseWriter) {
        Node curr = listHead;
        if (listHead == null){
            return;
        } else {
            if (curr != null) {
                courseWriter.println(curr.data.toString());
                printCourses(curr.next, courseWriter);
            }
        }
    }

    //Populates a smaller, user-selected linked list of sections to take
    //BROKEN -- WORK ON MANUALLY SELECTING CLASSES LATER. ONLY WORKS FOR ARGS RIGHT NOW
    public static Catalogue promptSections(Catalogue catalogue) {
        Catalogue userSections = new Catalogue();

        System.out.println("Welcome to the WFU Schedule Maker!");
        System.out.println("Please select the classes you would like to take below.         Press 0 to stop.");

        Scanner scnr = new Scanner(System.in);
        String userInput = "1";
        int index = -1;

        System.out.println("Please choose a number from the list in \"sortedCourseFile.txt\" to select your first course.");
        System.out.println("Press 0 to stop.");
        userInput = scnr.next();
        index = -1;

        while (!userInput.equals("0")); {
            if (userSections.calculateCredit() > 17) {
                System.out.println("It seems like you have over 17 credit hours! Please be advised that courseloads over" +
                                    "17 hours may require an academic appeal");
            }

            System.out.println("Please choose a number from the list in \"sortedCourseFile.txt\" to add a course.");
            System.out.println("If you would like to remove courses from your list, please press R.");
            userInput = scnr.next();
            //index = -1;

            try {
                index = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                if (userInput.equalsIgnoreCase("r")) {
                    System.out.println("Which section in your list would you like to remove? (Please enter a number.)");
                    try {
                        userInput = scnr.next();
                        index = Integer.parseInt(userInput);
                    } catch (NumberFormatException e2) {
                        if (userInput.equalsIgnoreCase("c")) {
                            //break;
                        } else {
                            System.out.println("Please enter a valid number, or press C to cancel the swap.");
                        }
                    }
                    Section removed = userSections.remove_from_index(index);
                    catalogue.add_at_tail(removed);
                    catalogue.sortCourses();

                    System.out.println("Section " + removed.toString() + " has been removed.");
                } else {
                    System.out.println("Please enter a valid number.");
                }
            }
            if (index != -1) {
                userSections.add_at_tail(catalogue.remove_from_index(index));
                System.out.println(userSections.tail.data.toString() + " has been added!");
            }
            System.out.println();
        }
        userSections.sortCourses();
        return userSections;
    }

    //Fill the user's personal coureload for the semester
    public static Catalogue fillSections(Catalogue catalogue, String[] args) {
        Catalogue userSections = new Catalogue();
        if (args.length > catalogue.size) { System.out.println("Cannot add sections : out of bounds in catalogue."); System.exit(1);}
        for (int i = 0; i < args.length; i++) {
            catalogue.swap(1, Integer.parseInt(args[i]));
            userSections.add_at_tail(catalogue.remove_from_head());
            System.out.println("Removing " + userSections.tail.data.toString());
        }
        return userSections;
    }

    //Creates week-view calendar pop-up for selected sections
    public static void createCalendarView(Catalogue sections_selected) {
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
        Node curr = sections_selected.head;
        for (int i = 0; i < sections_selected.size; i++) {
            for (int j = 0; j < curr.data.getDayOfTheWeek().length; j++) {
                if (curr.data.getDayOfTheWeek()[j] == 1) {
                    StdDraw.rectangle(((((double)(j + 1) / 10) * 1.15) + 0.058), (0.65 - (double)(i) / 10), 0.055, 0.045);
                    StdDraw.text(((((double)(j + 1) / 10) * 1.15) + 0.058), (0.65 - (double)(i) / 10), curr.data.getShortID());
                }
            }
            curr = curr.next;
        }

        StdDraw.show();
        StdDraw.pause(20);
    }
}


