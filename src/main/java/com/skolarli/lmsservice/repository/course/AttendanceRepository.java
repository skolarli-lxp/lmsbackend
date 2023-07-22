package com.skolarli.lmsservice.repository.course;

import com.skolarli.lmsservice.models.db.course.Attendance;
import com.skolarli.lmsservice.repository.core.TenantableRepository;

import java.util.List;

public interface AttendanceRepository extends TenantableRepository<Attendance> {
    List<Attendance> findByBatchSchedule_Id(long id);
}
