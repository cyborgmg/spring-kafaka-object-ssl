package com.example.demo;

import com.example.demo.config.KafkaProducerConfig;
import com.example.demo.model.FooObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ContextConfiguration(classes = KafkaProducerConfig.class)
class DemoApplicationTests {

	@Autowired
	private KafkaTemplate<String, FooObject> kafkaTemplateFooObject;


	@Autowired
	private KafkaTemplate<String, List<FooObject>> kafkaTemplateListFooObject;


	@Test
	void sendFooObjectListMessage() {
		FooObject p1 = new FooObject();
		p1.setName("name 1");
		p1.setType("Hi 1");

		FooObject p2 = new FooObject();
		p2.setName("name 2");
		p2.setType("Hi 2");

		List<FooObject> list = new ArrayList<>();
		list.add(p1);
		list.add(p2);

		kafkaTemplateListFooObject.send("fooTopicListFooObject",UUID.randomUUID().toString(),list);
	}

	@Test
	void sendFooObjectMessage() {
		FooObject p = new FooObject();
		p.setName("name 1");
		p.setType("Hi 1");

		kafkaTemplateFooObject.send("fooTopicFooObject",UUID.randomUUID().toString(),p);
	}

}
