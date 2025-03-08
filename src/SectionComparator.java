/**********************************************************************************************
 * @file : SectionComparator.java
 * @description : Compares two Sections against each other to inform how a list is ordered.
 * @author : Ella Shipman
 * @date : 7 March 2025
 *********************************************************************************************/

import java.util.*;

public class SectionComparator implements Comparator<Section> {
    @Override
    public int compare(Section s1, Section s2) {
        int HourCompare = Integer.compare(s1.calculateMeetingHour(), s2.calculateMeetingHour());
        int MinuteCompare = Integer.compare(s1.calculateMeetingMinutes(), s2.calculateMeetingMinutes());

        //Returns in reverse order (latest -> earliest due to current printing method (printCourses in Main)
        if (HourCompare == 0) {
            return MinuteCompare;
        } else {
            return HourCompare;
        }

    }
}
