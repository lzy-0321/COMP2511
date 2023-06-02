package staff;

/**
 * A staff member
 * @author Robert Clifton-Everest
 *
 */
public class StaffMember {
    private String name;
    private String salary;
    private String hireDate;
    private String endDate;

    public StaffMember(String name, String salary, String hireDate, String endDate) {
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

    public String getSalary() {
        return salary;
    }

    public String getHireDate() {
        return hireDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public static void main(String[] args) {
        StaffMember staffA = new StaffMember("John", "1000", "01/01/2020", "01/01/2021");
    }
}
