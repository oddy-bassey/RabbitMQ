package com.revoltcode.RabbitMQ.controller;

import com.revoltcode.RabbitMQ.model.Person;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/test/{name}")
    public String testApi(@PathVariable("name") String name){

        Person person = Person.builder()
                .id(1L)
                .name(name)
                .build();

        //publishing to queue
        rabbitTemplate.convertAndSend("Mobile", person);

        //publishing to Direct Exchange
        rabbitTemplate.convertAndSend("Direct-Exchange", "mobile", person);

        //publishing to Fanout Exchange
        rabbitTemplate.convertAndSend("Fanout-Exchange", "", person);

        //publishing to Topic Exchnage
        rabbitTemplate.convertAndSend("Topic-Exchange", "tv.mobile.ac", person);

        return "Success";
    }

    //publishing to headers exchange
    @GetMapping("/test2/{name}")
    public String testApi2(@PathVariable("name") String name) throws IOException {

        Person person = Person.builder()
                .id(1L)
                .name(name)
                .build();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(person);
        out.flush();
        out.close();

        byte[] byteMessage = bos.toByteArray();
        bos.close();

        Message message = MessageBuilder.withBody(byteMessage)
                .setHeader("item1", "mobile")
                .setHeader("item2", "television").build();

        rabbitTemplate.send("Headers-Exchnage", "", message);

        return "Success";
    }
}
