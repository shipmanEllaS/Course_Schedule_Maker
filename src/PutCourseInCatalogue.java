/**********************************************************************************************
 * @file : PutCourseInCatalogue.java
 * @description : Takes information from a text file and assigns it to a course object. Once
 *                all courses are added, they are printed out.
 * @author : Ella Shipman
 * @date : 10 February 2023
 *********************************************************************************************/

import java.io.*;
import java.util.*;

public class PutCourseInCatalogue {
    public static void main(String[] args) {
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
            newSect.fillIn(wordsList[0], wordsList[1], wordsList[2],wordsList[3], wordsList[5],
                           wordsList[6], wordsList[7], wordsList[9]);

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

            newSect.printCourse();
            }

        }
    }

