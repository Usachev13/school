package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(value = "/avatar")
public class AvatarTController {

    private final StudentService service;

    public AvatarTController(StudentService service) {
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<List<Avatar>> getAllAvatars(@RequestParam("page") Integer pageNumber, @RequestParam("size") Integer pageSize){
        List<Avatar> avatar = service.getAllAvatars(pageNumber,pageSize);
        return ResponseEntity.ok(avatar);
    }

}
