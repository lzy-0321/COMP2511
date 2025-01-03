package unsw.enrolment;

public class Enrolment {
    private CourseOffering offering;
    private Grade grade;
    private Student student;

    public Enrolment(CourseOffering offering, Student student) {
        this.offering = offering;
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public CourseOffering getOffering() {
        return offering;
    }

    public boolean hasPassedCourse() {
        if (grade == null) {
            return false;
        }

        return grade.hasPassedCourse();
    }

    public Course getCourse() {
        return offering.getCourse();
    }

    public String getTerm() {
        return offering.getTerm();
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
