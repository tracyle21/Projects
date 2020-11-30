package coms309.teambeans;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;

// import mockito related
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.BDDMockito.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import coms309.teambeans.DTOs.CourseDTO;
import coms309.teambeans.Models.Course;
import coms309.teambeans.Repositories.CourseRepository;
import coms309.teambeans.Services.CourseService;
import net.minidev.json.JSONObject;
import coms309.teambeans.Controllers.CourseController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseTests {

    @Autowired
    private CourseService cService;

    @MockBean
    private CourseRepository repo;

    @Test
    public void MockitoMajorCourseTest() throws Exception {
        List<Course> l = new ArrayList<Course>();
        // Set up MOCK methods for the repository
        Course json = new Course("COMS", 309);
        // set repository to the mocked repository
        when(this.repo.findByMajorName("COMS")).thenReturn(l);
        // set repository to the mocked repository
        l.add(json);
        List<Course> result = cService.getMajorCourses("COMS");
        assertEquals(result, l);
    }

    @Test
    public void MockitoAddCourseTest() throws Exception {
        List<Course> l = new ArrayList<Course>();
        // Set up MOCK methods for the repository
        Course json = new Course("COMS", 309);
        Course json2 = new Course("STATS", 330);
        Course json3 = new Course("ABC", 101);
        when(this.repo.findAll()).thenReturn(l);
        // set repository to the mocked repository
        l.add(json);
        l.add(json2);
        l.add(json3);
        List<Course> result = cService.getAllCourses();
        assertEquals(result, l);
    }

    // ensures all courses list is empty to start with but not null
    @Test
    void GoodCourseListEmpty() {
        CourseController controller = mock(CourseController.class);
        Course course = new Course();
        course.setId((long) 309);
        List<Course> courseList = new ArrayList<Course>();
        List<Course> courseList2 = controller.getAllCourses();
        assertTrue(courseList.equals(courseList2));
    }

    // ensures course Id can be changed (if course/major is redesigned, etc)
    @Test
    void CourseIDChange() {
        CourseController controller = mock(CourseController.class);
        Course course = new Course();
        course.setId(((long) 309));
        course.setMajorName("Com S");
        course.setNumber(309);
        Course course2 = new Course();
        course2.setId(((long) 309));
        course2.setMajorName("Com S");
        course2.setNumber(309);
        course2.setId(((long) 319));
        assertFalse(course.equals(course2));
    }

    // ensures course Major name can be changed (if course/major is redesigned, etc)
    @Test
    void CourseNameChange() {
        CourseController controller = mock(CourseController.class);
        Course course = new Course();
        course.setId(((long) 309));
        course.setMajorName("Com S");
        course.setNumber(309);
        Course course2 = new Course();
        course2.setId(((long) 309));
        course2.setMajorName("Com S");
        course2.setNumber(309);
        course2.setMajorName("Software");
        assertFalse(course.equals(course2));
    }

    // ensures course number can be changed (if course/major is redesigned, etc)
    @Test
    void CourseNumberChange() {
        CourseController controller = mock(CourseController.class);
        Course course = new Course();
        course.setId(((long) 309));
        course.setMajorName("Com S");
        course.setNumber(309);
        Course course2 = new Course();
        course2.setId(((long) 309));
        course2.setMajorName("Com S");
        course2.setNumber(309);
        course2.setNumber(311);
        assertFalse(course.equals(course2));
    }

    // ensures list of courses saves course id field
    @Test
    void CourseListIdChange() {
        CourseController controller = mock(CourseController.class);
        Course course = new Course();
        course.setId((long) 309);
        course.setMajorName("Com S");
        course.setNumber(309);
        List<Course> courseList = new ArrayList<Course>();
        courseList.add(course);
        course.setId((long) 319);
        controller.addCourse(course);
        List<Course> courseList2 = controller.getAllCourses();
        assertFalse(courseList.equals(courseList2));
    }

    // ensures list of courses saves course major name field
    @Test
    void CourseListNameChange() {
        CourseController controller = mock(CourseController.class);
        Course course = new Course();
        course.setId((long) 309);
        course.setMajorName("Com S");
        course.setNumber(309);
        List<Course> courseList = new ArrayList<Course>();
        courseList.add(course);
        course.setMajorName("LAS Undecided Major");
        controller.addCourse(course);
        List<Course> courseList2 = controller.getAllCourses();
        assertFalse(courseList.equals(courseList2));
    }

    // ensures list of courses saves course number field
    @Test
    void CourseListNumberChange() {
        CourseController controller = mock(CourseController.class);
        Course course = new Course();
        course.setId((long) 309);
        course.setMajorName("Com S");
        course.setNumber(309);
        List<Course> courseList = new ArrayList<Course>();
        courseList.add(course);
        course.setNumber(311);
        controller.addCourse(course);
        List<Course> courseList2 = controller.getAllCourses();
        assertFalse(courseList.equals(courseList2));
    }

    // for debugging
    @BeforeAll
    private static void before() {
        System.out.println("Before");
    }

    // for debugging
    @AfterAll
    private static void after() {
        System.out.println("After");
    }
}
