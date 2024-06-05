package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/port")
public class InfoController {

    private final String port;

    public InfoController(@Value("${Server.port}") String port) {
        this.port = port;
    }


    @GetMapping
    public String serverPort(){
        return port;
    }

}
