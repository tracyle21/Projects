package coms309.teambeans.Controllers;

import coms309.teambeans.Models.Course;
import coms309.teambeans.Services.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "CourseController", tags = {"Course API"})
@SwaggerDefinition(tags = {
        @Tag(name = "Course API", description = "Admin interface to manage courses")
})
@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "Get List of All Courses Registered")
    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }


    @ApiOperation(value = "Get List of Courses Based on Major Name")
    @GetMapping("/courses/{major}")
    public List<Course> getMajorCourses(@PathVariable String major) {
        return courseService.getMajorCourses(major);
    }

    @ApiOperation(value = "Register/Add a New Course")
    @PostMapping("/courses")
    public void addCourse(@RequestBody Course newCourse) {
        courseService.addCourse(newCourse);
    }

    public void setService(CourseService serv) {
        courseService = serv;
    }
}
