package guru.springframework.sfg_jms.sender;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfg_jms.model.HelloWorldMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static guru.springframework.sfg_jms.config.JmsConfig.MY_QUEUE;
import static guru.springframework.sfg_jms.config.JmsConfig.MY_QUEUE_2;

@Component
@Slf4j
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public HelloSender(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }


    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        log.info("Sending message");
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello world")
                .build();
        jmsTemplate.convertAndSend(MY_QUEUE, message);
        log.info("Message sent");
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndWaitMessage() throws JMSException {
        log.info("Sending message");
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello world and ")
                .build();
        Message answer=jmsTemplate.sendAndReceive(MY_QUEUE_2, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type", "guru.springframework.sfg_jms.model.HelloWorldMessage");
                    log.info("Sending hello world message");
                    return helloMessage;
                } catch (JsonProcessingException e) {
                    throw new JMSException(e.getMessage());
                }
            }
        });
        log.info("Message sent&received:{}", answer.getBody(String.class));
    }
}
