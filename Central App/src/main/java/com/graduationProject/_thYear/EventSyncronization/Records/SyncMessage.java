package com.graduationProject._thYear.EventSyncronization.Records;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyncMessage<T> {
    @Default
    List<T> createdRecords = new ArrayList<>();
    @Default
    List<T> updatedRecords = new ArrayList<>();
    @Default
    List<T> deletedRecords = new ArrayList<>();
    
}
