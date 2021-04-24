package com.example.demo.listner;

import com.example.demo.model.FooObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageListener {

    @KafkaListener(topics = "fooTopicFooObject", groupId = "foo", containerFactory = "fooListenerFooObject")
    void listenerFooObject(FooObject o, Acknowledgment ack) {
        System.out.println(o.getType()+":"+o.getName());
        ack.acknowledge();
    }

    @KafkaListener(topics = "fooTopicListFooObject", groupId = "foo", containerFactory = "fooListenerListFooObject")
    void listenerListFooObject(List<FooObject> data, Acknowledgment ack) {
        data.forEach(o-> System.out.println(o.getType()+":"+o.getName()));
        ack.acknowledge();
    }

}
