package com.graduationProject._thYear.EventSyncronization.Config.CentralAppSyncConfig.Consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;

import com.graduationProject._thYear.EventSyncronization.Records.JournalRecord;
import com.graduationProject._thYear.Journal.services.JournalService;

@Configuration
@Profile("central-app")
public class ConsumerJournalJob {


    @Autowired
    JournalService journalService;



    @KafkaListener(topics = "journal-topic", groupId = "my-group")
    public void listenAccount(JournalRecord message) {
        if(message == null){
            System.out.println("config is null");
            return ;
        }
        System.out.println("journal consumer:  " + message);
        try{
            journalService.saveOrUpdate(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

     
}
