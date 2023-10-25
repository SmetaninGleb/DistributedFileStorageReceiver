package ru.innopolis.csn.finalproject.distributedfilestorage.receiver.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.innopolis.csn.finalproject.distributedfilestorage.receiver.services.FileService;

import java.nio.charset.StandardCharsets;

@EnableRabbit
@Component
public class FileListener {

    private final FileService fileService;

    @Value("${config.rabbitmq.filename_header_name}")
    private String filenameHeaderName;

    @Value("${config.rabbitmq.chat_id_header_name}")
    private String chatIdHeaderName;

    public FileListener(FileService fileService) {
        this.fileService = fileService;
    }

    @RabbitListener(queues = "${config.rabbitmq.receive_queue}")
    public void getFileMessage(Message message) {
        MessageProperties properties = message.getMessageProperties();
        String filename = properties.getHeader(filenameHeaderName);
        String chatId = properties.getHeader(chatIdHeaderName);
        byte[] fileBytes = message.getBody();
        fileService.saveFile(fileBytes, filename, chatId);
    }

    @RabbitListener(queues = "${config.rabbitmq.document_list_queue_name}")
    public String getDocumentsList(Message message) {
        String chatId = message.getMessageProperties().getHeader(chatIdHeaderName);
        return fileService.getFileNames(chatId);
    }

    @RabbitListener(queues = "${config.rabbitmq.document_queue_name}")
    public Message getDocument(Message message) {
        String chatId = message.getMessageProperties().getHeader(chatIdHeaderName);
        String filename = new String(message.getBody(), StandardCharsets.UTF_8);
        byte[] fileBytes = fileService.getFileBytes(filename, chatId);
        Message sendMessage = MessageBuilder
                .withBody(fileBytes)
                .setHeader(filenameHeaderName, filename)
                .build();
        return sendMessage;
    }
}
