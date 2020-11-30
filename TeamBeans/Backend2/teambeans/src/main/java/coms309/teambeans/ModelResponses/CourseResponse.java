package coms309.teambeans.ModelResponses;

import coms309.teambeans.Models.Course;

import java.util.Collection;
import java.util.List;

public class CourseResponse {
    List<String> courses;

    public CourseResponse() {
    }

    public CourseResponse(List<String> courses) {
        this.courses = courses;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
}
