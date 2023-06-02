package staff;

import java.time.LocalDate;

public class Lecturer extends StaffMember {
    private String school;
    private char status;

    public Lecturer(String name, Double salary, LocalDate hireDate, LocalDate endDate, String school, char status) {
        super(name, salary, hireDate, endDate);
        this.school = school;
        if (status != 'A' && status != 'B' && status != 'C') {
            throw new IllegalArgumentException("Invalid status");
        } else {
            this.status = status;
        }
    }

    public String getSchool() {
        return school;
    }

    public char getStatus() {
        return status;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String output = "";
        output += super.toString();
        output += "School: " + getSchool() + "\n";
        output += "Status: " + getStatus() + "\n";
        return output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Lecturer) {
            Lecturer other = (Lecturer) obj;
            return super.equals(other) && this.getSchool().equals(other.getSchool())
                    && this.getStatus() == other.getStatus();
        } else {
            return false;
        }
    }
}
