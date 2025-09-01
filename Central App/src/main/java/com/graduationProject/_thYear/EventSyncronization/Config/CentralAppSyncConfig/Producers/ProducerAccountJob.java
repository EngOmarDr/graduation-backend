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

import com.graduationProject._thYear.Account.models.Account;
import com.graduationProject._thYear.Account.repositories.AccountRepository;
import com.graduationProject._thYear.EventSyncronization.Entities.SyncJob;
import com.graduationProject._thYear.EventSyncronization.Records.AccountRecord;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;

@Configuration
@Profile("central-app")
public class ProducerAccountJob {
    @Autowired
    private SyncJobRepository syncJobRepository;
    
    @Autowired
    private KafkaTemplate<String,AccountRecord> template;

    private List<AccountRecord> result = new ArrayList<>();
    
    @Bean("syncAccountJob")
    public Job syncAccountJob(JobRepository jobRepository, Step getUpsertedAccountsStep, Step getDeletedAccountsStep, Step accountTasklet) {
    return new JobBuilder("syncAccountJob", jobRepository)
        .start(getUpsertedAccountsStep)
        .next(getDeletedAccountsStep)
        .next(accountTasklet)
        .build();
    }



    @Bean
    public Step getUpsertedAccountsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<Account> accountUpsertReader, ListItemWriter<AccountRecord> accountWriter, ItemProcessor<Account,AccountRecord> accountUpsertProccessor) {
        return new StepBuilder("getUpsertedAccountsStep", jobRepository)
                .<Account, AccountRecord>chunk(1, transactionManager)
                .reader(accountUpsertReader)
                .processor(accountUpsertProccessor)
                .writer(accountWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step getDeletedAccountsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<Account> accountDeleteReader, ListItemWriter<AccountRecord> accountWriter, ItemProcessor<Account,AccountRecord> accountDeleteProccessor) {
        return new StepBuilder("getDeletedAccountsStep", jobRepository)
                .<Account, AccountRecord>chunk(1, transactionManager)
                .reader(accountDeleteReader)
                .processor(accountDeleteProccessor)
                .writer(accountWriter)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean
    public Step accountTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("accountTasklet", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Account Sync Producer Task Is Done Successfuly!");
                    System.out.println("result array size: " + result.size());
                    for(AccountRecord record : result){
                        System.out.println(record.getGlobalId());                   
                        template.send("account-topic",record);
                    }
                    syncJobRepository.save(
                        SyncJob.builder()
                            .batchSize(result.size())
                            .topic("account")
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
    public RepositoryItemReader<Account> accountUpsertReader(AccountRepository accountRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("account")
            .map(job -> job.getExecutedAt())
            .orElse(null);
        dateTime = null;
        return new RepositoryItemReaderBuilder<Account>()
            .name("accountUpsertReader")
            .repository(accountRepository)
            .methodName("findAllByUpsertedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }


        @Bean 
    public RepositoryItemReader<Account> accountDeleteReader(AccountRepository accountRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("account")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        System.out.println("account repository" + accountRepository.findAllByUpsertedAtAfter(dateTime, null).getSize());
        return new RepositoryItemReaderBuilder<Account>()
            .name("accountDeleteReader")
            .repository(accountRepository)
            .methodName("findAllByDeletedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }
    @Bean 
    public ItemProcessor<Account,AccountRecord> accountUpsertProccessor(){
        return item -> {
            return AccountRecord.fromAccountEntity(item);
        };
    }

     @Bean 
    public ItemProcessor<Account,AccountRecord> accountDeleteProccessor(){
        return item -> {
            AccountRecord record =  AccountRecord.fromAccountEntity(item);
            record.setIsDeleted(true);
            return record;
        };
    }


    @Bean 
    public ListItemWriter<AccountRecord> accountWriter(){
        ListItemWriter<AccountRecord> listItemWriter = new ListItemWriter<>();
        result = listItemWriter.getWrittenItems();
        return listItemWriter;
    }   
}
