package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionTrueOrFalse;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionTrueOrFalseRequest;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankTrueOrFalseRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;

@Configuration
@EnableBatchProcessing
public class ReaderConfigTrueFalseQuestions {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final QuestionBankTrueFalseProcessor questionBankTrueFalseProcessor;

    private final QuestionBankTrueOrFalseRepository questionBankTrueFalseRepository;


    public ReaderConfigTrueFalseQuestions(JobBuilderFactory jobBuilderFactory,
                                          StepBuilderFactory stepBuilderFactory,
                                          QuestionBankTrueOrFalseRepository questionBankTrueFalseRepository,
                                          QuestionBankTrueFalseProcessor questionBankTrueFalseProcessor) {


        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.questionBankTrueFalseRepository = questionBankTrueFalseRepository;
        this.questionBankTrueFalseProcessor = questionBankTrueFalseProcessor;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<NewBankQuestionTrueOrFalseRequest> trueFalseRequestItemReader(
        @Value("#{jobParameters[fullPathFileName]}") String pathToFile,
        @Value("#{jobParameters[tenantId]}") Long tenantId,
        @Value("#{jobParameters[userName]}") String userName) {

        SecurityContextHolder.getContext().setAuthentication(
            new TenantAuthenticationToken(userName, tenantId));

        FlatFileItemReader<NewBankQuestionTrueOrFalseRequest> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(new File(pathToFile)));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<NewBankQuestionTrueOrFalseRequest> lineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("Question", "Option1", "Option2", "CorrectAnswer", "Marks", "SampleAnswerText",
            "SampleAnswerUrl", "QuestionType", "DifficultyLevel", "QuestionFormat",
            "AnswerFormat", "CourseId", "BatchId", "ChapterId", "LessonId", "StudentId");

        BeanWrapperFieldSetMapper<NewBankQuestionTrueOrFalseRequest> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(NewBankQuestionTrueOrFalseRequest.class);

        DefaultLineMapper<NewBankQuestionTrueOrFalseRequest> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public RepositoryItemWriter<BankQuestionTrueOrFalse> bankQuestionTrueFalseWriter() {
        RepositoryItemWriter<BankQuestionTrueOrFalse> writer = new RepositoryItemWriter<>();
        writer.setRepository(questionBankTrueFalseRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step bankQuestionTrueFalseStep1(FlatFileItemReader<NewBankQuestionTrueOrFalseRequest> itemReader) {
        return stepBuilderFactory.get("csv-step").<NewBankQuestionTrueOrFalseRequest, BankQuestionTrueOrFalse>chunk(10)
            .reader(itemReader)
            .processor(questionBankTrueFalseProcessor)
            .writer(bankQuestionTrueFalseWriter())
            .build();
    }

    @Bean(name = "importTrueFalseQuestionsJob")
    public Job runJob(FlatFileItemReader<NewBankQuestionTrueOrFalseRequest> itemReader) {
        //TODO: Add a verification step to check if the entries are valid
        return jobBuilderFactory.get("importSubjectiveQuestions")
            .flow(bankQuestionTrueFalseStep1(itemReader))
            .end().build();
    }

    @Bean(name = "AsyncJobLauncher3")
    public JobLauncher simpleJobLauncher3(JobRepository jobRepository) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }
}
