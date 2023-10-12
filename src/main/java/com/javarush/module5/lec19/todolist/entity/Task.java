package com.javarush.module5.lec19.todolist.entity;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Hidden
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private Long id;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
}
