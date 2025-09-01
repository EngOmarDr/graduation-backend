package com.graduationProject._thYear.EventSyncronization.Config.PointOfSellSyncConfig.Producers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.graduationProject._thYear.EventSyncronization.Entities.SyncJob;
import com.graduationProject._thYear.EventSyncronization.Records.InvoiceRecord;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;
import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Invoice.repositories.InvoiceHeaderRepository;

@Configuration
@Profile("pos-app")
public class ProducerInvoiceJob {

   @Autowired
    private SyncJobRepository syncJobRepository;
    
    @Autowired
    private KafkaTemplate<String,InvoiceRecord> template;

    private List<InvoiceRecord> result = new ArrayList<>();
    
    @Bean("syncInvoiceJob")
    public Job syncInvoiceJob(JobRepository jobRepository, Step getUpsertedInvoicesStep, Step getDeletedInvoicesStep, Step invoiceTasklet) {
    return new JobBuilder("syncInvoiceJob", jobRepository)
        .start(getUpsertedInvoicesStep)
        .next(getDeletedInvoicesStep)
        .next(invoiceTasklet)
        .build();
    }



    @Bean
    public Step getUpsertedInvoicesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<InvoiceHeader> invoiceUpsertReader, ListItemWriter<InvoiceRecord> invoiceWriter, ItemProcessor<InvoiceHeader,InvoiceRecord> invoiceUpsertProccessor) {
        return new StepBuilder("getUpsertedInvoicesStep", jobRepository)
                .<InvoiceHeader, InvoiceRecord>chunk(1, transactionManager)
                .reader(invoiceUpsertReader)
                .processor(invoiceUpsertProccessor)
                .writer(invoiceWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step getDeletedInvoicesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<InvoiceHeader> invoiceDeleteReader, ListItemWriter<InvoiceRecord> invoiceWriter, ItemProcessor<InvoiceHeader,InvoiceRecord> invoiceDeleteProccessor) {
        return new StepBuilder("getDeletedInvoicesStep", jobRepository)
                .<InvoiceHeader, InvoiceRecord>chunk(1, transactionManager)
                .reader(invoiceDeleteReader)
                .processor(invoiceDeleteProccessor)
                .writer(invoiceWriter)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean
    public Step invoiceTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("invoiceTasklet", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Invoice Sync Producer Task Is Done Successfuly!");
                    System.out.println("result array size: " + result.size());
                    for(InvoiceRecord record : result){
                        System.out.println(record.getGlobalId());                   
                        template.send("invoice-topic",record);
                    }
                    syncJobRepository.save(
                        SyncJob.builder()
                            .batchSize(result.size())
                            .topic("invoice")
                            .status("COMPLETED")
                            .build()    
                    );
                    result.clear();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean 
    public RepositoryItemReader<InvoiceHeader> invoiceUpsertReader(InvoiceHeaderRepository invoiceRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("invoice")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        dateTime = null;
        return new RepositoryItemReaderBuilder<InvoiceHeader>()
            .name("invoiceUpsertReader")
            .repository(invoiceRepository)
            .methodName("findAllByUpsertedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }


        @Bean 
    public RepositoryItemReader<InvoiceHeader> invoiceDeleteReader(InvoiceHeaderRepository invoiceRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("invoice")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        return new RepositoryItemReaderBuilder<InvoiceHeader>()
            .name("invoiceDeleteReader")
            .repository(invoiceRepository)
            .methodName("findAllByDeletedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }
    @Bean 
    public ItemProcessor<InvoiceHeader,InvoiceRecord> invoiceUpsertProccessor(){
        return item -> {
            return InvoiceRecord.fromInvoiceEntity(item);
        };
    }

     @Bean 
    public ItemProcessor<InvoiceHeader,InvoiceRecord> invoiceDeleteProccessor(){
        return item -> {
            InvoiceRecord record =  InvoiceRecord.fromInvoiceEntity(item);
            // record.setIsDeleted(true);
            return record;
        };
    }


    @Bean 
    public ListItemWriter<InvoiceRecord> invoiceWriter(){
        ListItemWriter<InvoiceRecord> listItemWriter = new ListItemWriter<>();
        result = listItemWriter.getWrittenItems();
        return listItemWriter;
    }   
}
