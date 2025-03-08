/**********************************************************************************************
 * @file : Professor.java
 * @description : Contains all of the information pertaining to a professor at WFU.
 * @author : Ella Shipman
 * @date : 7 March 2025
 *********************************************************************************************/

public class Professor {
    private String name;
    private String title;
    private String department;
    private String office;
    private String phone;
    private String email;
    private double rmp_five_rating;
    private int rmp_would_take_again;
    private double rmp_difficulty;

    public Professor() {
        name = "N/A";
        title = "N/A";
        department = "N/A";
        office = "N/A";
        phone = "N/A";
        email = "N/A";
        rmp_five_rating = -1;
        rmp_would_take_again = -1;
        rmp_difficulty = -1;
    }

    public Professor(String name, String title, String department, String office, String phone, String email, double rmp_five_rating,
                     int rmp_would_take_again, double rmp_difficulty) {
        this.name = name;
        this.title = title;
        this.department = department;
        this.office = office;
        this.phone = phone;
        this. email = email;
        this.rmp_five_rating = rmp_five_rating;
        this.rmp_would_take_again = rmp_would_take_again;
        this.rmp_difficulty = rmp_difficulty;
    }

    public void setName(String name) {this.name = name;}
    public String getName() {return name;}

    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public void setDepartment(String department) {this.department = department;}
    public String getDepartment() {return department;}

    public void setOffice(String office) {this.office = office;}
    public String getOffice() {return office;}

    public void setPhone(String phone) {this.phone = phone;}
    public String getPhone() {return phone;}

    public void setEmail(String email) {this.email = email;}
    public String getEmail() {return email;}

    public void setRMPFiveRating(int rmp_five_rating) {this.rmp_five_rating = rmp_five_rating;}
    public double getRMPFiveRating() {return rmp_five_rating;}

    public void setRMPWouldTakeAgain(int rmp_would_take_again) {this.rmp_would_take_again = rmp_would_take_again;}
    public int getRMPWouldTakeAgain() {return rmp_would_take_again;}

    public void setRMPDifficulty(int rmp_difficulty) {this.rmp_difficulty = rmp_difficulty;}
    public double getRMPDifficulty() {return rmp_difficulty;}


}


