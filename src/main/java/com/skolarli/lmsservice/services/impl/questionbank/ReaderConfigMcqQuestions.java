package com.skolarli.lmsservice.services.impl.questionbank;

import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.models.dto.questionbank.NewBankQuestionMcqRequest;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankMcqRepository;
import com.skolarli.lmsservice.services.impl.questionbank.NewBankQuestionMcqRequestFieldSetMapper;
import com.skolarli.lmsservice.services.impl.questionbank.QuestionBankMcqProcessor;
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
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
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
public class ReaderConfigMcqQuestions {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final QuestionBankMcqProcessor questionBankMcqProcessor;

    private final QuestionBankMcqRepository questionBankMcqRepository;

    public ReaderConfigMcqQuestions(JobBuilderFactory jobBuilderFactory,
                                    StepBuilderFactory stepBuilderFactory,
                                    QuestionBankMcqRepository questionBankMcqRepository,
                                    QuestionBankMcqProcessor questionBankMcqProcessor) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.questionBankMcqRepository = questionBankMcqRepository;
        this.questionBankMcqProcessor = questionBankMcqProcessor;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<NewBankQuestionMcqRequest> itemReader(
        @Value("#{jobParameters[fullPathFileName]}") String pathToFile,
        @Value("#{jobParameters[tenantId]}") Long tenantId,
        @Value("#{jobParameters[userName]}") String userName) {

        SecurityContextHolder.getContext().setAuthentication(
            new TenantAuthenticationToken(userName, tenantId));

        FlatFileItemReader<NewBankQuestionMcqRequest> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(new File(pathToFile)));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<NewBankQuestionMcqRequest> lineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("Question", "NumberOfOptions", "Option1", "Option2", "Option3",
            "Option4", "Option5", "Option6", "NumberOfCorrectAnswers",
            "CorrectAnswer", "SampleAnswerText", "SampleAnswerUrl", "QuestionType",
            "DifficultyLevel", "QuestionFormat", "AnswerFormat", "CourseId", "BatchId",
            "ChapterId", "LessonId", "StudentId");

        FieldSetMapper<NewBankQuestionMcqRequest> fieldSetMapper =
            new NewBankQuestionMcqRequestFieldSetMapper();

        DefaultLineMapper<NewBankQuestionMcqRequest> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public RepositoryItemWriter<BankQuestionMcq> writer() {
        RepositoryItemWriter<BankQuestionMcq> writer = new RepositoryItemWriter<>();
        writer.setRepository(questionBankMcqRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1(FlatFileItemReader<NewBankQuestionMcqRequest> itemReader) {
        return stepBuilderFactory.get("csv-step").<NewBankQuestionMcqRequest, BankQuestionMcq>chunk(10)
            .reader(itemReader)
            .processor(questionBankMcqProcessor)
            .writer(writer())
            .build();
    }

    @Bean(name = "importMcqQuestionsJob")
    public Job runJob(FlatFileItemReader<NewBankQuestionMcqRequest> itemReader) {
        //TODO: Add a verification step to check if the entries are valid
        return jobBuilderFactory.get("importMcQuestions")
            .flow(step1(itemReader))

            .end().build();
    }

    //@Bean
    //public TaskExecutor taskExecutor() {
    //    SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
    //    asyncTaskExecutor.setConcurrencyLimit(10);
    //    return asyncTaskExecutor;
    //}

    @Bean(name = "AsyncJobLauncher")
    public JobLauncher simpleJobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }
}
