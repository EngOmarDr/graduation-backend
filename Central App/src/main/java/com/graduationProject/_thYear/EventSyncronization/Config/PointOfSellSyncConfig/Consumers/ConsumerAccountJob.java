package com.graduationProject._thYear.EventSyncronization.Config.PointOfSellSyncConfig.Consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;

import com.graduationProject._thYear.Account.services.AccountService;
import com.graduationProject._thYear.EventSyncronization.Records.AccountRecord;

@Configuration
@Profile("pos-app")
public class ConsumerAccountJob {

    @Autowired
    AccountService accountService;



    @KafkaListener(topics = "account-topic", groupId = "my-group")
    public void listenAccount(AccountRecord message) {
        if(message == null){
            System.out.println("config is null");
            return ;
        }
        System.out.println("account consumer:  " + message);
        try{
            accountService.saveOrUpdate(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

     
}
