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
import com.graduationProject._thYear.EventSyncronization.Records.JournalRecord;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;
import com.graduationProject._thYear.Journal.models.JournalHeader;
import com.graduationProject._thYear.Journal.repositories.JournalHeaderRepository;

@Configuration
@Profile("pos-app")
public class ProducerJournalJob {
     @Autowired
    private SyncJobRepository syncJobRepository;
    
    @Autowired
    private KafkaTemplate<String,JournalRecord> template;

    private List<JournalRecord> result = new ArrayList<>();
    
    @Bean("syncJournalJob")
    public Job syncJournalJob(JobRepository jobRepository, Step getUpsertedJournalsStep, Step getDeletedJournalsStep, Step journalTasklet) {
    return new JobBuilder("syncJournalJob", jobRepository)
        .start(getUpsertedJournalsStep)
        .next(getDeletedJournalsStep)
        .next(journalTasklet)
        .build();
    }



    @Bean
    public Step getUpsertedJournalsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<JournalHeader> journalUpsertReader, ListItemWriter<JournalRecord> journalWriter, ItemProcessor<JournalHeader,JournalRecord> journalUpsertProccessor) {
        return new StepBuilder("getUpsertedJournalsStep", jobRepository)
                .<JournalHeader, JournalRecord>chunk(1, transactionManager)
                .reader(journalUpsertReader)
                .processor(journalUpsertProccessor)
                .writer(journalWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step getDeletedJournalsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<JournalHeader> journalDeleteReader, ListItemWriter<JournalRecord> journalWriter, ItemProcessor<JournalHeader,JournalRecord> journalDeleteProccessor) {
        return new StepBuilder("getDeletedJournalsStep", jobRepository)
                .<JournalHeader, JournalRecord>chunk(1, transactionManager)
                .reader(journalDeleteReader)
                .processor(journalDeleteProccessor)
                .writer(journalWriter)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean
    public Step journalTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("journalTasklet", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Journal Sync Producer Task Is Done Successfuly!");
                    System.out.println("result array size: " + result.size());
                    for(JournalRecord record : result){
                        System.out.println(record.getGlobalId());                   
                        template.send("journal-topic",record);
                    }
                    syncJobRepository.save(
                        SyncJob.builder()
                            .batchSize(result.size())
                            .topic("journal")
                            .status("COMPLETED")
                            .build()    
                    );
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean 
    public RepositoryItemReader<JournalHeader> journalUpsertReader(JournalHeaderRepository journalRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("journal")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        return new RepositoryItemReaderBuilder<JournalHeader>()
            .name("journalUpsertReader")
            .repository(journalRepository)
            .methodName("findAllByUpsertedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }


        @Bean 
    public RepositoryItemReader<JournalHeader> journalDeleteReader(JournalHeaderRepository journalRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("journal")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        dateTime = null;
        return new RepositoryItemReaderBuilder<JournalHeader>()
            .name("journalDeleteReader")
            .repository(journalRepository)
            .methodName("findAllByDeletedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }
    @Bean 
    public ItemProcessor<JournalHeader,JournalRecord> journalUpsertProccessor(){
        return item -> {
            return JournalRecord.fromJournalEntity(item);
        };
    }

     @Bean 
    public ItemProcessor<JournalHeader,JournalRecord> journalDeleteProccessor(){
        return item -> {
            JournalRecord record =  JournalRecord.fromJournalEntity(item);
            // record.setIsDeleted(true);
            return record;
        };
    }


    @Bean 
    public ListItemWriter<JournalRecord> journalWriter(){
        ListItemWriter<JournalRecord> listItemWriter = new ListItemWriter<>();
        result = listItemWriter.getWrittenItems();
        return listItemWriter;
    }   
}
