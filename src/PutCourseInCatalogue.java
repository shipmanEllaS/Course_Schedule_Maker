//import org.apache.poi;

import java.io.*;

public class PutCourseInCatalogue {
    public static void main(String[] args) {
    //Basically:
        //Open one course's excel file
        //parse information from each cell
        //store information in Course object via Course constructor

        //Open file
        FileInputStream courseFile;
        try {
            courseFile = new FileInputStream("C:\\Users\\lminn\\IdeaProjects\\Course_Schedule_Maker\\src\\courseFile.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open course file!");
            System.exit(1);
        }

        //Scanner


    }
}
