package com.graduationProject._thYear.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;


@Configuration
public class KafkaConsumerConfig {

    // @KafkaListener(groupId = "my-group",topics = "topic1")
    // public void listen(KafkaMessage message) {
    //     System.out.println("config consumer:  " + message);
    // }

    @KafkaListener(groupId = "my-group", topics = "topic1")
    public void listen2(SyncMessage<String> message) {
        if(message == null){
            System.out.println("config is null");
        }
        message.printRecords();
        System.out.println("config consumer:  " + message);
    }


}


