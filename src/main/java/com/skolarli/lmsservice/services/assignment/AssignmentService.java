package com.skolarli.lmsservice.services.assignment;

import com.skolarli.lmsservice.models.db.assignment.Assignment;
import com.skolarli.lmsservice.models.db.assignment.AssignmentStatus;
import com.skolarli.lmsservice.models.dto.assignment.IndividualQuestionSortOrder;
import com.skolarli.lmsservice.models.dto.assignment.NewAssignmentRequest;

import java.util.List;

public interface AssignmentService {

    Assignment toAssignment(NewAssignmentRequest request);

    Assignment getAssignment(long id);

    List<Assignment> getAllAssignments();

    List<Assignment> getAllAssignmentsForCourse(Long courseId);

    List<Assignment> getAllAssignmentsForBatch(Long batchId);

    List<Assignment> getAllAssignmentsForSchedule(Long scheduleId);

    List<Assignment> getAllAssignmentsForStudent(Long studentId);

    List<Assignment> queryAssignments(Long courseId, Long batchId, Long scheduleId, Long studentId);


    Assignment saveAssignment(Assignment assignment);

    Assignment addQuestionsToAssignment(NewAssignmentRequest newAssignmentRequest, long id);

    Assignment updateAssignmentStatus(AssignmentStatus assignmentStatus, long id);

    Assignment updateAssignment(Assignment assignment, long id);

    Assignment updateAssignmentQuestions(NewAssignmentRequest newAssignmentRequest, long id);

    Assignment updateQuestionSortOrder(List<IndividualQuestionSortOrder> sortOrders, Long assignmentId);


    Assignment nullifyFields(List<String> fieldNames, long id);

    void hardDeleteAssignment(long id);

    void deleteQuestions(Long assignmentId, List<Long> questionIds);

}
