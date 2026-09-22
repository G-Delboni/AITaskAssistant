package com.app.aiassistant.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistantService {

    private final ChatClient chatClient;
    private final SummaryService summaryService;

    public AssistantService(
            ChatClient chatClient,
            SummaryService summaryService) {

        this.chatClient = chatClient;
        this.summaryService = summaryService;
    }

    //TODO -> make process actually initialize the classes in the bank.

    public AssistantResult process(String message) {

        AssistantResult result = chatClient.prompt()
                .user(message)
                .call()
                .entity(AssistantResult.class);

        if (result.assistantCommand().intent() == AssistantIntent.SHOW_SUMMARY) {

            Object summary;

            if (result.assistantCommand().summaryType() == SummaryType.ACTIVITY) {

                summary = summaryService.generateActivitySummary(
                        result.assistantCommand().period()
                );

            } else if (result.assistantCommand().summaryType() == SummaryType.PENDING) {

                summary = summaryService.generatePendingSummary(
                        result.assistantCommand().period()
                );

            } else {
                throw new IllegalArgumentException(
                        "Unsupported summary type."
                );
            }

            String response = generateFinalResponse(summary);

            return new AssistantResult(
                    result.assistantCommand(),
                    response
            );
        }

        return result;
    }

    private String generateFinalResponse(Object summary) {

        return chatClient.prompt()
                .user("""
                        Gere uma resposta natural e amigável para o usuário
                        utilizando exclusivamente os dados abaixo:

                        %s
                        """.formatted(summary))
                .call()
                .content();
    }
}
