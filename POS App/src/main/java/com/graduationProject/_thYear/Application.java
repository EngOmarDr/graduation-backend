package com.graduationProject._thYear;


import org.apache.kafka.clients.NetworkClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		System.out.println(context.getBeansOfType(KafkaAdmin.class));
		System.out.println(context.getBeansOfType(KafkaAdmin.class).size());
		var kafkaAdmin = context.getBeansOfType(KafkaAdmin.class).get("kafkaAdmin");
		System.out.println(kafkaAdmin.getConfigurationProperties());
		kafkaAdmin.createOrModifyTopics(TopicBuilder.name("topic3")
                .partitions(1)
                .replicas(1)
                .build());
	}



	// @Bean
	// public NewTopic topic() {
	// 	return TopicBuilder.name("topic1")
	// 		.partitions(1)
	// 		.replicas(0)
	// 		.build();
	// }

    // @KafkaListener(id = "myId",topics = "topic1")
    // public void listen(String in) {
    //     System.out.println(in);
    // }

	// @Bean
    // public ApplicationRunner runner(KafkaTemplate<String, String> template) {
    //     return args -> {
	// 		template.send("topic1", "this");
    //     };
    // }
}
