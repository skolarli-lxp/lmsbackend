package com.skolarli.lmsservice.unittests.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.models.db.Course;
import com.skolarli.lmsservice.models.db.CourseTag;
import com.skolarli.lmsservice.models.db.LmsUser;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.LmsUserService;
import com.skolarli.lmsservice.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CourseService courseService;
    @MockBean
    private LmsUserService lmsUserService;
    @MockBean
    private UserUtils userUtils;

    Course newCourse;
    Course updatedCourse;
    Course existingCourse;
    LmsUser lmsUser;
    LmsUser lmsUserNonAdmin;
    List<CourseTag> courseTagList;


    @BeforeEach
    public void setup() {
        lmsUser = new LmsUser();
        lmsUser.setId(1);
        lmsUser.setFirstName("Jaya");
        lmsUser.setLastName("Nair");
        lmsUser.setEmail("testuser@email.com");
        lmsUser.setPassword("mymockpassword");
        lmsUser.setIsAdmin(true);
        lmsUser.setIsInstructor(false);
        lmsUser.setIsStudent(false);
        lmsUser.setEmailVerified(false);
        lmsUser.setUserIsDeleted(false);

        lmsUserNonAdmin = new LmsUser();
        lmsUserNonAdmin.setId(1);
        lmsUserNonAdmin.setFirstName("Jaya");
        lmsUserNonAdmin.setLastName("Nair");
        lmsUserNonAdmin.setEmail("nonadminuser@email.com");
        lmsUserNonAdmin.setPassword("mymockpassword");
        lmsUserNonAdmin.setIsAdmin(false);
        lmsUserNonAdmin.setIsInstructor(false);
        lmsUserNonAdmin.setIsStudent(false);
        lmsUserNonAdmin.setEmailVerified(false);
        lmsUserNonAdmin.setUserIsDeleted(false);


        courseTagList = List.of(new CourseTag("tag1"), new CourseTag("tag2"));

        newCourse = new Course();
        newCourse.setId(1);
        newCourse.setCourseName("mycoursename");
        newCourse.setCourseDescription("mycoursedescription");

        updatedCourse = new Course();
        updatedCourse.setId(1);
        updatedCourse.setCourseName("mycoursename");
        updatedCourse.setCourseDescription("mycoursedescription");
        updatedCourse.setCourseOwner(lmsUser);


        existingCourse = new Course();
        existingCourse.setId(1);
        existingCourse.setCourseName("myoldcoursename");
        existingCourse.setCourseDescription("mycoursedescription");
        existingCourse.setCourseOwner(lmsUserNonAdmin);

        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken("testuser@email.com", 1));

    }

    @Test
    void addCourseTestSuccess() throws Exception {
        when(courseService.saveCourse(newCourse)).thenReturn(newCourse);
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
        Mockito.verify(courseService).saveCourse(newCourse);
    }

    @Test
    void addCourseTestFailure() throws Exception {
        when(courseService.saveCourse(newCourse)).thenThrow(new RuntimeException("Save failed"));
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError());
        Mockito.verify(courseService).saveCourse(newCourse);
    }

    @Test
    void updateCourseTestSuccessIsAdmin() throws Exception {
        when(courseService.saveCourse(newCourse)).thenReturn(updatedCourse);
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/courses/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        Mockito.verify(courseService).updateCourse(newCourse, 1);
    }

    @Test
    void updateCourseTestSuccessIsOwner() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(lmsUserNonAdmin);
        when(courseService.getCourseById(1)).thenReturn(existingCourse);
        when(courseService.updateCourse(newCourse, 1)).thenReturn(updatedCourse);
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/courses/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("courseName", is("mycoursename")));
        Mockito.verify(courseService).updateCourse(newCourse, 1);
    }

    @Test
    void updateCourseTestFailureException() throws Exception {
        when(userUtils.getCurrentUser()).thenReturn(lmsUserNonAdmin);
        when(courseService.getCourseById(1)).thenReturn(existingCourse);
        when(courseService.updateCourse(newCourse, 1)).thenThrow(
                new RuntimeException("error occured"));
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/courses/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError());
        Mockito.verify(courseService).updateCourse(newCourse, 1);
    }

    @Test
    void getAllCoursesSuccess() throws Exception {
        List<Course> courseList = List.of(existingCourse);
        when(courseService.getAllCourses()).thenReturn(courseList);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName",
                        is("myoldcoursename")));
        Mockito.verify(courseService).getAllCourses();
    }

    @Test
    void getAllCoursesFailureException() throws Exception {
        when(courseService.getAllCourses()).thenThrow(new RuntimeException("Error Occured"));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError());
        Mockito.verify(courseService).getAllCourses();
    }

    @Test
    void getCourseByIdSuccess() throws Exception {
        when(courseService.getCourseById(1)).thenReturn(existingCourse);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/courses/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("courseName", is("myoldcoursename")));
        Mockito.verify(courseService).getCourseById(1);
    }

    @Test
    void getCourseByIdFailureException() throws Exception {
        when(courseService.getCourseById(1)).thenThrow(new RuntimeException("error occurred"));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/courses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError());
        Mockito.verify(courseService).getCourseById(1);
    }
}
