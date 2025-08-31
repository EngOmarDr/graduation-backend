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
import com.graduationProject._thYear.EventSyncronization.Records.WarehouseRecord;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;
import com.graduationProject._thYear.Warehouse.models.Warehouse;
import com.graduationProject._thYear.Warehouse.repositories.WarehouseRepository;

@Configuration
@Profile("central-app")
public class ProducerWarehouseJob {
    @Autowired
    private SyncJobRepository syncJobRepository;
    
    @Autowired
    private KafkaTemplate<String,WarehouseRecord> template;

    private List<WarehouseRecord> result = new ArrayList<>();
    
    @Bean("syncWarehouseJob")
    public Job syncWarehouseJob(JobRepository jobRepository, Step getUpsertedWarehousesStep, Step getDeletedWarehousesStep, Step warehouseTasklet) {
        return new JobBuilder("syncWarehouseJob", jobRepository)
            .start(getUpsertedWarehousesStep)
            .next(getDeletedWarehousesStep)
            .next(warehouseTasklet)
            .build();
    }



    @Bean
    public Step getUpsertedWarehousesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<Warehouse> warehouseUpsertReader, ListItemWriter<WarehouseRecord> warehouseWriter, ItemProcessor<Warehouse,WarehouseRecord> warehouseUpsertProccessor) {
        return new StepBuilder("getUpsertedWarehousesStep", jobRepository)
                .<Warehouse, WarehouseRecord>chunk(1, transactionManager)
                .reader(warehouseUpsertReader)
                .processor(warehouseUpsertProccessor)
                .writer(warehouseWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step getDeletedWarehousesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<Warehouse> warehouseDeleteReader, ListItemWriter<WarehouseRecord> warehouseWriter, ItemProcessor<Warehouse,WarehouseRecord> warehouseDeleteProccessor) {
        return new StepBuilder("getDeletedWarehousesStep", jobRepository)
                .<Warehouse, WarehouseRecord>chunk(1, transactionManager)
                .reader(warehouseDeleteReader)
                .processor(warehouseDeleteProccessor)
                .writer(warehouseWriter)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean
    public Step warehouseTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("warehouseTasklet", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Warehouse Sync Producer Task Is Done Successfuly!");
                    System.out.println("result array size: " + result.size());
                    for(WarehouseRecord record : result){
                        System.out.println(record.getGlobalId());                   
                        template.send("warehouse-topic",record);
                    }
                    syncJobRepository.save(
                        SyncJob.builder()
                            .batchSize(result.size())
                            .topic("product")
                            .status("COMPLETED")
                            .build()    
                    );
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }



    @Bean 
    public RepositoryItemReader<Warehouse> warehouseUpsertReader(WarehouseRepository warehouseRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("warehouse")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        return new RepositoryItemReaderBuilder<Warehouse>()
            .name("warehouseUpsertReader")
            .repository(warehouseRepository)
            .methodName("findAllByUpsertedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }


        @Bean 
    public RepositoryItemReader<Warehouse> warehouseDeleteReader(WarehouseRepository warehouseRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("warehouse")
            .map(job -> job.getExecutedAt())
            .orElse(null);

        return new RepositoryItemReaderBuilder<Warehouse>()
            .name("warehouseDeleteReader")
            .repository(warehouseRepository)
            .methodName("findAllByDeletedAtAfter")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .build();
    
    }
    @Bean 
    public ItemProcessor<Warehouse,WarehouseRecord> warehouseUpsertProccessor(){
        return item -> {
            return WarehouseRecord.fromWarehouseEntity(item);
        };
    }

     @Bean 
    public ItemProcessor<Warehouse,WarehouseRecord> warehouseDeleteProccessor(){
        return item -> {
            WarehouseRecord record =  WarehouseRecord.fromWarehouseEntity(item);
            record.setIsDeleted(true);
            return record;
        };
    }


    @Bean 
    public ListItemWriter<WarehouseRecord> warehouseWriter(){
        ListItemWriter<WarehouseRecord> listItemWriter = new ListItemWriter<>();
        result = listItemWriter.getWrittenItems();
        return listItemWriter;
    }   
}
