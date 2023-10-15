package com.javarush.module5.lec19.todolist.dto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@Hidden
@Schema(hidden = true)
@EqualsAndHashCode
public class TaskDto {
    private Long id;
    @NotBlank(message = "description cannot be empty or null")
    private String description;
    @Pattern(regexp = "(IN_PROGRESS|DONE|PAUSED)",
            message = "status must have one of the following values \"IN_PROGRESS\", \"DONE\", \"PAUSED\"")
    private String status;
}
