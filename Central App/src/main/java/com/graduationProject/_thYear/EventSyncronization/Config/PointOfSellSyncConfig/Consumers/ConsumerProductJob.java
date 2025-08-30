package com.graduationProject._thYear.EventSyncronization.Config.PointOfSellSyncConfig.Consumers;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;

import com.graduationProject._thYear.EventSyncronization.Records.ProductRecord;
import com.graduationProject._thYear.EventSyncronization.Records.SyncMessage;

@Configuration
@Profile("pos-app")
public class ConsumerProductJob {


    

    @KafkaListener(topics = "product-topic", groupId = "my-group")
    public void listen2(SyncMessage<ProductRecord> message) {
        if(message == null){
            System.out.println("config is null");
            return ;
        }
        System.out.println("config consumer:  " + message);

    }

     
}
