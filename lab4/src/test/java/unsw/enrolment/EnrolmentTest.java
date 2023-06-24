package unsw.enrolment;

import java.io.IOException;

import unsw.enrolment.exceptions.InvalidEnrolmentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import org.junit.jupiter.api.Test;

public class EnrolmentTest {
    private List<Student> parseStudentsCSV(String path) {
        String fileContents;

        try {
            fileContents = new String(EnrolmentTest.class.getResourceAsStream(path).readAllBytes());
        } catch (IOException e) {
            return null;
        }

        CSVParser csvParser = null;

        try {
            csvParser = CSVParser.parse(fileContents, CSVFormat.RFC4180);
        } catch (IOException e) {
            return null;
        }

        List<Student> students = new ArrayList<Student>();

        csvParser.forEach(record -> {
            if (record.getRecordNumber() == 1)
                return;
            students.add(new Student(record.get(0), record.get(1), Integer.parseInt(record.get(2)),
                    record.get(3).split(" ")));
        });

        return students;
    }

    @Test
    public void testIntegration() {
        // Create courses
        Course cs1511 = new Course("COMP1511", "Programming Fundamentals");
        Course cs1531 = new Course("COMP1531", "Software Engineering Fundamentals");
        cs1531.addPrereq(cs1511);
        Course cs2521 = new Course("COMP2521", "Data Structures and Algorithms");
        cs2521.addPrereq(cs1511);

        CourseOffering cs1511Offering = new CourseOffering(cs1511, "19T1");
        CourseOffering cs1531Offering = new CourseOffering(cs1531, "19T1");
        CourseOffering cs2521Offering = new CourseOffering(cs2521, "19T2");

        // Create a student
        Student student1 = new Student("z5555555", "Jon Snow", 3707, new String[] {
                "SENGAH"
        });

        // Enrol the student in COMP1511 for T1 (this should succeed)
        assertDoesNotThrow(() -> {
            cs1511Offering.addEnrolment(student1);
        });
        assertTrue(student1.isEnrolled(cs1511Offering));

        // Enrol the student in COMP1531 for T1 (this should fail as they
        // have not met the prereq)
        assertThrows(InvalidEnrolmentException.class, () -> {
            cs1531Offering.addEnrolment(student1);
        });

        // Give the student a passing grade for COMP1511
        Grade student1comp1511grade = new Grade(cs1511Offering, 98, "HD");
        student1.setGrade(student1comp1511grade);

        // Enrol the student in 2521 & 1531 (this should succeed as they have met
        // the prereqs)
        assertDoesNotThrow(() -> {
            cs2521Offering.addEnrolment(student1);
            cs1531Offering.addEnrolment(student1);
        });

        assertTrue(student1.isEnrolled(cs2521Offering));
        assertTrue(student1.isEnrolled(cs1531Offering));
    }

    @Test
    public void testComparator() {
        List<Student> students = parseStudentsCSV("/students.csv");
        Course cs1511 = new Course("COMP1511", "Programming Fundamentals");
        CourseOffering cs1511Offering = new CourseOffering(cs1511, "19T1");
        for (Student student : students) {
            assertDoesNotThrow(() -> {
                cs1511Offering.addEnrolment(student);
            });
        }
        // current order of students is:
        // z5204829, z5122521, z5169779, z5169766, z5259819, z5263737, z5210932,
        // z5157372, z5260633, z5260889, z5204996, z5169811, z5113139, z5255918,
        // z5214750, z5208437
        List<String> expectedStudents = new ArrayList<String>();
        expectedStudents.addAll(Arrays.asList("z5204829", "z5122521", "z5169779", "z5169766", "z5259819", "z5263737",
                "z5210932", "z5157372", "z5260633", "z5260889", "z5204996", "z5169811", "z5113139", "z5255918",
                "z5214750", "z5208437"));
        List<String> actualStudents1 = new ArrayList<String>();
        List<Student> actualList = cs1511Offering.studentsEnrolledInCourse();
        actualList.stream().forEach(student -> actualStudents1.add(student.getZID()));
        assertEquals(expectedStudents, actualStudents1);
    }
}
