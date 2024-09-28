package co.smarttechie.integrationtest;

import co.smarttechie.entity.Message;
import co.smarttechie.kafka.producer.MessageProducer;
import co.smarttechie.model.MessageDTO;
import co.smarttechie.repository.MessageRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class KafkaDemoIntegrationTest {

    @Container
    @ServiceConnection
    public static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");


    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageProducer messageProducer;

    @Test
    public void test_processMessages() throws Exception {
        readMessagesFromFile().forEach(messageDTO -> messageProducer.sendMessage("messages", messageDTO.content()));
        // Wait for the Kafka consumer to process the message
        Thread.sleep(2000);
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).isNotEmpty();
        assertThat(messages.size()).isEqualTo(10);

    }

    private List<MessageDTO> readMessagesFromFile() throws IOException, URISyntaxException {
        String jsonContent = Files.readString(Path.of(ClassLoader.getSystemResource("messages.json").toURI()));
        Gson gson = new Gson();
        return gson.fromJson(jsonContent, new TypeToken<List<MessageDTO>>(){}.getType());
    }
}
