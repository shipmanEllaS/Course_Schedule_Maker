/**********************************************************************************************
 * @file : Section.java
 * @description : Contains all of the information within an individual section of any course
 *                offered at WFU. Subclass of Course.
 * @author : Ella Shipman
 * @date : 10 February 2025
 *********************************************************************************************/

public class Section extends Course{
    private String section_id;
    private String meeting_day;
    private String meeting_time;
    private String instructor;

    public Section() {
        section_id = null;
        meeting_day = null;
        meeting_time = null;
        instructor = null;
    }
}
