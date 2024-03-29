package com.pet.project.awss3client.service.impl;

import com.pet.project.awss3client.service.api.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Value("${aws-s3-kafka.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${aws-s3-kafka.kafka.key-serializer}")
    private Class keySerializer;

    @Value("${aws-s3-kafka.kafka.value-serializer}")
    private Class valueSerializer;

    @Value("${aws-s3-kafka.kafka.topic-name}")
    private String topicName;

    @Override
    public void sendToTopic(String json) {
        logger.info("Sending json = '{}' to topic = '{}'", json, topicName);
        getKafkaTemplate().send(topicName, json);
    }

    private KafkaTemplate<String, String> getKafkaTemplate() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }
}
