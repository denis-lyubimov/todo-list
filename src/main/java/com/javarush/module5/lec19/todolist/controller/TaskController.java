package com.javarush.module5.lec19.todolist.controller;


import com.javarush.module5.lec19.todolist.dto.TaskDto;
import com.javarush.module5.lec19.todolist.exception.NothingToChangeExcpetion;
import com.javarush.module5.lec19.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Controller
@RequestMapping("/todo-list")
@Tag(name = "контроллер для таска", description = "crud для thymeleaf")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "возвращает страницу с тудулистом")
    public String getTodoList(
            @Parameter(description = "номер страницы, начиная с нуля")
            @RequestParam(name = "pageNumber", defaultValue = "0")
            @Min(value = 0, message = "The value must be positive or 0") int pageNumber,
            @Parameter(description = "количество тасков на странице")
            @RequestParam(name = "pageSize", defaultValue = "5")
            @Min(value = 1, message = "The value must be 1 and greater") int pageSize,
            @Parameter(description = "сортировка параметров таска")
            @RequestParam(name = "sortBy", defaultValue = "id")
            @Pattern(regexp = "(id|description|status)", message = "The value must be \"id\", or \"description\", or \"status\"") String sortBy,
            Model model) {
        Page<TaskDto> tasksPage = taskService.getTasksByPage(pageNumber, pageSize, sortBy);
        System.out.println(tasksPage.getContent());
        model.addAttribute("tasks", tasksPage.getContent());
        model.addAttribute("pageNumber", tasksPage.getNumber());
        model.addAttribute("pageCount", tasksPage.getTotalPages());
        return "todo-list";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "удаляет таск по айди")
    public String delteTask(
            @Parameter(description = "айди таска")
            @PathVariable("id")
            @Min(value = 0, message = "The value must be positive or 0")
            @NotNull Long id,
            @Parameter(description = "номер страницы, начиная с нуля")
            @RequestParam(name = "pageNumber", defaultValue = "0")
            @Min(value = 0, message = "The value must be positive or 0") int pageNumber,
            @Parameter(description = "количество тасков на странице")
            @RequestParam(name = "pageSize", defaultValue = "5")
            @Min(value = 1, message = "The value must be 1 and greater") int pageSize,
            @Parameter(description = "сортировка параметров таска")
            @RequestParam(name = "sortBy", defaultValue = "id")
            @Pattern(regexp = "(id|description|status)", message = "The value must be \"id\", or \"description\", or \"status\"") String sortBy,
            Model model) {
        taskService.delteById(id);
        return getTodoList(pageNumber, 5, "id", model);
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "перекидывает в редактирование таска")
    public String showUpdateForm(@Parameter(description = "айди таска")
                                 @PathVariable("id")
                                 @Min(value = 0, message = "The value must be positive or 0")
                                 @NotNull Long id,
                                 @Parameter(description = "номер страницы, начиная с нуля")
                                 @RequestParam(name = "pageNumber", defaultValue = "0")
                                 @Min(value = 0, message = "The value must be positive or 0") int pageNumber,
                                 Model model) {
        TaskDto taskDto = taskService.findById(id);
        model.addAttribute("task", taskDto);
        model.addAttribute("pageNumber", pageNumber);
        return "update-task";
    }

    @PostMapping("/update/{id}")
    @Operation(summary = "ректирует таск")
    public String updateTask(@Parameter(description = "айди таска")
                             @PathVariable("id")
                             @Min(value = 0, message = "The value must be positive or 0")
                             @NotNull Long id,
                             @Parameter(description = "номер страницы, начиная с нуля")
                             @RequestParam(name = "pageNumber", defaultValue = "0")
                             @Min(value = 0, message = "The value must be positive or 0") int pageNumber,
                             @Valid TaskDto taskDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            taskDto.setId(id);
            return showUpdateForm(id, pageNumber, model);
        }
        try {
            taskService.updateById(id, taskDto);
        } catch (NothingToChangeExcpetion e) {
            taskDto.setId(id);
            return showUpdateForm(id, pageNumber, model);
        }
        return getTodoList(pageNumber, 5, "id", model);
    }
}
