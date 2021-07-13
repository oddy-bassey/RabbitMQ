package com.revoltcode.RabbitMQ.consumer;

import com.revoltcode.RabbitMQ.model.Person;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

@Service
public class RabbitMQConsumer {

    //@RabbitListener(queues = "Mobile")
    public void getMessage(Person person){
        System.out.println("My name is "+person.getName());
    }

    //receiving message from Headers-Exchange
    @RabbitListener(queues = "Mobile")
    public void getMessage2(byte[] message) throws IOException, ClassNotFoundException {

        ByteArrayInputStream bis = new ByteArrayInputStream(message);
        ObjectInput oi = new ObjectInputStream(bis);
        Person person = (Person)oi.readObject();

        oi.close();
        bis.close();

        System.out.println("My name is "+person.getName());
    }
}
