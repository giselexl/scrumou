package br.ifsp.scrumou.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value(value = "${message.topic.private.name}")
    private String privateTopicName;

    @Bean
    public NewTopic privateTopic() {
        return TopicBuilder.name(privateTopicName)
                .partitions(10)
                .replicas(1)
                .compact()
                .build();
    }
}
