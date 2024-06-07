package ru.hogwarts.school.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.*;
import java.util.stream.Stream;

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
    public List<Student> getStudentByFaculty(@PathVariable Long id){
        return service.findFaculty(id).getStudents();
        
    }

    @GetMapping("/longest_name_faculty")
    public String getLongestFaculty(){
        List<Faculty> faculties = service.getAll();
        return faculties.stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }
    @GetMapping("/integer")
    public Integer num(){
        return Stream.iterate(1, a -> a +1) .limit(1_000_000) .reduce(0, (a, b) -> a + b );


    }
    @GetMapping("/integer-2")
    public int integer() {
        Stream<Integer> numbers = Stream.iterate(1, a -> a + 1).limit(1_000_000);
        return numbers.parallel().reduce(0, Integer::sum);
    }
}
