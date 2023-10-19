package com.javarush.module5.lec19.todolist.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@Controller
@RequestMapping("/")
@Tag(name = "контроллер для логинов")
public class SecurityController {
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
