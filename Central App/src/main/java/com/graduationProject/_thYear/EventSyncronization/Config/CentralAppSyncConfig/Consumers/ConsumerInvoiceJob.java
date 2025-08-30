package com.graduationProject._thYear.EventSyncronization.Config.CentralAppSyncConfig.Consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;

import com.graduationProject._thYear.EventSyncronization.Records.InvoiceRecord;
import com.graduationProject._thYear.Invoice.services.InvoiceService;

@Configuration
@Profile("central-app")
public class ConsumerInvoiceJob {


    @Autowired
    InvoiceService invoiceService;



    @KafkaListener(topics = "invoice-topic", groupId = "my-group")
    public void listenAccount(InvoiceRecord message) {
        if(message == null){
            System.out.println("config is null");
            return ;
        }
        System.out.println("invoice consumer:  " + message);
        try{
            invoiceService.saveOrUpdate(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

     
}
