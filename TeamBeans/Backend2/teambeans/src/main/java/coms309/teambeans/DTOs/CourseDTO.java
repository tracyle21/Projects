package coms309.teambeans.DTOs;

public class CourseDTO {
    private String username;
    private String majorName;
    private int number;

    public CourseDTO() {
    }

    public CourseDTO(String username, String majorName, int number) {
        this.username = username;
        this.majorName = majorName;
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
