package unsw.enrolment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import unsw.enrolment.exceptions.InvalidEnrolmentException;

public class CourseOffering {
    private Course course;
    private String term;
    private List<Enrolment> enrolments = new ArrayList<Enrolment>();

    public CourseOffering(Course course, String term) {
        this.course = course;
        this.course = course;
        this.term = term;
        this.course.addOffering(this);
    }

    public Course getCourse() {
        return course;
    }

    public String getCourseCode() {
        return course.getCourseCode();
    }

    public List<Course> getCoursePrereqs() {
        return course.getPrereqs();
    }

    public String getTerm() {
        return term;
    }

    public Enrolment addEnrolment(Student student) throws InvalidEnrolmentException {
        if (checkValidEnrolment(student)) {
            Enrolment enrolment = new Enrolment(this, student);
            enrolments.add(enrolment);
            student.addEnrolment(enrolment);
            return enrolment;
        } else {
            throw new InvalidEnrolmentException("student has not satisfied the prerequisites");
        }
    }

    private boolean checkValidEnrolment(Student student) {
        List<Course> prereqs = getCoursePrereqs();

        // for (Course prereq : prereqs) {
        // List<Enrolment> studentEnrolments = student.getEnrolments();
        // boolean valid = false;

        // for (Enrolment enrolment : studentEnrolments) {
        // if (enrolment.getCourse().equals(prereq)) {
        // if (enrolment.hasPassedCourse()) {
        // valid = true;
        // break;
        // }
        // }
        // }

        // if (!valid) {
        // return false;
        // }
        // }

        // use steam way
        for (Course prereq : prereqs) {
            List<Enrolment> studentEnrolments = student.getEnrolments();
            boolean valid = studentEnrolments.stream()
                    .anyMatch(enrolment -> enrolment.getCourse().equals(prereq) && enrolment.hasPassedCourse());
            if (!valid) {
                return false;
            }
        }

        return true;
    }

    public List<Student> studentsEnrolledInCourse() {
        List<Student> students = enrolments.stream().map(Enrolment::getStudent).collect(Collectors.toList());

        Collections.sort(students,
                Comparator.comparingInt(Student::getProgram).thenComparingInt(Student::getStreamsSize)
                        .thenComparing(Student::getName).thenComparing(Student::getZID));
        return students;
    }

}
