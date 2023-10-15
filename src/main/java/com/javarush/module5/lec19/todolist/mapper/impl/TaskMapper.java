package com.javarush.module5.lec19.todolist.mapper.impl;

import com.javarush.module5.lec19.todolist.dto.TaskDto;
import com.javarush.module5.lec19.todolist.entity.Status;
import com.javarush.module5.lec19.todolist.entity.Task;
import com.javarush.module5.lec19.todolist.mapper.EntityDtoMapper;
import com.javarush.module5.lec19.todolist.repo.TaskRepo;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper implements EntityDtoMapper<Task, TaskDto> {

    private final TaskRepo taskRepo;

    @Autowired
    public TaskMapper(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public TaskDto entityToDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .description(task.getDescription())
                .status(task.getStatus().toString())
                .build();
    }

    @Override
    public List<TaskDto> entityListToDtoList(List<Task> tasks) {
        return tasks.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Task dtoToEntity(TaskDto taskDto) {
        return Task.builder()
                .description(taskDto.getDescription())
                .status(Status.valueOf(taskDto.getStatus()))
                .build();
    }

    @Override
    public List<Task> dtoListToEntityList(List<TaskDto> taskDtos) {
        return taskDtos.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }
}
