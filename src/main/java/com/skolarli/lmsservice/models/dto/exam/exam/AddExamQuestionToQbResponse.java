package com.skolarli.lmsservice.models.dto.exam.exam;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddExamQuestionToQbResponse {
    List<BankQuestionMcq> mcqQuestionsList;
    List<BankQuestionTrueOrFalse> trueOrFalseQuestionsList;
    List<BankQuestionSubjective> subjectiveQuestionsList;
}