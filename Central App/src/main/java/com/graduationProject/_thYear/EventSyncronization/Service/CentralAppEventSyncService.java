package com.graduationProject._thYear.EventSyncronization.Service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.graduationProject._thYear.EventSyncronization.Entities.SyncJob;
import com.graduationProject._thYear.EventSyncronization.Repositories.SyncJobRepository;


@Service
@Profile("central-app")
public class CentralAppEventSyncService implements EventSyncService{
    @Autowired
    @Qualifier("syncWarehouseJob")
    Job syncWarehouseJob;
    @Autowired
    @Qualifier("syncAccountJob")
    Job syncAccountJob;
    @Autowired
    @Qualifier("syncProductJob")
    Job syncProductJob;
    @Autowired
    @Qualifier("syncCurrencyJob")
    Job syncCurrencyJob;
    @Autowired
    @Qualifier("syncInvoiceTypeJob")
    Job syncInvoiceTypeJob;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    SyncJobRepository syncJobRepository;

    @Override
    public void createSyncJob() {
        
        try {
            jobLauncher.run(syncProductJob, new JobParametersBuilder()
                .addLong("createAt", System.currentTimeMillis())
                .toJobParameters());    

        } catch (Exception e){
            e.printStackTrace();
            syncJobRepository.save(SyncJob.builder()
                .topic("product")
                .status("FAILED")
                .build());
        }

        try {
            jobLauncher.run(syncAccountJob, new JobParametersBuilder()
                .addLong("createAt", System.currentTimeMillis())
                .toJobParameters());
        } catch (Exception e){
            e.printStackTrace();
            syncJobRepository.save(
                SyncJob.builder()
                    .topic("account")
                    .status("FAILED")
                    .build()    
            );
        }
        try {
            jobLauncher.run(syncCurrencyJob, new JobParametersBuilder()
                .addLong("createAt", System.currentTimeMillis())
                .toJobParameters());
        }
        catch (Exception e){
            e.printStackTrace();
            syncJobRepository.save(
                SyncJob.builder()
                    .topic("currency")
                    .status("FAILED")
                    .build()    
            );
        }
        try {
            jobLauncher.run(syncWarehouseJob, new JobParametersBuilder()
                .addLong("createAt", System.currentTimeMillis())
                .toJobParameters());
        } catch (Exception e){
            e.printStackTrace();
            syncJobRepository.save(
                SyncJob.builder()
                    .topic("warehouse")
                    .status("FAILED")
                    .build()    
            );
        }
        try {
            jobLauncher.run(syncInvoiceTypeJob, new JobParametersBuilder()
                .addLong("createAt", System.currentTimeMillis())
                .toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
            syncJobRepository.save(
                SyncJob.builder()
                    .topic("invoiceType")
                    .status("FAILED")
                    .build()    
            );                    
        }
    }
    
}
