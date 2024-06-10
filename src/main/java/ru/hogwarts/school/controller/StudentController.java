package ru.hogwarts.school.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService service;
    private final StudentRepository repository;

    public StudentController(StudentService service, StudentRepository repository) {
        this.service = service;
        this.repository = repository;
    }


    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = service.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping
    public List<Student> getAll() {
        return service.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable long id) {
        Student findedStudent = service.findStudent(id);
        if (findedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findedStudent);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student updatedStudent = service.editStudent(student);
        if (updatedStudent == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id) {
        service.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("age")
    public ResponseEntity<Collection<Student>> getAllByAge(@RequestParam int age) {
        if (age > 0) {
            return ResponseEntity.ok(service.getAllByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());

    }

    @GetMapping("betweenAge")
    public ResponseEntity<Collection<Student>> findStudentByBetweenAge(@RequestParam int min, @RequestParam int max) {
        return ResponseEntity.ok(service.findByAgeBetween(min, max));

    }

    @GetMapping("{id}/faculty")
    public Faculty findFaculty(@PathVariable Long id) {
        return service.findStudent(id).getFaculty();
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        service.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = service.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = service.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength(avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping(value = "/count-students-by-school")
    public int getStudentsBySchool() {
        return service.getStudentsBySchool();
    }

    @GetMapping(value = "/avg-students-age")
    public int getAvgStudentsAge() {
        return service.getAvgStudentsAge();
    }

    @GetMapping(value = "/get-last-students")
    public List<Student> getLastStudents() {
        return service.getLastStudents();
    }

    @GetMapping("/names-starting-with-a")
    public ResponseEntity<List<String>> getStudentsNamesStartingWithA() {
        List<Student> students = repository.findAll();

        List<String> nameStartingWithA = students.stream()
                .filter(student -> student.getName().toUpperCase().startsWith("А"))
                .map(Student::getName)
                .sorted()
                .collect(Collectors.toList());

        return ResponseEntity.ok(nameStartingWithA);


    }

    @GetMapping("/avgAgeStudents")
    public double getAvgAgeStudents() {
        return service.getAvgAgeStudents();

    }
    @GetMapping("/print-parallel")
    public void printParallel(){
        service.printParallel();;
    }
    @GetMapping("/print-sync")
    public void printSync(){
        service.printSynchronize();
    }
}

