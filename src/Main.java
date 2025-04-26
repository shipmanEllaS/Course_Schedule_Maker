/**********************************************************************************************
 * @file : Main.java
 * @description : Takes information from a text file and assigns it to a course object. Once
 *                all courses are added, they are printed out. A user can input their user-
 *                name to pull up their information, manipulate it, and generate a calendar
 *                or finals schedule view of the course sections they've chosen to save.
 * @author : Ella Shipman
 * @date : 25 April 2025
 *********************************************************************************************/

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        //Set up
        populateProfessors("src/professors.txt");
        Catalogue<Section> catalogue = putSectionsInCatalogue();

        sortCourses(catalogue, 'd');

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

        try {
            sortedCourseFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Prompting user for login
        Scanner scnr = new Scanner(System.in);
        String userInput = "";
        String fileName = "src/";

        System.out.println("Hello! Welcome to the WFU Schedule Maker");
        System.out.print("Please enter your user name (case sensitive): ");
        userInput = scnr.next();

        if (userInput != null) { fileName = "src/" + userInput + ".csv"; }

        FileInputStream userFile = null;        //Check if file already exists
        try {
            userFile = new FileInputStream(fileName);
            userFile.close();
        } catch (IOException e) {
            File newUserFile = new File(fileName);
            System.out.println("It seems you are a new user! Starting the data input process...");
            System.out.println();
            inputNewUserData(fileName, scnr);
        }

        System.out.println();
        System.out.println("Welcome, " + userInput + "!");
        System.out.println();
        System.out.print("Would you like to proceed through the terminal or the GUI? ('t': terminal / 'g': GUI)    ");
        userInput = scnr.nextLine();
        if (userInput.isEmpty() || userInput.isBlank()) { userInput = scnr.nextLine(); }
        while (!(userInput.equals("t")) && !(userInput.equals("g"))) {
            System.out.print(userInput + " is not a valid input, please enter 't' for terminal or 'g' for GUI"    );
            userInput = scnr.nextLine();
        }
        if (userInput.equals("g")) {
            new MainGUI(fileName, catalogue);
        } else {
            promptUser(catalogue, fileName, userInput, scnr);
        }
        System.out.println();
    }

    //Prompts user for menu selection
    public static void promptUser(Catalogue<Section> catalogue, String fileName, String userInput, Scanner scnr) {
        while (!userInput.equalsIgnoreCase("e")) {
            System.out.println("=======MENU=======");
            System.out.println("(a) Edit user data");
            System.out.println("(b) Load saved schedules");
            System.out.println("(c) Create new saved schedule");
            System.out.println("(e) Exit");
            System.out.println("==================");
            System.out.println();
            System.out.print("What would you like to do?    ");
            userInput = scnr.next();

            switch(userInput) {
                case "a":
                    editUserData(fileName, scnr);
                    break;
                case "b":
                    loadSavedSchedules(fileName, scnr, catalogue);
                    break;
                case "c":
                    createSavedSchedule(fileName, scnr);
                    break;
            }
        }

    }

    //Loads saved schedules so user can edit, create calendar view, or create final table
    public static void loadSavedSchedules(String fileName, Scanner scnr, Catalogue<Section> catalogue) {
        String userInput = "null";
        String input;
        String userData = null;
        int choice = -1;
        String[] info;
        ArrayList<String> listOfSchedules = new ArrayList<>();

        File userFile = new File(fileName);
        Scanner fileScan = null;

        try {
            fileScan  = new Scanner(userFile);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open user file to read data.");
            System.exit(1);
        }

        if (!fileScan.hasNext()) {
            System.out.println("It seems you are a new user! Starting the data input process...");
            System.out.println();
            inputNewUserData(fileName, scnr);
        }
        fileScan.close();

        while (!userInput.equalsIgnoreCase("r")) {
            try {
                fileScan = new Scanner(userFile);
            } catch (FileNotFoundException e) {
                System.out.println("Can't open new scanner while loading saved schedules.");
                System.exit(1);
            }
            for (int i = 1; i <= 9; i++) {     //Moving to save location in userFile, if none, create one
                userData = fileScan.nextLine();
            }
            if (userData == null) {
                System.out.println("You have no saved schedules! Creating one now...");
                createSavedSchedule(fileName, scnr);
            }

            System.out.println("=======SAVED SCHEDULE MENU=======");        //Printing menu
            System.out.println("(a) Edit a saved schedule");
            System.out.println("(b) Load calendar view of schedule");
            System.out.println("(c) Load finals schedule of schedule");
            System.out.println("(r) Return back to selection screen");
            System.out.println("==================");
            System.out.println();
            System.out.print("What would you like to do?    ");
            userInput = scnr.next();

            System.out.println("Your Saved Schedules:");    //Printing saved schedules
            int i = 1;
            while (fileScan.hasNextLine()) {
                userData = fileScan.nextLine();
                listOfSchedules.add(userData);
                info = userData.split("~~");
                System.out.print("(" + i + ") - ");
                for (int j = 0; j < info.length; j++) {
                    if (j == 0) {
                        System.out.print(info[0] + ": ");
                    } else if (i == info.length - 1){
                        System.out.print(info[j]);
                    } else {
                        System.out.print(info[j] + ", ");
                    }
                }
                System.out.println();
                i++;
            }
            System.out.println();
            fileScan.close();

            Catalogue<Section> selectedSections = new Catalogue<>();
            switch(userInput) {
                case "a":
                    int position = 0;
                    System.out.print("Which would you like to change? (Please enter a number):    ");
                    position = scnr.nextInt();
                    userInput = scnr.nextLine();    //Accounting for the \n character leftover from nextInt()
                    while (!(position >= 1 && position <= i)) {     //Ensuring number is valid
                        System.out.print("Please enter a valid number:  ");
                        position = scnr.nextInt();
                    }

                    try {
                        fileScan = new Scanner(userFile);
                    } catch (FileNotFoundException e) {
                        System.out.println("Can't open new scanner while loading saved schedules.");
                        System.exit(1);
                    }

                    position += 9;        //Saved schedules start at line 10, add 9 when moving line-by-line through userFile
                    for (int j = 1; j <= position; j++) {
                        userData = fileScan.nextLine();
                    }
                    info = userData.split("~~");


                    System.out.print("Are you adding or removing sections? (adding: 1 / removing: 0)     ");
                    choice = scnr.nextInt();
                    input = null;
                    if (choice == 1) {
                        System.out.print("Please enter the class(es) you would like to add to " + info[0] + ":  ");
                        input = scnr.nextLine();
                        if (input.contains("\n") || input.isEmpty()) {
                            input = scnr.nextLine();
                        }
                        userData = userData + "~~" + input;
                    } else if (choice == 0) {
                        System.out.print("Please enter the class(es) you would like to remove from " + info[0] + " (case sensitive): ");
                        input = scnr.nextLine();
                        if (input.contains("\n") || input.isEmpty()) {
                            input = scnr.nextLine();
                        }
                        userData = info[0];
                        for (int j = 1; j < info.length; j++) {
                            if (info[j].equalsIgnoreCase(input)) {
                                info[j] = "~~";
                            }
                            userData = userData + "~~" + info[j];
                        }
                    }
                    userData = userData.replace(": ", "~~");
                    userData = userData.replace(", ", "~~");     //Formatting
                    userData = userData.replace("~~~", "~");
                    if (userData.endsWith("~~")) {
                        userData = userData.substring(0, (userData.length() - 2));
                    }
                    modifyUserFileAtLine(fileName, position, userData);      //Modify line in userFile
                    System.out.println("Edited successfully! ");
                    System.out.println();
                    break;
                case "b":
                    System.out.print("Select a schedule to generate a calendar view (Please enter a number):    ");
                    choice = scnr.nextInt();
                    userInput = scnr.nextLine();    //Accounting for the \n character leftover from nextInt()
                    selectedSections = generateCatalogueFromSectionList(listOfSchedules.get(choice - 1), catalogue);
                    createCalendarView(selectedSections);
                    break;
                case "c":
                    System.out.print("Select a schedule to generate a finals table (Please enter a number):    ");
                    choice = scnr.nextInt();
                    userInput = scnr.nextLine();    //Accounting for the \n character leftover from nextInt()
                    selectedSections = generateCatalogueFromSectionList(listOfSchedules.get(choice - 1), catalogue);
                    createFinalsSchedule(selectedSections);
                    break;
            }
        }
        fileScan.close();
    }

    //Return a catalogue of sections based off of a string containing section name and section IDs (ABC 123-D)
    public static Catalogue<Section> generateCatalogueFromSectionList(String savedSchedule, Catalogue<Section> catalogue) {
        Catalogue<Section> sectionsInList = new Catalogue<>();
        int cutoff = savedSchedule.indexOf("~");
        savedSchedule = savedSchedule.substring(cutoff+2);
        String savedSchedule2 = savedSchedule;
        String[] courseName = savedSchedule.split("~~");
        String[] sectionID = savedSchedule2.split("-");
        int i;

        for (i = 0; i < courseName.length; i++) {          //Isolate course names
            courseName[i] = courseName[i].substring(0, 7);
        }

        for (i = 1; i <= sectionID.length -1; i++) {        //Isolate section IDs, HAVE TO SKIP INDEX 0
            sectionID[i] = sectionID[i].substring(0, 1);
        }

        for (i = 0; i < courseName.length; i++) {       //Skip the first element due to schedule title in savedSchedule
            sectionsInList.add_at_tail(searchForString(catalogue, sectionID[i+1], courseName[i]));
        }
        return sectionsInList;
    }

    //Adds a new saved schedule to the user's file
    public static void createSavedSchedule(String fileName, Scanner scnr) {
        String userInput = null;
        String fullScheduleInfo = null;
        String[] info = null;

        FileInputStream userFile = null;
        try {
            userFile = new FileInputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Unable to print in user file.");
            System.exit(1);
        }
        Scanner fileScan = new Scanner (userFile);

        System.out.print("Enter the name of your new Saved Schedule:    ");
        fullScheduleInfo = scnr.nextLine();
        if (fullScheduleInfo.contains("\n") || fullScheduleInfo.isEmpty()) { fullScheduleInfo = scnr.nextLine(); }

        System.out.println("Please enter the short IDs of your classes in this schedule as such: MTH 101,EDU 101,...");
        System.out.print("Classes in " + fullScheduleInfo + ": ");
        userInput = scnr.nextLine();
        info = userInput.split(",");

        for (int i = 0; i < info.length; i++) {     //Determining which line to insert new schedule at
            fullScheduleInfo = fullScheduleInfo + "~~" + info[i];
        }
        fullScheduleInfo = fullScheduleInfo.replace(",", "~~");
        int counter = 1;
        while (fileScan.hasNextLine()) {        //Finding the first blank line
            counter++;
            userInput = fileScan.nextLine();
        }
        fileScan.close();
        if (counter >= 10) {        //Inserting in userFile
            modifyUserFileAtLine(fileName, counter, fullScheduleInfo);
        }
        System.out.println();
    }

    //Add/remove divisionals, additional courses taken, or professors stored in the user file
    public static void editUserData(String fileName, Scanner scnr) {
        String userInput = "null";
        String input;
        String userData = null;
        int choice = -1;
        String[] info;

        File userFile = new File(fileName);
        Scanner fileScan = null;

        try {
            fileScan  = new Scanner(userFile);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open user file to read data.");
            System.exit(1);
        }

        if (!fileScan.hasNext()) {
            System.out.println("It seems you are a new user! Starting the data input process...");
            System.out.println();
            inputNewUserData(fileName, scnr);
        }

        while (!userInput.equalsIgnoreCase("r")) {
            try {
                fileScan = new Scanner(userFile);
            } catch (FileNotFoundException e) {
                System.out.println("Can't open new scanner");
                System.exit(1);
            }
            System.out.println("Editing menu: ");
            System.out.println("(1) Divisionals Completed");
            System.out.println("(2) Courses Taken");
            System.out.println("(3) Professors Taken");
            System.out.println("(r) Return back to selection screen");
            System.out.println();
            System.out.print("What would you like to change? (Please enter a number):    ");
            userInput = scnr.next();

            switch(userInput) {
                case "1":
                    userData = fileScan.nextLine();
                    System.out.println("Your current list of divisionals:");    //Printing divisionals
                    for (int i = 1; i <= 7; i++) {
                        info = userData.split("~~");
                        System.out.print("(" + i + ") - ");
                        for (int j = 0; j < info.length; j++) {
                            if (j == 0) {
                                System.out.print(info[0] + ": ");
                            } else if (i == info.length - 1){
                                System.out.print(info[j]);
                            } else {
                                System.out.print(info[j] + ", ");
                            }
                        }
                        System.out.println();
                        userData = fileScan.nextLine();
                    }
                    fileScan.close();       //Reset scanner to the top
                    try {
                        fileScan = new Scanner(userFile);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Which line would you like to change?");     //Select line to change
                    int lineNum = scnr.nextInt();
                    if (lineNum >= 1 && lineNum <= 7) {         //Find line to change
                        for (int i = 1; i <= lineNum; i++) {
                            userData = fileScan.nextLine();
                        }
                    }
                    fileScan.close();
                    System.out.println();
                    info = userData.split("~~");

                    System.out.print("Are you adding or removing classes? (adding: 1 / removing: 0)     ");
                    choice = scnr.nextInt();
                    input = null;
                    if (choice == 1) {
                        System.out.print("Please enter the class(es) you would like to add to " + info[0] + ":  ");
                        input = scnr.nextLine();
                        if (input.contains("\n") || input.isEmpty()) {
                            input = scnr.nextLine();
                        }
                        userData = userData + "~~" + input;
                    } else if (choice == 0) {
                        System.out.print("Please enter the class(es) you would like to remove from " + info[0] + " (case sensitive): ");
                        input = scnr.nextLine();
                        if (input.contains("\n") || input.isEmpty()) {
                            input = scnr.nextLine();
                        }
                        userData = "";
                        for (int i = 0; i < info.length; i++) {
                            if (info[i].equalsIgnoreCase(input)) {
                                info[i] = "~~";
                            }
                            userData = userData + info[i];
                        }
                    }
                    userData = userData.replace(",", "~~");     //Formatting
                    userData = userData.replace("~~~~", "~~");
                    modifyUserFileAtLine(fileName, lineNum, userData);      //Modify line in userFile
                    info = userData.split("~~");
                    System.out.println("Edited successfully! ");
                    System.out.println();
                    break;

                case "2":
                    for (int i = 1; i <= 8; i++) {      //Identify line in file to modify
                        userData = fileScan.nextLine();
                    }
                    info = userData.split("~~");

                    userData = userData.replaceFirst("~~", ": ");
                    userData = userData.replace("~~", ", ");
                    System.out.println("Here is your current list of non-divisional courses taken at WFU:");        //List contents
                    System.out.println(userData);
                    System.out.println();

                    System.out.print("Are you adding or removing classes? (adding: 1 / removing: 0)     ");     //Adding/removing
                    choice = scnr.nextInt();
                    if (choice == 1) {
                        System.out.print("Please enter the class(es) you would like to add:  ");        //Adding
                        input = scnr.nextLine();
                        if (input.contains("\n") || input.isEmpty()) {
                            input = scnr.nextLine();
                        }
                        userData = userData + ", " + input;
                    } else if (choice == 0) {
                        System.out.print("Please enter the class(es) you would like to remove (case sensitive): ");     //Removing
                        input = scnr.nextLine();
                        if (input.contains("\n") || input.isEmpty()) {
                            input = scnr.nextLine();
                        }
                        String[] coursesToRemove = input.split(",");

                        userData = info[0];
                        for (int i = 1; i < info.length; i++) {
                            for (int j = 0; j < coursesToRemove.length; j++) {
                                if (info[i].equalsIgnoreCase(coursesToRemove[j])) {
                                    info[i] = "~~";
                                }
                            }
                            userData = userData + "~~" + info[i];
                        }
                    }
                    userData = userData.replace(": ", "~~");
                    userData = userData.replace(", ", "~~");     //Formatting
                    userData = userData.replace("~~~", "~");
                    if (userData.endsWith("~~")) {
                        userData = userData.substring(0, (userData.length() - 2));
                    }
                    modifyUserFileAtLine(fileName, 8, userData);      //Modify line in userFile
                    System.out.println("Edited successfully! ");
                    System.out.println();
                    break;
                case "3":
                    for (int i = 1; i <= 9; i++) {      //Identify line in file to modify
                        userData = fileScan.nextLine();
                    }
                    info = userData.split("~~");

                    userData = userData.replaceFirst("~~", ": ");
                    userData = userData.replace("~~", ", ");
                    System.out.println("Here is your current list of saved professors at WFU:");        //List contents
                    System.out.println(userData);
                    System.out.println();

                    System.out.print("Are you adding or removing professors? (adding: 1 / removing: 0)     ");     //Adding/removing
                    choice = scnr.nextInt();
                    String profInputted = null;
                    if (choice == 1) {
                        System.out.print("Please enter the prof(s) you would like to add (Lastname Firstname):  ");        //Adding
                        profInputted = scnr.nextLine();
                        if (profInputted.contains("\n") || profInputted.isEmpty()) {
                            profInputted = scnr.nextLine();
                        }
                        userData = userData + ", " + profInputted;
                    } else if (choice == 0) {
                        System.out.print("Please enter the prof(s) you would like to remove (Lastname Firstname): ");     //Removing
                        profInputted = scnr.nextLine();
                        if (profInputted.contains("\n") || profInputted.isEmpty()) {
                            profInputted = scnr.nextLine();
                        }
                        String[] profsToRemove = profInputted.split(",");

                        userData = info[0];
                        for (int i = 1; i < info.length; i++) {
                            for (int j = 0; j < profsToRemove.length; j++) {
                                if (info[i].equalsIgnoreCase(profsToRemove[j])) {
                                    info[i] = "~~";
                                }
                            }
                            userData = userData + "~~" + info[i];
                        }
                    }
                    userData = userData.replace(": ", "~~");
                    userData = userData.replace(", ", "~~");     //Formatting
                    userData = userData.replace("~~~", "~");
                    if (userData.endsWith("~~")) {
                        userData = userData.substring(0, (userData.length() - 2));
                    }
                    modifyUserFileAtLine(fileName, 9, userData);      //Modify line in userFile
                    System.out.println("Edited successfully! ");
                    System.out.println();
                    break;
            }
        }
        fileScan.close();
    }

    //Inserts a new line in the user file
    public static void modifyUserFileAtLine(String fileName, int lineNum, String replacementLine) {
        ArrayList<String> dataInFile = new ArrayList<>();
        String lineToEdit = null;

        FileInputStream inputFile = null;       //Open input file
        Scanner fileReader = null;
        try {
            inputFile = new FileInputStream(new File(fileName));
            fileReader = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to print in user file.");
            System.exit(1);
        }

        int counter = 1;        //Find line to replace, add in
        while (counter <= lineNum - 1) {
            lineToEdit = fileReader.nextLine();
            lineToEdit = lineToEdit.replace("~~~~", "~~");
            dataInFile.add(lineToEdit);
            counter++;
        }
        if (fileReader.hasNextLine()) { lineToEdit = fileReader.nextLine(); }       //Skip replaced line
        dataInFile.add(replacementLine);
        while (fileReader.hasNextLine()) {  //Add rest of data
            dataInFile.add(fileReader.nextLine());
        }

        try {
            fileReader.close();
            inputFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileOutputStream userFile = null;       //Open output file
        try {
            userFile = new FileOutputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Unable to print in user file.");
            System.exit(1);
        }
        PrintWriter userWriter = new PrintWriter(userFile);

        for (int i = 0; i < dataInFile.size(); i++) {   //Re-print info
            userWriter.println(dataInFile.get(i));
        }
        try {
            userWriter.close();
            userFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Profile set-up for a new user: filling out divisionals, courses taken, and professors
    public static void inputNewUserData(String fileName, Scanner scnr) {
        String userInput = null;
        String[] divisionals = {"DIV I", "DIV II", "DIV III", "DIV IV", "DIV V", "CD", "QR"};

        FileOutputStream userFile = null;
        try {
            userFile = new FileOutputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Unable to print in user file.");
            System.exit(1);
        }
        PrintWriter userWriter = new PrintWriter(userFile);

        System.out.println("Please enter the short ID of your classes for each section as such: MTH 101, EDU 101, ...");
        System.out.println("If you have no classes to input, please press enter twice to skip.");

        for (int i = 0; i < divisionals.length; i++) {
            System.out.print("For " + divisionals[i] + ", you have taken: ");
            userInput = scnr.nextLine();
            if (userInput.equals("\n") || userInput.isEmpty()) {
                userInput = scnr.nextLine();
            }
            userInput = userInput.replace(",", "~~");
            userWriter.println(divisionals[i] + "~~" + userInput);
        }
        System.out.println();

        System.out.print("If there are any classes you have taken that you have not listed above, please do so here:   ");
        userInput = scnr.nextLine();

        if (!userInput.isEmpty()) {
            userInput.replace(",", "~~");
            userWriter.println("Additional Courses Taken~~" + userInput);
        }

        System.out.print("If there are any professors you would like to store, please do so here (Firstname Lastname): ");
        userInput = scnr.nextLine();
        userInput = userInput.replace(",", "~~");
        userWriter.println("Professors Taken~~" + userInput);

        try {
            userWriter.close();
            userFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("You have completed profile setup, thank you!");
        System.out.println();
    }

    //Populares the professor catalogue from professors.txt
    public static void populateProfessors(String fileName) {
        FileInputStream profFile = null;
        try {
            profFile = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Professor File Not Found!");
            System.exit(1);
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

            try {
                Professor tempProf = new Professor(dataLine[0], dataLine[1], dataLine[2], dataLine[3], dataLine[4], dataLine[5],
                        Double.parseDouble(dataLine[6]), Integer.parseInt(dataLine[7]),
                        Double.parseDouble(dataLine[8]));
            } catch (Exception e) {
                System.out.println("Unable to add professor to the catalogue");
                System.exit(1);
            }
        }
    }

    //Fills out the main course catalogue from courses listed in courseFile.txt
    public static Catalogue<Section> putSectionsInCatalogue() {
        FileInputStream courseFile = null;
        try {
            courseFile = new FileInputStream("src/courseFile.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open course file!");
            System.exit(1);
        }
        //Scanner
        Scanner fileReader = new Scanner(courseFile);
        //List of classes
        Catalogue<Section> courseCatalogue = new Catalogue<>();

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
            newSect.setSectionID(wordsList[1]);     //Section number
            newSect.setMeetingDay(wordsList[2]);    //Meeting day(s)
            newSect.setMeetingTime(wordsList[3]);   //Meeting times
            newSect.setInstructor(wordsList[14]);   //Instructor
            newSect.setTags(wordsList[15]);         //Tags
        }

        courseCatalogue.print();
        return courseCatalogue;
    }

    //Prints courses into file sortedCourseFile.txt
    public static void printCourses(Node<Section> listHead, PrintWriter courseWriter) {
        Node<Section> curr = listHead;
        if (listHead == null){
            return;
        } else {
            if (curr != null) {
                courseWriter.println(curr.data.toString());
                printCourses(curr.next, courseWriter);
            }
        }
    }

    //Creates week-view calendar pop-up for selected sections
    public static void createCalendarView(Catalogue<Section> sections_selected) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(1.0);
        StdDraw.setCanvasSize(1600, 500);

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
        Node<Section> curr = sections_selected.head;
        for (int i = 0; i < sections_selected.size; i++) {

            for (int j = 0; j < curr.data.getDayOfTheWeek().length; j++) {
                if (curr.data.getDayOfTheWeek()[j] == 1) {
                    StdDraw.rectangle(((((double)(j + 1) / 10) * 1.15) + 0.058), (0.68 - (double)(curr.data.calculateMeetingHour()-8) / 18), 0.055, 0.025);
                    StdDraw.text(((((double)(j + 1) / 10) * 1.15) + 0.058), (0.675 - (double)(curr.data.calculateMeetingHour()-8) / 18), curr.data.getShortID());
                }
            }
            curr = curr.next;
        }

        StdDraw.show();
        StdDraw.pause(20);
    }

    //Creates a table showing the finals schedule for the classes chosen
    public static void createFinalsSchedule(Catalogue<Section> sections_selected) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(1.0);
        StdDraw.setCanvasSize(1000, 600);

        //Header
        StdDraw.text(0.5, 0.9, "Your Finals Schedule");

        String[] days = {"Day 6", "Day 5", "Day 4", "Day 3", "Day 2", "Day 1"};
        String[] times = {"9AM", "2PM", "7PM"};

        StdDraw.rectangle(0.5, 0.5, 0.45, 0.35);
        for (int i = 0; i <= 2; i++) {  //vertical lines
            StdDraw.line((0.27 + (i*0.225)),0.85, (0.27 + (i*0.225)), 0.15);
            StdDraw.text((0.3852 + (i*0.225)), 0.8, times[i]);
        }
        for (int i = 0; i <= 6; i++) {  //horizontal lines
            StdDraw.line(0.05, (0.15 + 0.1*i), 0.95, (0.15 + 0.1*i));
            if (i != 6) {
                StdDraw.text(0.155, (0.2 + 0.1*i), days[i]);
            }
        }

        //Determining where classes will go
        String[] Days = {"MWF", "MW", "WF", "TR"};
        int[] Times = {8, 9, 10, 11, 12, 13, 14, 15, 20};
        String[] Special = {"MTH", "STA", "BUS", "ACC 111", "ACC 221"};
        Node<Section> curr = sections_selected.head;
        for (int i = 0; i < sections_selected.size; i++) {
            double x = 0.0; double y = 0.0;
            for (int j = 0; j < Special.length; j++) {      //MTH, STA, BUS, ACC cases
                if (curr.data.getShortID().contains(Special[j])) {
                    if (curr.data.getShortID().contains("ACC")) { x = 0.8325;
                        if (curr.data.getShortID().contains("1")) { y = 0.75; }
                        else {y = 0.5; } }
                    else { x = 0.3825; y = 0.7; }
                }
            }
            for (int j = 0; j < Times.length; j++) {        //Check time, then day
                if (curr.data.calculateMeetingHour() == Times[j] && (x == 0 || y == 0)) {
                    switch (curr.data.calculateMeetingHour()) {
                        case 8:
                            x = 0.8325;
                            if (curr.data.getMeetingDay().equals("TR")) { y = 0.6;
                            } else { y = 0.3;}
                            break;
                        case 9:
                            if (curr.data.getMeetingDay().equals("MWF")) { x = 0.3825; y = 0.2;
                            } else {
                                y = 0.5;
                                if (curr.data.getMeetingDay().equals("WF")) { x = 0.3825;
                                } else { x = 0.6075; } }
                            break;
                        case 10:
                            x = 0.3825;
                            y = 0.5;
                            break;
                        case 12:
                            if (curr.data.getMeetingDay().equals("MWF")) { x = 0.6075; y = 0.7;
                            } else { y = 0.3; }
                            if (curr.data.getMeetingDay().equals("TR")) { x = 0.3825;
                            } else { x = 0.6075; }
                            break;
                        case 11:
                            y = 0.6;
                            if (curr.data.getMeetingDay().equals("TR")) { x = 0.3825;
                            } else { x = 0.6075; }
                            break;
                        case 13:
                            x = 0.6075; y = 0.3;
                            break;
                        case 14:
                            y = 0.4;
                            if (curr.data.getMeetingDay().equals("TR")) { x = 0.3825;
                            } else { x = 0.6075; }
                            break;
                        case 15:
                            x = 0.6075; y = 0.2;
                            break;
                        case 20:
                            x = 0.8325;
                            if (curr.data.getMeetingDay().equals("TR")) { y = 0.3;
                            } else { y = 0.6; }
                            break;
                    }
                } else {
                    StdDraw.text(x, y, curr.data.getShortID());     //Draw class blocks
                }
            }
            curr = curr.next;
        }
        StdDraw.show();
        StdDraw.pause(20);
    }

    //Sorts classes based on time or department
    public static void sortCourses(Catalogue<Section> catalogue, char sortBy) {
        Node<Section> curr;
        int counter;
        int sectionCompare = 0;

        if (catalogue.isEmpty()) {
            System.out.println("Cannot sort an empty course catalogue.");
            System.exit(1);
        }

        while (!areSectionsSorted(catalogue, sortBy)) {
            curr = catalogue.head;
            counter = 1;

            while (curr.next != null) {
                sectionCompare = curr.data.compareTo(curr.next.data);
                if (sectionCompare < 0) {
                    switch (sortBy) {
                        case 'b':
                            catalogue.swap(counter, (counter + 1));
                            break;
                        case 'd':
                            if (sectionCompare % 2 == 0) {
                                catalogue.swap(counter, (counter + 1));
                            }
                            break;
                        case 't':
                            if (sectionCompare % 2 != 0) {
                                catalogue.swap(counter, (counter + 1));
                            }
                            break;
                    }
                }
                curr = curr.next;
                counter++;
            }
        }
    }

    //Checks if classes is sorted based off of the section comparable
    public static boolean areSectionsSorted(Catalogue<Section> catalogue, char sortBy) {
        Node<Section> curr = catalogue.head;
        int sectionCompare;

        while (curr.next != null) {
            sectionCompare = curr.data.compareTo(curr.next.data);

            if (sectionCompare < 0) {
                if (sortBy == 'b') { return false; }
                else if (sortBy == 'd' && (sectionCompare % 2 == 0)) { return false; }
                else if (sortBy == 't' && (sectionCompare % 2 != 0)) { return false; }
            }
            //System.out.println("isSorted while loop");
            curr = curr.next;
        }
        return true;
    }

    //Calculates the credit hours in a list of sections
    public static double calculateCredit(Catalogue<Section> catalogue) {
        double creditHours = 0;
        Node<Section> curr = catalogue.head;

        while (curr != null) {
            creditHours += curr.data.calculateCreditHours();
            curr = curr.next;
        }
        return creditHours;
    }

    //Returns a section from the main course catalogue based on the course name (ABC 123) and the section id (D)
    public static Section searchForString(Catalogue<Section> catalogue, String sectID, String sectName) {
        Node<Section> curr = catalogue.head;

        while (curr != null) {
            if (curr.data.getShortID().equalsIgnoreCase(sectName)) {
                if (curr.data.getSectionID().equalsIgnoreCase(sectID)) {
                    return curr.data;
                }
            }
            curr = curr.next;
        }
        return null;
    }

}
