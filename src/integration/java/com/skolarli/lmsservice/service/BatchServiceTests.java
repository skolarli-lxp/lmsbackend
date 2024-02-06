package com.skolarli.lmsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.AbstractContainerBaseTest;
import com.skolarli.lmsservice.services.course.CourseService;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BatchServiceTests extends AbstractContainerBaseTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    CourseService courseService;
    private final ObjectMapper mapper;

    public BatchServiceTests(CourseService courseService, ObjectMapper mapper) {
        super();
        this.courseService = courseService;
        this.mapper = mapper;
    }

    @BeforeAll
    public static void setUp() {
        container.withReuse(true);
        container.withInitScript("courseservicetestdata.sql");
        container.start();
    }

    @BeforeEach
    public void setup() {

    }

    @Test
    @Order(1)
    void newDomainTestSuccess() {


    }

}
