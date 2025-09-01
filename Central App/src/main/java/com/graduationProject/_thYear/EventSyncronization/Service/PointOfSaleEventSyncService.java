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
@Profile("pos-app")
public class PointOfSaleEventSyncService implements EventSyncService {

    @Autowired
    @Qualifier("syncInvoiceJob")
    Job syncInvoiceJob;

    // @Autowired
    // @Qualifier("syncJournalJob")
    // Job syncJournalJob;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    SyncJobRepository syncJobRepository;

    @Override
    public void createSyncJob() {
        System.out.println(syncInvoiceJob);
         try {
            jobLauncher.run(syncInvoiceJob, new JobParametersBuilder()
                .addLong("createAt", System.currentTimeMillis())
                .toJobParameters());    

        } catch (Exception e){
            // e.printStackTrace();
            System.out.println(e.getCause());
            System.out.println("hi there failure");
            syncJobRepository.save(SyncJob.builder()
                .topic("invoice")
                .status("FAILED")
                .build());
        }

        // try {
        //     jobLauncher.run(syncJournalJob, new JobParametersBuilder()
        //         .addLong("createAt", System.currentTimeMillis())
        //         .toJobParameters());
        // } catch (Exception e){
        //     // e.printStackTrace();
        //     syncJobRepository.save(
        //         SyncJob.builder()
        //             .topic("journal")
        //             .status("FAILED")
        //             .build()    
        //     );
        // }
    }
    
}
