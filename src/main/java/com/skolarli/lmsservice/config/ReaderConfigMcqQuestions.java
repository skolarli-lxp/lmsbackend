package com.skolarli.lmsservice.config;

import com.skolarli.lmsservice.models.db.questionbank.BankQuestionMcq;
import com.skolarli.lmsservice.repository.questionbank.QuestionBankMcqRepository;
import com.skolarli.lmsservice.services.impl.questionbank.QuestionBankMcqProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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

import java.io.File;

@Configuration
@EnableBatchProcessing
public class ReaderConfigMcqQuestions {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final QuestionBankMcqRepository questionBankMcqRepository;

    public ReaderConfigMcqQuestions(JobBuilderFactory jobBuilderFactory,
                                    StepBuilderFactory stepBuilderFactory,
                                    QuestionBankMcqRepository questionBankMcqRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.questionBankMcqRepository = questionBankMcqRepository;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<BankQuestionMcq> itemReader(
        @Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
        FlatFileItemReader<BankQuestionMcq> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource(new File(pathToFile)));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<BankQuestionMcq> lineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("Question", "numberOfOptions", "Option1", "Option2", "Option3",
            "Option4", "Option5", "Option6", "Number of Correct Answers",
            "correctAnswer", "SampleAnswerText");

        BeanWrapperFieldSetMapper<BankQuestionMcq> fieldSetMapper =
            new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BankQuestionMcq.class);

        DefaultLineMapper<BankQuestionMcq> lineMapper = new DefaultLineMapper<>();
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
    public QuestionBankMcqProcessor processor() {
        return new QuestionBankMcqProcessor();
    }

    @Bean
    public Step step1(FlatFileItemReader<BankQuestionMcq> itemReader) {
        return stepBuilderFactory.get("csv-step").<BankQuestionMcq, BankQuestionMcq>chunk(10)
            .reader(itemReader)
            .processor(processor())
            .writer(writer())
            .build();
    }

    @Bean(name = "importMcqQuestionsJob")
    public Job runJob(FlatFileItemReader<BankQuestionMcq> itemReader) {
        return jobBuilderFactory.get("importCustomers")
            .flow(step1(itemReader)).end().build();
    }

    //@Bean
    //public TaskExecutor taskExecutor() {
    //    SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
    //    asyncTaskExecutor.setConcurrencyLimit(10);
    //    return asyncTaskExecutor;
    //}
}
