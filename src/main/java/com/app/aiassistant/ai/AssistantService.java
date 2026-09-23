package com.app.aiassistant.ai;

import com.app.aiassistant.dto.*;
import com.app.aiassistant.entity.Task;
import com.app.aiassistant.enums.TaskStatus;
import com.app.aiassistant.repository.TaskRepository;
import com.app.aiassistant.service.ActivityService;
import com.app.aiassistant.service.GoalService;
import com.app.aiassistant.service.TaskService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class AssistantService {
    private final ChatClient chatClient;
    private final SummaryService summaryService;
    private final TaskService taskService;
    private final GoalService goalService;
    private final TaskRepository taskRepository;
    private final ActivityService activityService;

    public AssistantService(
            TaskRepository taskRepository, ChatClient chatClient,
            SummaryService summaryService, TaskService taskService, GoalService goalService, ActivityService activityService) {
        this.taskRepository = taskRepository;
        this.chatClient = chatClient;
        this.summaryService = summaryService;
        this.taskService = taskService;
        this.goalService = goalService;
        this.activityService = activityService;
    }

    private LocalDateTime resolveDueAt(AssistantCommand command) {
        if (command.dueAt() == null
                || command.dueAt().isBlank()
                || command.dueAt().equalsIgnoreCase("null")) {

            if (command.localDate() != null) {
                return command.localDate().atTime(23, 59, 59);
            }

            return null;
        }

        return LocalDateTime.parse(command.dueAt());
    }

    public AssistantCommand interpret(String message) {

        LocalDate today = LocalDate.now();

        return chatClient.prompt()
                .system(system -> system
                        .param("currentDate", today.toString())
                )
                .user(message)
                .call()
                .entity(
                        AssistantCommand.class,
                        spec -> spec
                                .useProviderStructuredOutput()
                                .validateSchema()
                );
    }

    public AssistantResult process(String message) {

        AssistantCommand command = interpret(message);
        LocalDateTime dueAt = resolveDueAt(command);

        switch (command.intent()) {

            case CREATE_TASK:
//                System.out.println("localDate: " + command.localDate());
//                System.out.println("dueAt: " + command.dueAt());

                TaskRequestDTO taskRequest = new TaskRequestDTO();

                taskRequest.setTitle(command.title());
                taskRequest.setDescription(command.description());
                taskRequest.setDurationMinutes(Double.valueOf(command.durationMinutes()));
                taskRequest.setDueAt(dueAt);

                TaskResponseDTO taskResponseDTO =
                        taskService.insert(taskRequest);

                return new AssistantResult(
                        command,
                        generateFinalResponse(taskResponseDTO)
                );
            case CREATE_GOAL:
                GoalRequestDTO goalRequest = new GoalRequestDTO();
                goalRequest.setTitle(command.title());
                goalRequest.setDescription(command.description());
                goalRequest.setPeriod(command.goalPeriod());
                goalRequest.setTargetValue(command.targetValue());
                goalRequest.setTargetUnit(command.targetUnit());

                GoalResponseDTO goalResponseDTO = goalService.insert(goalRequest);
                return new AssistantResult(
                        command,
                        generateFinalResponse(goalResponseDTO)
                );
            case LIST_TASKS:
                List<TaskResponseDTO> tasks = taskService.findAll();
                return new AssistantResult(
                        command, generateFinalResponse(tasks));

            case REGISTER_ACTIVITY:
                ActivityRequestDTO activityRequest = new ActivityRequestDTO();
                activityRequest.setDescription(command.description());
                activityRequest.setDurationMinutes(command.durationMinutes());
                activityRequest.setDate(command.localDate());
                activityRequest.setGoalID(command.goalId());
                activityRequest.setTaskID(command.taskId());

                ActivityResponseDTO activityResponseDTO = activityService.insert(activityRequest);
                return new AssistantResult(
                        command,
                        generateFinalResponse(activityResponseDTO)
                );
            case COMPLETE_TASK:
                if (dueAt == null && command.localDate() != null) {
                    command.localDate()
                            .atTime(23, 59, 59)
                            .format(DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss"));
                }

                List<Task> matchingTasks = taskRepository
                        .findByStatus(
                                TaskStatus.PENDING,
                                TaskStatus.RUNNING
                        )
                        .stream()
                        .filter(task ->
                                matchesText(task.getTitle(), command.title())
                                        || matchesText(task.getDescription(), command.description())
                        )
                        .filter(task ->
                                task.getDueAt()
                                        .toLocalDate()
                                        .equals(command.localDate())
                        )
                        .toList();


                if (matchingTasks.isEmpty()) {

                    return new AssistantResult(
                            command,
                            generateFinalResponse(
                                    "Nenhuma tarefa correspondente foi encontrada."
                            )
                    );
                }

                if (matchingTasks.size() > 1) {

                    return new AssistantResult(
                            command,
                            generateFinalResponse(
                                    matchingTasks
                            )
                    );
                }

                Task task = matchingTasks.getFirst();

                task.completeTask();
                taskRepository.save(task);

                return new AssistantResult(
                        command,
                        generateFinalResponse(task)
                );

            case SHOW_SUMMARY:
                if (command.summaryType() == SummaryType.ACTIVITY) {
                    SummaryData summary = summaryService.generateActivitySummary(command.summaryPeriod());
                    return new  AssistantResult(
                            command,
                            generateFinalResponse(summary)
                    );
                } else if (command.summaryType() == SummaryType.PENDING) {
                    PendingSummary summary = summaryService.generatePendingSummary(command.summaryPeriod());
                    return new  AssistantResult(
                            command,
                            generateFinalResponse(summary)
                    );
                }
            case UNKNOWN:
                return new AssistantResult(
                        command,
                        generateFinalResponse("Não consegui identificar o seu pedido.")
                );

            default:
               return new AssistantResult(
                        command,
                       generateFinalResponse("Ainda não consigo executar essa operação.")
               );
        }
    }

    private boolean matchesText(String taskText, String searchText) {

        if (searchText == null || searchText.isBlank()) {
            return true;
        }

        if (taskText == null) {
            return false;
        }

        return taskText.toLowerCase().contains(
                searchText.toLowerCase()
        );
    }

    private String generateFinalResponse(Object summary) {

        LocalDate today = LocalDate.now();

        return chatClient.prompt()
                .system(system -> system
                        .param("currentDate", today.toString())
                )
                .user("""
                    Gere uma resposta natural e amigável para o usuário
                    utilizando exclusivamente os dados abaixo.

                    Data atual: %s

                    Dados:
                    %s
                    """.formatted(today, summary))
                .call()
                .content();
    }
}
