package guru.springframework.sfg_jms.sender;


import guru.springframework.sfg_jms.model.HelloWorldMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static guru.springframework.sfg_jms.config.JmsConfig.MY_QUEUE;

@Component
@Slf4j
public class HelloSender {

    private final JmsTemplate jmsTemplate;

    public HelloSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
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
}
