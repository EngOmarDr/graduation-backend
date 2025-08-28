package com.graduationProject._thYear.Configuration;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerConfig {

	@Bean
    public ApplicationRunner runner1(KafkaTemplate<String, SyncMessage<String>> template) {
        return args -> {
			template.send("topic1", new SyncMessage<>(List.of("thing1", "thing2")));
        };
    }

    // @Bean
    // public ApplicationRunner runner2(KafkaTemplate<String, KafkaMessage> template) {
    //     return args -> {
	// 		template.send("topic1", new KafkaMessage());
    //     };
    // }
}
