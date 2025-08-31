package com.graduationProject._thYear.EventSyncronization.Config.CentralAppSyncConfig.Producers;

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
import com.graduationProject._thYear.EventSyncronization.Records.InvoiceTypeRecord;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;
import com.graduationProject._thYear.InvoiceType.models.InvoiceType;
import com.graduationProject._thYear.InvoiceType.repositories.InvoiceTypeRepository;

@Configuration
@Profile("central-app")
public class ProducerInvoiceTypeJob {
    @Autowired
    private SyncJobRepository syncJobRepository;
    
    @Autowired
    private KafkaTemplate<String,InvoiceTypeRecord> template;

    private List<InvoiceTypeRecord> result = new ArrayList<>();
    
    @Bean("syncInvoiceTypeJob")
    public Job syncInvoiceTypeJob(JobRepository jobRepository, Step getUpsertedInvoiceTypesStep, Step getDeletedInvoiceTypesStep, Step invoiceTypeTasklet) {
        return new JobBuilder("syncInvoiceTypeJob", jobRepository)
            .start(getUpsertedInvoiceTypesStep)
            .next(getDeletedInvoiceTypesStep)
            .next(invoiceTypeTasklet)
            .build();
    }



    @Bean
    public Step getUpsertedInvoiceTypesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<InvoiceType> invoiceTypeUpsertReader, ListItemWriter<InvoiceTypeRecord> invoiceTypeWriter, ItemProcessor<InvoiceType,InvoiceTypeRecord> invoiceTypeUpsertProccessor) {
        return new StepBuilder("getUpsertedInvoiceTypesStep", jobRepository)
                .<InvoiceType, InvoiceTypeRecord>chunk(1, transactionManager)
                .reader(invoiceTypeUpsertReader)
                .processor(invoiceTypeUpsertProccessor)
                .writer(invoiceTypeWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step getDeletedInvoiceTypesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<InvoiceType> invoiceTypeDeleteReader, ListItemWriter<InvoiceTypeRecord> invoiceTypeWriter, ItemProcessor<InvoiceType,InvoiceTypeRecord> invoiceTypeDeleteProccessor) {
        return new StepBuilder("getDeletedInvoiceTypesStep", jobRepository)
                .<InvoiceType, InvoiceTypeRecord>chunk(1, transactionManager)
                .reader(invoiceTypeDeleteReader)
                .processor(invoiceTypeDeleteProccessor)
                .writer(invoiceTypeWriter)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean
    public Step invoiceTypeTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("invoiceTypeTasklet", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("InvoiceType Sync Producer Task Is Done Successfuly!");
                    System.out.println("result array size: " + result.size());
                    for(InvoiceTypeRecord record : result){
                        System.out.println(record.getGlobalId());                   
                        template.send("invoiceType-topic",record);
                    }
                    syncJobRepository.save(
                        SyncJob.builder()
                            .batchSize(result.size())
                            .topic("invoiceType")
                            .status("COMPLETED")
                            .build()    
                    );
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean 
    public RepositoryItemReader<InvoiceType> invoiceTypeUpsertReader(InvoiceTypeRepository invoiceTypeRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("invoiceType")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        return new RepositoryItemReaderBuilder<InvoiceType>()
            .name("invoiceTypeUpsertReader")
            .repository(invoiceTypeRepository)
            .methodName("findAllByUpsertedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }


        @Bean 
    public RepositoryItemReader<InvoiceType> invoiceTypeDeleteReader(InvoiceTypeRepository invoiceTypeRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("invoiceType")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        return new RepositoryItemReaderBuilder<InvoiceType>()
            .name("invoiceTypeDeleteReader")
            .repository(invoiceTypeRepository)
            .methodName("findAllByDeletedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }
    @Bean 
    public ItemProcessor<InvoiceType,InvoiceTypeRecord> invoiceTypeUpsertProccessor(){
        return item -> {
            return InvoiceTypeRecord.fromInvoiceTypeEntity(item);
        };
    }

     @Bean 
    public ItemProcessor<InvoiceType,InvoiceTypeRecord> invoiceTypeDeleteProccessor(){
        return item -> {
            InvoiceTypeRecord record =  InvoiceTypeRecord.fromInvoiceTypeEntity(item);
            record.setIsDeleted(true);
            return record;
        };
    }


    @Bean 
    public ListItemWriter<InvoiceTypeRecord> invoiceTypeWriter(){
        ListItemWriter<InvoiceTypeRecord> listItemWriter = new ListItemWriter<>();
        result = listItemWriter.getWrittenItems();
        return listItemWriter;
    }      
}
