package com.skolarli.lmsservice.services.impl;

import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Exam;
import com.skolarli.lmsservice.models.dto.exam.NewExamQuestionsAllTypesRequest;
import com.skolarli.lmsservice.models.dto.exam.NewExamRequest;
import com.skolarli.lmsservice.repository.ExamRepository;
import com.skolarli.lmsservice.services.ExamService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExamServiceImpl implements ExamService {

    ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }


    @Override
    public Exam getExam(long id) {
        List<Exam> exams =
                examRepository.findAllById(new ArrayList<>(List.of(id)));
        if (exams.size() == 0) {
            throw new ResourceNotFoundException("Exam with Id " + id + " not found");
        }
        return exams.get(0);
    }

    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @Override
    public List<NewExamQuestionsAllTypesRequest> getAllQuestions(Long id) {
        return null;
    }

    @Override
    public Exam saveExam(NewExamRequest examRequest) {
        return null;
    }

    @Override
    public List<Exam> saveAllExams(List<NewExamRequest> exams) {
        return null;
    }

    @Override
    public Exam addQuestionToExam(NewExamQuestionsAllTypesRequest newExamQuestionRequest, long id) {
        return null;
    }

    @Override
    public Exam updateExams(NewExamRequest exam, long id) {
        return null;
    }

    @Override
    public void hardDeleteExam(long id) {

    }
}
