package com.graduationProject._thYear.EventSyncronization.Config.PointOfSellSyncConfig.Consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;

import com.graduationProject._thYear.InvoiceType.services.InvoiceTypeService;
import com.graduationProject._thYear.EventSyncronization.Records.InvoiceTypeRecord;

@Configuration
@Profile("pos-app")
public class ConsumerInvoiceTypeJob {
    
    @Autowired
    InvoiceTypeService invoiceTypeService;



    @KafkaListener(topics = "invoiceType-topic", groupId = "my-group")
    public void listenInvoiceType(InvoiceTypeRecord message) {
        if(message == null){
            System.out.println("config is null");
            return ;
        }
        System.out.println("invoiceType consumer:  " + message);
        try{
            invoiceTypeService.saveOrUpdate(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
