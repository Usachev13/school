package ru.hogwarts.school.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }
    @PostMapping
    public ResponseEntity <Faculty> createFaculty(@RequestBody Faculty faculty){
        Faculty newFaculty = service.createFaculty(faculty);
        return ResponseEntity.ok(newFaculty);
    }
    @GetMapping("{id}")
    public ResponseEntity <Faculty> findFaculty(@PathVariable long id){
        Faculty findedFaculty = service.findFaculty(id);
        if (findedFaculty == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findedFaculty);
    }
    @PutMapping
    public ResponseEntity <Faculty> editFaculty(@RequestBody Faculty faculty){
        Faculty editedFaculty = service.editFaculty(faculty);
        if (editedFaculty == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editedFaculty);
    }
    @DeleteMapping("{id}")
    public ResponseEntity <Faculty> deleteFaculty(@PathVariable long id){
        Faculty deletedFaculty = service.deleteFaculty(id);
        if (deletedFaculty == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deletedFaculty);
    }
    @GetMapping("color")
    public ResponseEntity <Collection<Faculty>> findColor(@RequestParam(required = false) String color){
        if (color.isBlank()){
            return ResponseEntity.ok(service.getColor(color));
        }
        return ResponseEntity.ok(Collections.emptyList());

    }


}
