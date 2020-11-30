package coms309.teambeans.Repositories;

import coms309.teambeans.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    public List<Course> findByMajorName(String majorName);
    Course findByMajorNameAndNumber(String majorName, int number);
    
}
