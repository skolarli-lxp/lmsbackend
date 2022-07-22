package com.skolarli.lmsservice.repository;

import com.skolarli.lmsservice.models.db.Attendance;

import java.util.List;

public interface AttendanceRepository extends TenantableRepository<Attendance> {
    List<Attendance> findByBatchSchedule_Id(long id);
}
