public class Section extends Course{
    private String section_id;

    public Section() {
        section_id = null;
    }

    public Section(String name,String id, String courseName, String level, String department, int time,
                   String desc, String class_style, String place) {
        section_id = name;
        this.setShortID(id);
        this.setTitle(courseName);
        this.setAcademicLevel(level);
        this.setCourseOwner(department);
        this.setHours(time);
        this.setDescription(desc);
        this.setFormat(class_style);
        this.setLocation(place);
    }
}
