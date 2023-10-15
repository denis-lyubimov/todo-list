package com.javarush.module5.lec19.todolist.service;

import com.javarush.module5.lec19.todolist.dto.TaskDto;
import com.javarush.module5.lec19.todolist.entity.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    Long create(Task task);

    TaskDto findById(Long id);

    List<TaskDto> findAll();

    Page<TaskDto> getTasksByPage(int pageNumber, int pageSize, String sortBy);

    TaskDto updateById(Long id, TaskDto taskDto);

    TaskDto updateDescription(Long id, String description);

    TaskDto updateStatus(Long id, String status);

    void delteById(Long id);

    void delteAll();
}
