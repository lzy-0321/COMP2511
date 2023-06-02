package staff;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * A staff member
 * @author Robert Clifton-Everest
 *
 */
public class StaffMember {
    private String name;
    private Double salary;
    private LocalDate hireDate;
    private LocalDate endDate;

    public StaffMember(String name, Double salary, LocalDate hireDate, LocalDate endDate) {
        if (name == null || salary == null || hireDate == null || endDate == null) {
            throw new NullPointerException("Null value passed to StaffMember constructor");
        } else {
            this.name = name;
            this.salary = salary;
            this.hireDate = hireDate;
            this.endDate = endDate;
        }
    }

    public String getName() {
        return name;
    }

    public Double getSalary() {
        return salary;
    }

    public String getHireDate() {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(hireDate);
    }

    public String getEndDate() {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(endDate);
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        String output = "";
        output += "Name: " + getName() + "\n";
        output += "Salary: $" + getSalary() + "\n";
        output += "Hire Date: " + getHireDate() + "\n";
        output += "End Date: " + getEndDate() + "\n";
        return output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StaffMember) {
            StaffMember staffMember = (StaffMember) obj;
            return staffMember.getName().equals(this.getName()) && staffMember.getSalary().equals(this.getSalary())
                    && staffMember.getHireDate().equals(this.getHireDate())
                    && staffMember.getEndDate().equals(this.getEndDate());
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        StaffMember staffA = new StaffMember("John", 1000.0, LocalDate.of(2010, 1, 1), LocalDate.of(2015, 1, 1));
        System.out.println(staffA.toString());
    }
}
