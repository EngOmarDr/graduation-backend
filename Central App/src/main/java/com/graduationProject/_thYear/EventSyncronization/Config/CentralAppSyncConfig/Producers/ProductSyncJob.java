package com.graduationProject._thYear.EventSyncronization.Config.CentralAppSyncConfig.Producers;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.graduationProject._thYear.Invoice.models.InvoiceHeader;

@Configuration
public class ProductSyncJob {
    
    // private List<InvoiceHeader> result;
    
    // @Bean
    // public Job syncProduct(JobRepository jobRepository, Step step1, Step stepTasklet) {
    // return new JobBuilder("SyncDataJob", jobRepository)
    //     .start(step1)
    //     .next(stepTasklet)
    //     .build();
    // }
}
