/**********************************************************************************************
 * @file : MainGUI.java
 * @description : The GUI for Main.java (using Swing).
 * @author : Ella Shipman
 * @date : 25 April 2025
 *********************************************************************************************/

import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    JLabel div1, div2, div3, div4, div5, cd, qr, additional_courses, profs_taken;
    JTextField d1, d2, d3, d4, d5, dcd, dqr, dac, dpt;

    //The Constructor class for MainGUI, creates the GUI
    public MainGUI(String fileName, Catalogue<Section> catalogue) {
        populateLabels();

        setTitle( "WFU Schedule Maker" );
        setSize( 400, 500 );
        setDefaultCloseOperation( EXIT_ON_CLOSE );

        JTabbedPane mainFrame = new JTabbedPane();

        // needs to be final to be accessed inside the event handlers
        final JPanel editUserDataPanel = new JPanel();
        final JPanel loadSavedSchedulesPanel = new JPanel();
        CardLayout cl = new CardLayout();
        loadSavedSchedulesPanel.setLayout(cl);
        final JPanel createSavedSchedulesPanel = new JPanel();

        //EDIT USER DATA PANEL
        editUserDataPanel.add(div1);
        editUserDataPanel.add(d1);
        editUserDataPanel.add(div2);
        editUserDataPanel.add(d2);
        editUserDataPanel.add(div3);
        editUserDataPanel.add(d3);
        editUserDataPanel.add(div4);
        editUserDataPanel.add(d4);
        editUserDataPanel.add(div5);
        editUserDataPanel.add(d5);
        editUserDataPanel.add(cd);
        editUserDataPanel.add(dcd);
        editUserDataPanel.add(qr);
        editUserDataPanel.add(dqr);
        editUserDataPanel.add(additional_courses);
        editUserDataPanel.add(dac);
        editUserDataPanel.add(profs_taken);
        editUserDataPanel.add(dpt);
        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel[] labels = {div1, div2, div3, div4, div5, cd, qr, additional_courses, profs_taken};
                JTextField[] fields = {d1, d2, d3, d4, d5, dcd, dqr, dac, dpt};

                FileOutputStream file = null;
                PrintWriter userWriter = null;
                try {
                    file = new FileOutputStream(fileName);
                    userWriter = new PrintWriter(file);
                } catch (Exception e2) {
                    inputNewUserData(fileName, labels, fields);
                }
                String line;
                for (int i = 0; i < labels.length; i++) {
                    line = labels[i].getText() + "~~" + fields[i].getText();
                    line.replace(",", "~~");
                    Main.modifyUserFileAtLine(fileName, (i+1), line);
                }
            }
        });
        editUserDataPanel.add(enterButton);


        mainFrame.addTab( "Edit User Data", editUserDataPanel);
        mainFrame.addTab( "Load Saved Schedules", loadSavedSchedulesPanel);
        mainFrame.addTab("Create Saved Schedules", createSavedSchedulesPanel);

        //LOAD SAVED SCHEDULE PANEL
        //Populating the user's list of saved schedules
        ArrayList<String> saved_schedules_string = new ArrayList<>();
        saved_schedules_string = populateSavedSchedules(fileName);
        Catalogue<Section> current_saved_schedule;
        ArrayList<Catalogue<Section>> listOfSchedules = new ArrayList<>();
        for (int i = 0; i < saved_schedules_string.size(); i++) {
            current_saved_schedule = Main.generateCatalogueFromSectionList(saved_schedules_string.get(i), catalogue);
            listOfSchedules.add(current_saved_schedule);
        }

        JButton editSavedScheduleButton, loadCalendarViewButton, loadFinalsScheduleButton;

        //Add saved schedules, modifier buttons to loadSavedSchedulesPane
        Scanner fileScan;
        for (int i = 0; i< listOfSchedules.size(); i++) {
            fileScan = new Scanner(fileName);
            final Catalogue<Section> curr = listOfSchedules.get(i);
            JPanel tab = new JPanel();

            //Initialize vars
            JButton addButton, removeButton;
            JTextField addField, removeField;

            //Adding edit menu to loadSavedSchedules
            tab.setBackground(Color.pink);

            addField = new JTextField(5); removeField = new JTextField(5);
            addField.setEditable(true); removeField.setEditable(true);
            addButton = new JButton("Add"); removeButton = new JButton("Remove");
            final String info = saved_schedules_string.get(i);
            final int lineNum = i+10;
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newLine = info + "~~" + addField.getText();
                    newLine = newLine.replace(",", "~~");     //Formatting
                    newLine = newLine.replace("~~~~", "~~");
                    Main.modifyUserFileAtLine(fileName, lineNum, newLine);      //Modify line in userFile
                    System.out.println(newLine);
                    addField.setText("");
                }
            });
            removeButton.addActionListener( new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newLine = "";
                    String[] list  = info.split("~~");
                    for (int i = 0; i < list.length; i++) {
                        if (list[i].equalsIgnoreCase(removeField.getText())) {
                            list[i] = "~~";
                        }
                        newLine = newLine + "~~" + list[i];
                    }
                    newLine = newLine.substring(2);
                    newLine = newLine.replace(",", "~~");     //Formatting
                    newLine = newLine.replace("~~~", "~");
                    Main.modifyUserFileAtLine(fileName, lineNum, newLine);      //Modify line in userFile
                    System.out.println(newLine);
                    removeField.setText("");

                }
            });
            tab.add(JLabelFromSavedScheduleString(saved_schedules_string.get(i)));
            tab.add(addField); tab.add(addButton);
            tab.add(removeField); tab.add(removeButton);

            loadCalendarViewButton = new JButton("Load Calendar View");
            loadCalendarViewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { Main.createCalendarView(curr); }
            });
            loadFinalsScheduleButton = new JButton("Load Finals Schedule");
            loadFinalsScheduleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { Main.createFinalsSchedule(curr); }
            });
            tab.add(loadCalendarViewButton);
            tab.add(loadFinalsScheduleButton);

            loadSavedSchedulesPanel.add(tab);
            fileScan.close();
        }

        //CREATE NEW SCHEDULE PANEL
        JTextField scheduleTitle = new JTextField(35);
        scheduleTitle.setEditable(true);
        JTextField  addCoursesField = new JTextField(20);
        addCoursesField.setEditable(true);
        JButton addCoursesButton = new JButton("Enter");
        final int lineNum;
        if (saved_schedules_string.size() == 0) { lineNum = 10; }
        else { lineNum = saved_schedules_string.size() + 10;
        }
        addCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String info = scheduleTitle.getText();
                String newLine = info + "~~" + addCoursesField.getText();
                newLine = newLine.replace(",", "~~");     //Formatting
                newLine = newLine.replace("~~~~", "~~");
                Main.modifyUserFileAtLine(fileName, lineNum, newLine);      //Modify line in userFile
                System.out.println(newLine);
                addCoursesField.setText("");
                scheduleTitle.setText("");
            }
        });
        createSavedSchedulesPanel.add(new JLabel("Title: "));
        createSavedSchedulesPanel.add(scheduleTitle);
        createSavedSchedulesPanel.add(new JLabel("Sections: "));
        createSavedSchedulesPanel.add(addCoursesField);
        createSavedSchedulesPanel.add(addCoursesButton);

        //NAVIGATION
        JPanel navigationPanel = new JPanel();

        JButton prevButton = new JButton("<< prev");
        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cl.previous(loadSavedSchedulesPanel);
            }
        });
        navigationPanel.add(prevButton);

        JButton nextButton = new JButton("next >>");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cl.next(loadSavedSchedulesPanel);
            }
        });
        navigationPanel.add(nextButton);

        add(navigationPanel, BorderLayout.SOUTH);
        add(mainFrame, BorderLayout.CENTER );

        setVisible( true );
    }

    //Defines all JLabels nad JTextFields initialized in MainGUI objects
    public void populateLabels() {
        div1 = new JLabel("DIV I");
        div2 = new JLabel("DIV II");
        div3 = new JLabel("DIV III");
        div4 = new JLabel("DIV IV");
        div5 = new JLabel("DIV V");
        cd = new JLabel("CD");
        qr = new JLabel("QR");
        additional_courses = new JLabel("Additional Courses");
        profs_taken = new JLabel("Professors Taken");
        d1 = new JTextField(35);
        d2 = new JTextField(35);
        d3 = new JTextField(35);
        d4 = new JTextField(35);
        d5= new JTextField(35);
        dcd = new JTextField(37);
        dqr = new JTextField(35);
        dac = new JTextField(20);
        dpt = new JTextField(25);
    }

    //Create a new file for new users, fill with information entered into the GUI
    public void inputNewUserData(String fileName, JLabel[] labels, JTextField[] fields) {
        FileOutputStream userFile = null;
        try {
            userFile = new FileOutputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("Unable to print in user file.");
            System.exit(1);
        }
        PrintWriter userWriter = new PrintWriter(userFile);

        String line;
        for (int i = 0; i < labels.length; i++) {       //Adding DIV 1 through Professors Taken
            line = labels[i].getText() + "~~" + fields[i].getText();
            line = line.replace(",", "~~");
            userWriter.println(fileName);
        }

        try {
            userWriter.close();
            userFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("You have completed profile setup, thank you!");
        System.out.println();
    }

    //Creates a JLabel from a saved schedule
    public static JLabel JLabelFromSavedScheduleString(String section) {
        String[] info = section.split("~~");
        String label = "";

        for (int j = 0; j < info.length; j++) {
            if (j == 0) {
                label += (info[0] + ": ");
            } else if (j == info.length - 1) {
                label += info[j];
            } else {
                label += (info[j] + ", ");
            }
        }
        return new JLabel(label);
    }

    //Returns an arraylist of all saved schedules in the user's file (saved as a string, not a catalogue)
    public static ArrayList<String> populateSavedSchedules(String fileName) {
        //Opening file
        File userFile = new File(fileName);
        Scanner fileScan = null;

        try {
            fileScan = new Scanner(userFile);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open user file to read data.");
            System.exit(1);
        }

        //Variables
        ArrayList<String> listOfSchedules = new ArrayList<>();
        String[] info;
        String userData;

        //Populating listOfSchedules
        for (int i = 1; i <= 9; i++) {
            userData = fileScan.nextLine();
        }
        while (fileScan.hasNextLine()) {
            userData = fileScan.nextLine();
            listOfSchedules.add(userData);
        }
        System.out.println();
        fileScan.close();

        return listOfSchedules;
    }
}
