package ru.hogwarts.school.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity <Student> createStudent(@RequestBody Student student){
        Student createdStudent = service.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }
    @GetMapping("{id}")
    public ResponseEntity <Student> findStudent(@PathVariable long id){
        Student findedStudent = service.findStudent(id);
        if(findedStudent == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findedStudent);
    }
    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student){
        Student updatedStudent = service.editStudent(student);
        if (updatedStudent == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }
    @DeleteMapping("{id}")
    public ResponseEntity <Student> deleteStudent(@PathVariable long id){
        Student deletedStudent = service.deleteStudent(id);
        return ResponseEntity.ok(deletedStudent);
    }
    @GetMapping("color")
    public ResponseEntity<Collection<Student>> getByAge(@RequestParam int age){
        if(age > 0){
            return ResponseEntity.ok(service.findAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());

    }
}
