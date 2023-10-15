package com.javarush.module5.lec19.todolist.controller;

import com.javarush.module5.lec19.todolist.dto.TaskDto;
import com.javarush.module5.lec19.todolist.mapper.impl.TaskMapper;
import com.javarush.module5.lec19.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Validated
@RestController
@RequestMapping("/rest/tasks")
@Tag(name = "rest контроллер для таска", description = "rest crud для таска")
public class TaskRestController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskRestController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    @Operation(summary = "создает таск")
    public ResponseEntity<List<Map<String, String>>> create(@RequestBody List<@Valid TaskDto> taskDtos) {
        List<Map<String, String>> responseList = new ArrayList<>();
        if (taskDtos.size() > 0) {
            Map<String, String> created = new HashMap<>() {{
                put("result", "tasks created");
            }};
            responseList.add(created);
            for (TaskDto taskDto : taskDtos) {
                Long id = taskService.create(taskMapper.dtoToEntity(taskDto));
                Map<String, String> playerMap = new HashMap<>();
                playerMap.put("description", taskDto.getDescription());
                playerMap.put("status", taskDto.getStatus());
                playerMap.put("id", id.toString());
                responseList.add(playerMap);
            }
        } else {
            Map<String, String> notCreated = new HashMap<>() {{
                put("result", "no tasks created");
            }};
            responseList.add(notCreated);
        }
        return new ResponseEntity<>(responseList, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "возвращает страницу с тасками")
    public ResponseEntity<List<TaskDto>> getTasksByPage(
            @Parameter(description = "номер страницы, начиная с нуля")
            @RequestParam(name = "pageNumber", defaultValue = "0")
            @Min(value = 0, message = "The value must be positive or 0") int pageNumber,
            @Parameter(description = "количество тасков на странице")
            @RequestParam(name = "pageSize", defaultValue = "5")
            @Min(value = 1, message = "The value must be 1 and greater") int pageSize,
            @Parameter(description = "сортировка параметров таска")
            @RequestParam(name = "sortBy", defaultValue = "id")
            @Pattern(regexp = "(id|description|status)", message = "The value must be \"id\", or \"description\", or \"status\"") String sortBy) {
        Page<TaskDto> taskDtosPage = taskService.getTasksByPage(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(taskDtosPage.getContent(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "обновление всего таска по айди")
    public ResponseEntity<TaskDto> updateById(@Parameter(description = "айди таска")
                                              @PathVariable("id")
                                              @Min(value = 0, message = "The value must be positive or 0")
                                              @NotNull Long id,
                                              @RequestBody @Valid TaskDto taskDto) {
        TaskDto result = taskService.updateById(id, taskDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{id}/description")
    @Operation(summary = "обновление статуса и дескрипшена таска по айди")
    public ResponseEntity<TaskDto> updateDescriptionById(
            @Parameter(description = "описание таска")
            @RequestParam(name = "description", required = false)
            @NotBlank(message = "The value must contain text") String description,
            @Parameter(description = "айди таска")
            @PathVariable("id")
            @Min(value = 0, message = "The value must be positive or 0")
            @NotNull Long id) {
        TaskDto result = taskService.updateDescription(id, description);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "обновление статуса и дескрипшена таска по айди")
    public ResponseEntity<TaskDto> updateStatusById(
            @Parameter(description = "статус таска")
            @RequestParam(name = "status", defaultValue = "1")
            @Pattern(regexp = "(IN_PROGRESS|DONE|PAUSED)",
                    message = "status must have one of the following values \"IN_PROGRESS\", \"DONE\", \"PAUSED\"") String status,
            @Parameter(description = "айди таска")
            @PathVariable("id")
            @Min(value = 0, message = "The value must be positive or 0")
            @NotNull Long id) {
        TaskDto result = taskService.updateStatus(id, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "удаляет таск по айди")
    public ResponseEntity<String> deleteById(
            @Parameter(description = "айди таска")
            @PathVariable("id")
            @Min(value = 0, message = "The value must be positive or 0")
            @NotNull Long id) {
        taskService.delteById(id);
        return new ResponseEntity<>("Task %s was deleted".formatted(id), HttpStatus.OK);
    }
}
