package coms309.teambeans.Services;

import coms309.teambeans.Models.Course;
import coms309.teambeans.Repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        courseRepository.findAll().forEach(courses::add);
        return courses;
    }

    public List<Course> getMajorCourses(String majorName) {
        List<Course> courses = new ArrayList<>();
        courseRepository.findByMajorName(majorName).forEach(courses::add);
        return courses;
    }

    public void addCourse(Course course) {
        Course c = new Course(course.getMajorName(), course.getNumber());
        courseRepository.save(course);
    }
}
