package com.hiveplace.task_manager.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hiveplace.task_manager.config.LocalDateTimeTypeAdapter;
import com.hiveplace.task_manager.dto.TaskDTO;
import com.hiveplace.task_manager.entity.Task;
import com.hiveplace.task_manager.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;

@Service
public class MessageServiceImpl implements MessageService {

    private final SqsAsyncClient sqsAsyncClient;

    @Value("${tasks.queue.url}")
    private String queueUrl;

    public MessageServiceImpl(SqsAsyncClient sqsAsyncClient){
        this.sqsAsyncClient = sqsAsyncClient;
    }
    @Override
    public void sendMessage(Task task) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(toJson(task))
                .build();
        sqsAsyncClient.sendMessage(sendMessageRequest);
    }

    private String toJson(Task task){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        return gson.toJson(task);
    }
}
