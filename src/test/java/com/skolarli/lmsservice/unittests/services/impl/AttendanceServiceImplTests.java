package com.skolarli.lmsservice.unittests.services.impl;

import com.skolarli.lmsservice.repository.AttendanceRepository;
import com.skolarli.lmsservice.services.impl.AttendanceServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AttendanceServiceImplTests {
    @Autowired
    AttendanceServiceImpl attendanceService;

    @MockBean
    AttendanceRepository attendanceRepository;

    @BeforeEach
    void setup(){

    }

    @Test
    void testSaveAttendanceSuccess(){
        //TODO
    }

}
