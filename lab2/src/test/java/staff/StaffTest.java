package staff;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StaffTest {
    // Add your tests here
    public static void printStaffDetails(StaffMember staffMember) {
        System.out.println(staffMember.toString());
    }

    public static void main(String[] args) {
        StaffMember staff1 = new StaffMember("Bob", 10000.0, LocalDate.of(2010, 1, 1), LocalDate.of(2015, 1, 1));
        Lecturer lecturer1 = new Lecturer("Bob", 10000.0, LocalDate.of(2010, 1, 1), LocalDate.of(2015, 1, 1),
                "School of Computing", 'A');

        StaffMember staff2 = new StaffMember("John", 12000.0, LocalDate.of(2011, 1, 1), LocalDate.of(2016, 1, 1));
        Lecturer lecturer2 = new Lecturer("John", 12000.0, LocalDate.of(2011, 1, 1), LocalDate.of(2016, 1, 1),
                "School of Computing", 'C');
        printStaffDetails(staff1);
        printStaffDetails(lecturer1);
        printStaffDetails(staff2);
        printStaffDetails(lecturer2);
        System.out.println("staff1 is lecturer1: " + staff1.equals(lecturer1));
        System.out.println("staff2 is lecturer2: " + staff2.equals(lecturer2));
        System.out.println("staff1 is staff2: " + staff1.equals(staff2));
        System.out.println("lecturer1 is lecturer2: " + lecturer1.equals(lecturer2));
        System.out.println("staff1 is lecturer2: " + staff1.equals(lecturer2));
        System.out.println("lecturer1 is staff2: " + lecturer1.equals(staff2));
    }

    @Test
    public void testEquals() {
        StaffMember staff1 = new StaffMember("Bob", 10000.0, LocalDate.of(2010, 1, 1), LocalDate.of(2015, 1, 1));
        Lecturer lecturer1 = new Lecturer("Bob", 10000.0, LocalDate.of(2010, 1, 1), LocalDate.of(2015, 1, 1),
                "School of Computing", 'A');

        StaffMember staff2 = new StaffMember("John", 12000.0, LocalDate.of(2011, 1, 1), LocalDate.of(2016, 1, 1));
        Lecturer lecturer2 = new Lecturer("John", 12000.0, LocalDate.of(2011, 1, 1), LocalDate.of(2016, 1, 1),
                "School of Computing", 'C');

        assertEquals(staff1.equals(lecturer1), true);
        assertEquals(staff2.equals(lecturer2), true);
        assertEquals(staff1.equals(staff2), false);
        assertEquals(lecturer1.equals(lecturer2), false);
        assertEquals(staff1.equals(lecturer2), false);
        assertEquals(lecturer1.equals(staff2), false);
    }
}
