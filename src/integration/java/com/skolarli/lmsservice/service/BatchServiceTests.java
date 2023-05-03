package com.skolarli.lmsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.AbstractContainerBaseTest;
import com.skolarli.lmsservice.services.CourseService;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BatchServiceTests extends AbstractContainerBaseTest {

    @Autowired
    CourseService courseService;

    @Autowired
    private ObjectMapper mapper;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeAll
    public static void setUp() {
        container.withReuse(true);
        container.withInitScript("courseservicetestdata.sql");
        container.start();
    }

    @BeforeEach
    public void setup() throws Exception {

    }

    @Test
    @Order(1)
    void newDomainTestSuccess() throws Exception {


    }

}
