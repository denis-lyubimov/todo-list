package com.javarush.module5.lec19.todolist.service.impl;

import com.javarush.module5.lec19.todolist.dto.TaskDto;
import com.javarush.module5.lec19.todolist.entity.Status;
import com.javarush.module5.lec19.todolist.entity.Task;
import com.javarush.module5.lec19.todolist.exception.NotFoundException;
import com.javarush.module5.lec19.todolist.exception.NothingToChangeExcpetion;
import com.javarush.module5.lec19.todolist.mapper.impl.TaskMapper;
import com.javarush.module5.lec19.todolist.repo.TaskRepo;
import com.javarush.module5.lec19.todolist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private final TaskRepo taskRepo;
    @Autowired
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public Long create(Task task) {
        Task createdTask = taskRepo.save(task);
        return createdTask.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDto findById(Long id) {
        return taskRepo.findById(id)
                .map(taskMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException("Task %s not found".formatted(id.toString())));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDto> getTasksByPage(int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return taskRepo.findAll(pageable).map(taskMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> findAll() {
        return taskMapper.entityListToDtoList(taskRepo.findAll());
    }

    @Override
    @Transactional
    public TaskDto updateById(Long id, TaskDto taskDto) {
        Task foundTask = findEntityById(id);
        Task taskForUpdate = taskMapper.dtoToEntity(taskDto);
        if (taskForUpdate.equalsWithoutId(foundTask)) {
            throw new NothingToChangeExcpetion("Nothing to update");
        }
        taskForUpdate.setId(id);
        return taskMapper.entityToDto(taskRepo.save(taskForUpdate));
    }

    @Override
    @Transactional
    public TaskDto updateDescription(Long id, String description){
        Task foundTask = findEntityById(id);
        if (foundTask.getDescription().equals(description)){
            throw new NothingToChangeExcpetion("Nothing to update");
        }
        foundTask.setDescription(description);
        return taskMapper.entityToDto(taskRepo.save(foundTask));
    }

    @Override
    @Transactional
    public TaskDto updateStatus(Long id, String status){
        Task foundTask = findEntityById(id);
        Status newStatus = Status.valueOf(status);
        if (foundTask.getStatus().equals(newStatus)){
            throw new NothingToChangeExcpetion("Nothing to update");
        }
        foundTask.setStatus(newStatus);
        return taskMapper.entityToDto(taskRepo.save(foundTask));
    }

    @Override
    @Transactional
    public void delteById(Long id) {
        findEntityById(id);
        taskRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void delteAll() {
        taskRepo.deleteAll();
    }

    @Transactional(readOnly = true)
    public Task findEntityById(Long id){
        return taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task %s not found".formatted(id.toString())));
    }
}
