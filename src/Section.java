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
    private String tags;
    private int[] meetingDayOfTheWeek;

    public Section() {
        section_id = null;
        meeting_day = null;
        meeting_time = null;
        instructor = null;
    }

    //Returns the days of the week as an int[] (0 - no class, 1 - class)
    public int[] getDayOfTheWeek() {
        if (meetingDayOfTheWeek == null) {
            meetingDayOfTheWeek = calculateMeetingDay(meeting_day);
        }

        return meetingDayOfTheWeek;
    }

    public void setMeetingDay (String days) {
        meeting_day = days;
    }

    public void setSectionID (String id) {
        section_id = id;
    }

    public void setMeetingTime (String time) {
        meeting_time = time;
    }

    public void setInstructor (String name) {
        instructor = name;
    }

    public void setTags (String tags) {
        this.tags = tags;
    }

    //Converts the meeting days into a list of numbers (0 - not meeting, 1 - meeting)
    private int[] calculateMeetingDay(String days) {
        int[] results = {0, 0, 0, 0, 0, 0, 0};
        char[] possible_days = {'S', 'M', 'T', 'W', 'R', 'F', 's'};

        if (days.equals(null)) {
            return results;
        }

        for (int i = 0; i <= days.length() - 1; i++) {
            for (int j = 0; j < possible_days.length; j++) {
                if (days.charAt(i) == possible_days[j]) {
                    results[j] = 1;
                }
            }
        }
        return results;
    }
}
