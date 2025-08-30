package com.graduationProject._thYear.EventSyncronization.Config.CentralAppSyncConfig.Producers;

import java.time.LocalDateTime;
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
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord;
import com.graduationProject._thYear.EventSyncronization.Records.SyncMessage;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;
import com.graduationProject._thYear.Product.models.Product;
import com.graduationProject._thYear.Product.repositories.ProductRepository;

@Configuration
@Profile("central-app")
public class ProduceProductJob {

    @Autowired
    private SyncJobRepository syncJobRepository;
    
    @Autowired
    private KafkaTemplate<String,SyncMessage<ProductRecord>> template;

    private SyncMessage<ProductRecord> result = new SyncMessage<>();
    
    @Bean
    public Job syncProduct(JobRepository jobRepository, Step getCreatedProductsStep, Step stepTasklet) {
    return new JobBuilder("SyncDataJob", jobRepository)
        .start(getCreatedProductsStep)
        .next(stepTasklet)
        .build();
    }



      @Bean
    public Step getCreatedProductsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        RepositoryItemReader<Product> productReader1, ListItemWriter<ProductRecord> producWriter, ItemProcessor<Product,ProductRecord> productProccessor) {
        return new StepBuilder("getCreatedProductsStep", jobRepository)
                .<Product, ProductRecord>chunk(1, transactionManager)
                .reader(productReader1)
                .processor(productProccessor)
                .writer(producWriter)
                .allowStartIfComplete(true)
                .build();
    }


    @Bean
    public Step stepTasklet(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepTasklet", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("👉 Hello from Spring Batch 5!");
                    System.out.println("👉 This job prints simple statements.");
                    System.out.println("result array size: " + result.getCreatedRecords().size());
                    for(ProductRecord record : result.getCreatedRecords()){
                        System.out.println(record.getGlobalId());                   
                        System.out.println(record.getGroup());                   
                    }
                    template.send("product-topic",result);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .allowStartIfComplete(true)
                .build();
    }


    @Bean 
    public ListItemReader<String> productReader(){
        return new ListItemReader<>(List.of("thing1", "thing2", "thing2"));
    }


    @Bean 
    public RepositoryItemReader<Product> productReader1(ProductRepository productRepository){
        LocalDateTime dateTime = syncJobRepository.findLastByTopic("product")
            .map(job -> job.getExecutedAt())
            .orElse(null);
        // System.out.println(productRepository.findAll().size());

        return new RepositoryItemReaderBuilder<Product>()
            .name("productReader1")
            .repository(productRepository)
            .methodName("findCreatedAfterDateTime")
            .arguments(dateTime)
            .sorts(Collections.singletonMap("createdAt", Sort.Direction.DESC))
            .build();
    
    }

    @Bean 
    public ItemProcessor<Product,ProductRecord> productProccessor(){
        return item -> {
            System.out.println(item.getGroupId().getId());
            return ProductRecord.fromProductEntity(item);
        };
    }

    @Bean 
    public ListItemWriter<ProductRecord> productWriter(){
        ListItemWriter<ProductRecord> listItemWriter = new ListItemWriter<>();
        result.setCreatedRecords(listItemWriter.getWrittenItems()) ;
        return listItemWriter;
    }
}
