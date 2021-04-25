package com.example.demo.config;


import com.example.demo.model.FooObject;
import com.example.demo.utils.ConstUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

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

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        props.put(ConsumerConfig.GROUP_ID_CONFIG, ConstUtils.GROUP);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100"); // TODO ACERTAR PARA COLOCAR A QUANTIDADE NA // CONFIGURAÇÃO

        // SSL encryption
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, kafkaKeystoreDirectory);
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaKeystorePassword);
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaTruststoreDirectory);
        props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, kafkaKeystorePassword);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaTruststorePassword);

        return props;
    }


    public ConsumerFactory<String, FooObject> consumerFactoryFooObject() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(),new JsonDeserializer<>(FooObject.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FooObject> fooListenerFooObject() {
        ConcurrentKafkaListenerContainerFactory<String, FooObject> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryFooObject());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    public ConsumerFactory<String, List<FooObject>> consumerFactoryListFooObject(){
        ObjectMapper om = new ObjectMapper();
        JavaType type = om.getTypeFactory().constructParametricType(List.class, FooObject.class);
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(), new JsonDeserializer<List<FooObject>>(type, om, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, List<FooObject>> fooListenerListFooObject(){
        ConcurrentKafkaListenerContainerFactory<String, List<FooObject>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryListFooObject());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

}
