/**********************************************************************************************
 * @file : Section.java
 * @description : Contains all of the information within an individual section of any course
 *                offered at WFU. Subclass of Course.
 * @author : Ella Shipman
 * @date : 8 April 2025
 *********************************************************************************************/

public class Section extends Course implements Comparable<Section>{
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
        tags = null;
    }

    public Section (Section s) {
        this.section_id = s.section_id;
        this.meeting_day = s.meeting_day;
        this.meeting_time = s.meeting_time;
        this.instructor = s.instructor;
        this.tags = s.tags;
        //From Class Course:
        setShortID(s.getShortID());
        setTitle(s.getTitle());
        setAcademicLevel(s.getAcademicLevel());
        setCourseOwner(s.getCourseOwner());
        setHours(s.getHours());
        setDescription(s.getDescription());
        setFormat(s.getFormat());
        setLocation(s.getLocation());

        setGradingBasis(s.getGradingBasis());
        setEquivalentCourses(s.getEquivalentCourses());
        setCourseMaterials(s.getCourseMaterials());
    }

    //Compares the department and then the time of the sections
    public int compareTo(Section that) {
        int timeCompare = this.compareDept(that.getCourseOwner());
        int deptCompare = this.compareTime(that.calculateMeetingHour(), that.calculateMeetingMinutes());

        if (timeCompare == 0 && deptCompare == 0) {
            return 0;
        } else {
            return (timeCompare + deptCompare);
        }
    }

    public int compareDept(String courseOwner) {
        return this.getCourseOwner().compareTo(courseOwner);
    }

    //Compares the time of this section to another section. returns an integer
    public int compareTime(int hour, int minute) {
        if (this.calculateMeetingHour() == hour) {
            if (this.calculateMeetingMinutes() == minute) {
                return 0;
            } else if (this.calculateMeetingMinutes() > minute) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if (this.calculateMeetingHour() > hour) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    //Returns the days of the week as an int[] (0 - no class, 1 - class)
    public int[] getDayOfTheWeek() {
        if (meetingDayOfTheWeek == null) {
            meetingDayOfTheWeek = calculateMeetingDay(meeting_day);
        }
        return meetingDayOfTheWeek;
    }

    public void setMeetingDay (String days) { meeting_day = days; }

    public void setSectionID (String id) { section_id = id; }
    public String getSectionID() { return section_id; }

    public void setMeetingTime (String time) { meeting_time = time; }

    public void setInstructor (String name) { instructor = name; }

    public void setTags (String tags) { this.tags = tags; }

    @Override
    public String toString() {
        return (getShortID() + "-" + section_id + ": " + getTitle() + ". Meets " + meeting_day + " at " + meeting_time + ". ");
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

    //Returns the hour of meeting_time as an integer (excluding minutes)
    public int calculateMeetingHour() {
        if (meeting_time == null) {
            return -1;
        }
        if (meeting_time.charAt(2) == ':') {            //##:##
            return Integer.parseInt(meeting_time.substring(0, 2));
        } else {                                        //#:##
            return Integer.parseInt(meeting_time.substring(0, 1));
        }
    }

    //Returns the minutes of meeting_time as an integer (excluding hour)
    public int calculateMeetingMinutes() {
        if (meeting_time == null) {
            return -1;
        }
        if (meeting_time.charAt(2) == ':') {            //##:##
            return Integer.parseInt(meeting_time.substring(3, 5));
        } else {                                        //#:##
            return Integer.parseInt(meeting_time.substring(2, 4));
        }
    }
}
