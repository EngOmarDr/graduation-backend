package com.graduationProject._thYear.EventSyncronization.Service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("pos-app")
public class PointOfSaleEventSyncService implements EventSyncService {

    @Override
    public void createSyncJob() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSyncJob'");
    }
    
}
