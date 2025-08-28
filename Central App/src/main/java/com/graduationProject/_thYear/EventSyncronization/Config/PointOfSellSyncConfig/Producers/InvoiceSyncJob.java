package com.graduationProject._thYear.EventSyncronization.Config.PointOfSellSyncConfig.Producers;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.graduationProject._thYear.EventSyncronization.Entities.SyncJob;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;
import com.graduationProject._thYear.Invoice.models.InvoiceHeader;
import com.graduationProject._thYear.Invoice.repositories.InvoiceHeaderRepository;

@Configuration
public class InvoiceSyncJob {


    @Bean
    public ApplicationRunner runner1(InvoiceHeaderRepository repository, SyncJobRepository repository2) {
        return args -> {
            List<InvoiceHeader> records = repository.findAll();
            repository2.save(SyncJob.builder().topic("invoice").build());

            System.out.println(repository2.findLastByTopic("invoice"));
            System.out.println("before for loop");
            for(InvoiceHeader record: records){
                System.out.println(record.getId());
                System.out.println(record.getGlobalId());
            }
            System.out.println("hi there");
        };
    }

    
}
