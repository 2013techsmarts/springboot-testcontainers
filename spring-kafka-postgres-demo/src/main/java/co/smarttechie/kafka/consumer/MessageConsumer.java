package co.smarttechie.kafka.consumer;

import co.smarttechie.entity.Message;
import co.smarttechie.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumer {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "messages", groupId = "message_group")
    public void consume(String message) {
        log.info("******The message is consumed******" + message);
        Message msg = new Message();
        msg.setContent(message);
        messageRepository.save(msg);
    }
}
