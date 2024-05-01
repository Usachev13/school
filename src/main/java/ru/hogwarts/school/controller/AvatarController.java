package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.AvatarService;

import java.io.EOFException;
import java.io.IOException;



@RestController
@RequestMapping("avatar")
public class AvatarController {

    private final AvatarService service;

    public AvatarController(AvatarService service) {this.service = service;}
    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        service.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }
}
