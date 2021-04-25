package com.example.demo.listner;

import com.example.demo.model.FooObject;
import com.example.demo.utils.ConstUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageListener {

    private static final String TOPIC_LIST_FOO_OBJECT = "${spring.cloud.stream.bindings.topic-list-foo-object.destination}";

    private static final String TOPIC_FOO_OBJECT = "${spring.cloud.stream.bindings.topic-foo-object.destination}";

    @KafkaListener(topics = TOPIC_LIST_FOO_OBJECT, groupId = ConstUtils.GROUP, containerFactory = "fooListenerListFooObject")
    void listenerListFooObject(List<FooObject> data, Acknowledgment ack) {
        data.forEach(o-> System.out.println(o.getType()+":"+o.getName()));
        ack.acknowledge();
    }

    @KafkaListener(topics = TOPIC_FOO_OBJECT, groupId = ConstUtils.GROUP, containerFactory = "fooListenerFooObject")
    void listenerFooObject(FooObject o, Acknowledgment ack) {
        System.out.println(o.getType()+":"+o.getName());
        ack.acknowledge();
    }

}
