package ru.hogwarts.school.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty newFaculty = service.createFaculty(faculty);
        return ResponseEntity.ok(newFaculty);
    }
    @GetMapping
    public List<Faculty> getAll(){
        return service.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> findFaculty(@PathVariable Long id) {
        Faculty findedFaculty = service.findFaculty(id);
        if (findedFaculty == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findedFaculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty editedFaculty = service.editFaculty(faculty);
        if (editedFaculty == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable long id) {
        service.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("color_or_name")
    public ResponseEntity <Collection<Faculty>> findColor(@RequestParam(required = false) String color, @RequestParam(required = false)String name){
        return ResponseEntity.ok(service.getColorOrName(color, name));

    }
    @GetMapping("{id}/students")
    public List<Student> getStudentByFaculty(Long id){
        return service.findFaculty(id).getStudents();
        
    }
}
