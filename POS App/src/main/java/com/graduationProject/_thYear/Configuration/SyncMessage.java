package com.graduationProject._thYear.Configuration;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncMessage<T> {
    List<T> createdRecords;

    public void printRecords(){
        for (T record: createdRecords){
            System.out.println(record);
        }
    }
}
