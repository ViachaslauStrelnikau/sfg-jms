package guru.springframework.sfg_jms.listener;

import guru.springframework.sfg_jms.config.JmsConfig;
import guru.springframework.sfg_jms.model.HelloWorldMessage;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.Message;
import org.apache.activemq.artemis.jms.client.ActiveMQObjectMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    public HelloMessageListener(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message) {
//        log.info("Received message {}",helloWorldMessage);

    }

    @JmsListener(destination = JmsConfig.MY_QUEUE_2)
    public void listen2(@Payload HelloWorldMessage helloWorldMessage,
                        @Headers MessageHeaders headers,
                        Message message) throws JMSException {
        log.info("Received message {}", helloWorldMessage);

        HelloWorldMessage messageBack = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello world and Back")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), messageBack);
        log.info("Sent back  message {}", messageBack);

    }
}
