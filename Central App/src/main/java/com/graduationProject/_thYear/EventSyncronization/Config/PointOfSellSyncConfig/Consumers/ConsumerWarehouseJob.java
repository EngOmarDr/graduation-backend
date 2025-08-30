package com.graduationProject._thYear.EventSyncronization.Config.PointOfSellSyncConfig.Consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;

import com.graduationProject._thYear.Warehouse.services.WarehouseService;
import com.graduationProject._thYear.EventSyncronization.Records.WarehouseRecord;

@Configuration
@Profile("pos-app")
public class ConsumerWarehouseJob {
     @Autowired
    WarehouseService warehouseService;



    @KafkaListener(topics = "warehouse-topic", groupId = "my-group")
    public void listenAccount(WarehouseRecord message) {
        if(message == null){
            System.out.println("config is null");
            return ;
        }
        System.out.println("account consumer:  " + message);
        try{
            if (message.getIsDeleted()){
                warehouseService.deleteWarehouse(null);
                return;
            }
            warehouseService.saveOrUpdate(message);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
