package com.skolarli.lmsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.AbstractContainerBaseTest;
import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.CourseService;
import com.skolarli.lmsservice.services.TenantService;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseServiceTests extends AbstractContainerBaseTest {

    @Autowired
    CourseService courseService;

    @Autowired
    private ObjectMapper mapper;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeAll()
    public static void setUp(){
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
