package com.graduationProject._thYear.Configuration;

import java.util.Collections;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Invoice.repositories.InvoiceHeaderRepository;

@Configuration
public class UserSyncJob {

    private List<InvoiceHeader> result;
    @Bean
    public Job syncData(JobRepository jobRepository, Step step1, Step stepTasklet) {
    return new JobBuilder("SyncDataJob", jobRepository)
        .start(step1)
        .next(stepTasklet)
        .build();
    }


  // Step definition
    @Bean
    public Step stepTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepTasklet", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("👉 Hello from Spring Batch 5!");
                    System.out.println("👉 This job prints simple statements.");
                    System.out.println("result array size: " + result.size());
                    for(InvoiceHeader invoiceHeader: result){
                        System.out.println(invoiceHeader);                   
                    }
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

      // Step definition
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<InvoiceHeader> invoicReader1, ListItemWriter<InvoiceHeader> invoiceWriter) {
        System.out.println(invoiceWriter.getWrittenItems());
        return new StepBuilder("step1", jobRepository)
                .<InvoiceHeader, InvoiceHeader>chunk(100, transactionManager)
                .reader(invoicReader1)
                .writer(invoiceWriter)
                .allowStartIfComplete(true)
                .build();
    }




    @Bean 
    public ListItemReader<String> invoiceReader(){
        return new ListItemReader<>(List.of("thing1", "thing2", "thing2"));
    }


    @Bean 
    public RepositoryItemReader<InvoiceHeader> invoiceReader1(InvoiceHeaderRepository invoiceHeaderRepository){
        return new RepositoryItemReaderBuilder<InvoiceHeader>()
            .name("invoiceReader1")
            .repository(invoiceHeaderRepository)
            .methodName("findAll")
            .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
            .build();
    }
    
    @Bean 
    public ListItemWriter<InvoiceHeader> invoiceWriter(){
        ListItemWriter<InvoiceHeader> listItemWriter = new ListItemWriter<>();
        result = listItemWriter.getWrittenItems();
        return listItemWriter;
    }
}
