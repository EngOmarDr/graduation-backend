package com.graduationProject._thYear;


import java.util.Map;

import org.apache.kafka.clients.NetworkClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaResourceFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
	// 	System.out.println("hi there after run");
	// 	JobLauncher jobLauncher = (JobLauncher)context.getBean("jobLauncher");
	// 	try{
	// 	jobLauncher.run((Job) context.getBean("syncData"), new JobParametersBuilder()
	// 		.addLong("timestamp", System.currentTimeMillis())
	// 		.toJobParameters());
	// 	} catch(Exception e){
	// 		System.out.println(e.getMessage());
	// 	}
		
	}
}
