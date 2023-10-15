package com.javarush.module5.lec19.todolist.repo;

import com.javarush.module5.lec19.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepo extends JpaRepository<Task, Long> {

}
