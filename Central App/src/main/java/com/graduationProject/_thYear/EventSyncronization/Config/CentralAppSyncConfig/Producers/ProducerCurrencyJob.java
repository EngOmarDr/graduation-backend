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

import com.graduationProject._thYear.Currency.models.Currency;
import com.graduationProject._thYear.Currency.repositories.CurrencyRepository;
import com.graduationProject._thYear.EventSyncronization.Entities.SyncJob;
import com.graduationProject._thYear.EventSyncronization.Records.CurrencyRecord;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;


@Configuration
@Profile("central-app")
public class ProducerCurrencyJob {
    @Autowired
    private SyncJobRepository syncJobRepository;
    
    @Autowired
    private KafkaTemplate<String,CurrencyRecord> template;

    private List<CurrencyRecord> result = new ArrayList<>();
    
    @Bean("syncCurrencyJob")
    public Job syncCurrencyJob(JobRepository jobRepository, Step getUpsertedCurrencysStep, Step getDeletedCurrencysStep, Step currencyTasklet) {
    return new JobBuilder("syncCurrencyJob", jobRepository)
        .start(getUpsertedCurrencysStep)
        .next(getDeletedCurrencysStep)
        .next(currencyTasklet)
        .build();
    }



    @Bean
    public Step getUpsertedCurrencysStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<Currency> currencyUpsertReader, ListItemWriter<CurrencyRecord> currencyWriter, ItemProcessor<Currency,CurrencyRecord> currencyUpsertProccessor) {
        return new StepBuilder("getUpsertedCurrencysStep", jobRepository)
                .<Currency, CurrencyRecord>chunk(1, transactionManager)
                .reader(currencyUpsertReader)
                .processor(currencyUpsertProccessor)
                .writer(currencyWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step getDeletedCurrencysStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<Currency> currencyDeleteReader, ListItemWriter<CurrencyRecord> currencyWriter, ItemProcessor<Currency,CurrencyRecord> currencyDeleteProccessor) {
        return new StepBuilder("getDeletedCurrencysStep", jobRepository)
                .<Currency, CurrencyRecord>chunk(1, transactionManager)
                .reader(currencyDeleteReader)
                .processor(currencyDeleteProccessor)
                .writer(currencyWriter)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean
    public Step currencyTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("currencyTasklet", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Currency Sync Producer Task Is Done Successfuly!");
                    System.out.println("result array size: " + result.size());
                    for(CurrencyRecord record : result){
                        System.out.println(record.getGlobalId());                   
                        template.send("currency-topic",record);
                    }
                    syncJobRepository.save(
                        SyncJob.builder()
                            .batchSize(result.size())
                            .topic("currency")
                            .status("COMPLETED")
                            .build()    
                    );
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean 
    public RepositoryItemReader<Currency> currencyUpsertReader(CurrencyRepository currencyRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("currency")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        return new RepositoryItemReaderBuilder<Currency>()
            .name("currencyUpsertReader")
            .repository(currencyRepository)
            .methodName("findAllByUpsertedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }


        @Bean 
    public RepositoryItemReader<Currency> currencyDeleteReader(CurrencyRepository currencyRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("currency")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        return new RepositoryItemReaderBuilder<Currency>()
            .name("currencyDeleteReader")
            .repository(currencyRepository)
            .methodName("findAllByUpsertedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }
    @Bean 
    public ItemProcessor<Currency,CurrencyRecord> currencyUpsertProccessor(){
        return item -> {
            return CurrencyRecord.fromCurrencyEntity(item);
        };
    }

     @Bean 
    public ItemProcessor<Currency,CurrencyRecord> currencyDeleteProccessor(){
        return item -> {
            CurrencyRecord record =  CurrencyRecord.fromCurrencyEntity(item);
            record.setIsDeleted(true);
            return record;
        };
    }


    @Bean 
    public ListItemWriter<CurrencyRecord> currencyWriter(){
        ListItemWriter<CurrencyRecord> listItemWriter = new ListItemWriter<>();
        result = listItemWriter.getWrittenItems();
        return listItemWriter;
    }   
}
