package com.skolarli.lmsservice.tests.controller;

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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public  void setup() throws Exception {
        lmsUser = new LmsUser(1, "Jaya", "Nair", "testuser@email.com",
                "mymockpassword", true, false, false, null);

        lmsUserNonAdmin = new LmsUser(1, "Jaya", "Nair", "nonadminuser@email.com",
                "mymockpassword", false, false, false, null);

        courseTagList = List.of(new CourseTag("tag1"), new CourseTag("tag2"));

        newCourse = new Course(1, "mycoursename", 100, null,
                0, 0, null, Course.CourseStatus.PLANNED,
                null, courseTagList);

        updatedCourse = new Course(1, "mycoursename", 100, null,
                20, 500, lmsUser, Course.CourseStatus.PLANNED,
                null, courseTagList);

        existingCourse = new Course(1, "myoldcoursename", 100, null,
                20, 500, lmsUserNonAdmin, Course.CourseStatus.PLANNED,
                null, courseTagList);


        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken("testuser@email.com", 1));

    }

    @Test
    void addCourseTestSuccess() throws Exception{
        when(lmsUserService.getLmsUserByEmail(any())).thenReturn(lmsUserNonAdmin);
        when(courseService.saveCourse(newCourse)).thenReturn(newCourse);
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated());
        Mockito.verify(lmsUserService).getLmsUserByEmail("testuser@email.com");
        Mockito.verify(courseService).saveCourse(newCourse);
    }

    @Test
    void addCourseTestFailure() throws Exception{
        when(lmsUserService.getLmsUserByEmail(any())).thenReturn(lmsUser);
        when(courseService.saveCourse(newCourse)).thenThrow(new RuntimeException("Save failed"));
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError());
        Mockito.verify(lmsUserService).getLmsUserByEmail("testuser@email.com");
        Mockito.verify(courseService).saveCourse(newCourse);
    }

    @Test
    void updateCourseTestSuccessIsAdmin() throws Exception{
        when(userUtils.getCurrentUser()).thenReturn(lmsUser);
        when(courseService.saveCourse(newCourse)).thenReturn(updatedCourse);
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/courses/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        Mockito.verify(courseService).updateCourse(newCourse, 1);
    }

    @Test
    void updateCourseTestSuccessIsOwner() throws Exception{
        when(userUtils.getCurrentUser()).thenReturn(lmsUserNonAdmin);
        when(courseService.getCourseById(1)).thenReturn(existingCourse);
        when(courseService.updateCourse(newCourse,1)).thenReturn(updatedCourse);
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/courses/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("name", is("mycoursename")));
        Mockito.verify(courseService).updateCourse(newCourse, 1);
    }

    @Test
    void updateCourseTestFailurePermissionDenied() throws Exception{
        when(userUtils.getCurrentUser()).thenReturn(lmsUserNonAdmin);
        existingCourse.setOwner(lmsUser);
        when(courseService.getCourseById(1)).thenReturn(existingCourse);
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/courses/{id}","1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden());
    }

    @Test
    void updateCourseTestFailureException() throws Exception{
        when(userUtils.getCurrentUser()).thenReturn(lmsUserNonAdmin);
        when(courseService.getCourseById(1)).thenReturn(existingCourse);
        when(courseService.updateCourse(newCourse,1)).thenThrow(new RuntimeException("error occured"));
        String requestJson = mapper.writeValueAsString(newCourse);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/courses/{id}","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isInternalServerError());
        Mockito.verify(courseService).updateCourse(newCourse, 1);
    }

    @Test
    void getAllCoursesSuccess() throws Exception{
        List<Course> courseList = List.of(existingCourse);
        when(courseService.getAllCourses()).thenReturn(courseList);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].name", is("myoldcoursename")));
        Mockito.verify(courseService).getAllCourses();
    }

    @Test
    void getAllCoursesFailureException() throws Exception{
        List<Course> courseList = List.of(existingCourse);
        when(courseService.getAllCourses()).thenThrow(new RuntimeException("Error Occured"));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/courses")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isInternalServerError());
        Mockito.verify(courseService).getAllCourses();
    }

        @Test
    void getCourseByIdSuccess() throws Exception{
        when(courseService.getCourseById(1)).thenReturn(existingCourse);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/courses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                        .andExpect(jsonPath("name", is("myoldcoursename")));
        Mockito.verify(courseService).getCourseById(1);
    }

    @Test
    void getCourseByIdFailureException() throws Exception{
        when(courseService.getCourseById(1)).thenThrow(new RuntimeException("error occurred"));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/courses/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isInternalServerError());
        Mockito.verify(courseService).getCourseById(1);
    }
}
