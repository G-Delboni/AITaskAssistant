package com.app.aiassistant.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistantService {
    private final ChatClient chatClient;

    public AssistantService(ChatClient lumeChatClient) {
        this.chatClient = lumeChatClient;
    }


    public AssistantResult interpret(String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .entity(AssistantResult.class);
    }
}
