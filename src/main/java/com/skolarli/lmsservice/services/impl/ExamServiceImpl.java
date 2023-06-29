package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.dto.NewExamQuestionRequest;
import com.skolarli.lmsservice.services.ExamService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExamServiceImpl implements ExamService {


    @Override
    public Exam getExam(long id) {
        return null;
    }

    @Override
    public List<Exam> getAllExams() {
        return null;
    }

    @Override
    public Exam saveExam(Exam exam) {
        return null;
    }

    @Override
    public List<Exam> saveAllExams(List<Exam> exams) {
        return null;
    }

    @Override
    public Exam addQuestionToExam(NewExamQuestionRequest newExamQuestionRequest, long id) {
        return null;
    }

    @Override
    public Exam updateExams(Exam exam, long id) {
        return null;
    }

    @Override
    public void hardDeleteExam(long id) {

    }
}
