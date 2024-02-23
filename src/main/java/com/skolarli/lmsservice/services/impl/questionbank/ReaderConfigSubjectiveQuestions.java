package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionSubjective;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionSubjectiveRequest;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankSubjectiveRepository;
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
public class ReaderConfigSubjectiveQuestions {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final QuestionBankSubjectiveProcessor questionBankSubjectiveProcessor;

    private final QuestionBankSubjectiveRepository questionBankSubjectiveRepository;

    public ReaderConfigSubjectiveQuestions(JobBuilderFactory jobBuilderFactory,
                                           StepBuilderFactory stepBuilderFactory,
                                           QuestionBankSubjectiveRepository questionBankSubjectiveRepository,
                                           QuestionBankSubjectiveProcessor questionBankSubjectiveProcessor) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.questionBankSubjectiveRepository = questionBankSubjectiveRepository;
        this.questionBankSubjectiveProcessor = questionBankSubjectiveProcessor;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<NewBankQuestionSubjectiveRequest> subjectiveRequestItemReader(
        @Value("#{jobParameters[fullPathFileName]}") String pathToFile,
        @Value("#{jobParameters[tenantId]}") Long tenantId,
        @Value("#{jobParameters[userName]}") String userName) {

        SecurityContextHolder.getContext().setAuthentication(new TenantAuthenticationToken(userName, tenantId));

        FlatFileItemReader<NewBankQuestionSubjectiveRequest> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(new File(pathToFile)));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<NewBankQuestionSubjectiveRequest> lineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("Question", "WordCount", "CorrectAnswer", "Marks", "SampleAnswerText",
            "SampleAnswerUrl", "QuestionType", "DifficultyLevel", "QuestionFormat",
            "AnswerFormat", "CourseId", "BatchId", "ChapterId", "LessonId", "StudentId");

        BeanWrapperFieldSetMapper<NewBankQuestionSubjectiveRequest> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(NewBankQuestionSubjectiveRequest.class);

        DefaultLineMapper<NewBankQuestionSubjectiveRequest> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public RepositoryItemWriter<BankQuestionSubjective> bankQuestionSubjectiveWriter() {
        RepositoryItemWriter<BankQuestionSubjective> writer = new RepositoryItemWriter<>();
        writer.setRepository(questionBankSubjectiveRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step bankQuestionSubjectiveStep1(FlatFileItemReader<NewBankQuestionSubjectiveRequest> itemReader) {
        return stepBuilderFactory.get("csv-step").<NewBankQuestionSubjectiveRequest, BankQuestionSubjective>chunk(10)
            .reader(itemReader)
            .processor(questionBankSubjectiveProcessor)
            .writer(bankQuestionSubjectiveWriter())
            .build();
    }

    @Bean(name = "importSubjectiveQuestionsJob")
    public Job runJob(FlatFileItemReader<NewBankQuestionSubjectiveRequest> itemReader) {
        //TODO: Add a verification step to check if the entries are valid
        return jobBuilderFactory.get("importSubjectiveQuestions")
            .flow(bankQuestionSubjectiveStep1(itemReader))
            .end().build();
    }

    @Bean(name = "AsyncJobLauncher2")
    public JobLauncher simpleJobLauncher2(JobRepository jobRepository) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }
}
