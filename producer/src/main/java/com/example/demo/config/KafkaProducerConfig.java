package com.example.demo.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Value("${spring.cloud.stream.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.cloud.stream.kafka-truststore-directory}")
    private String kafkaTruststoreDirectory;

    @Value("${spring.cloud.stream.kafka-truststore-password}")
    private String kafkaTruststorePassword;

    @Value("${spring.cloud.stream.kafka-keystore-directory}")
    private String kafkaKeystoreDirectory;

    @Value("${spring.cloud.stream.kafka-keystore-password}")
    private String kafkaKeystorePassword;

    public Map<String, Object> producerConfigs() {

        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        // Create a safe Producer
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        // High throughput producer (at the expense of a bit of latency and CPU usage)
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        props.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024)); // 32 KB batch size

        // SSL encryption
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, kafkaKeystoreDirectory);
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaKeystorePassword);
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaTruststoreDirectory);
        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, kafkaKeystorePassword);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaTruststorePassword);


        return props;
    }

    @Bean
    public ProducerFactory<String, ?> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ?> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
