package ru.hogwarts.school;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.random;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() throws Exception{
        Assertions.assertThat(studentController).isNotNull();
    }
    @Test
    public void testGetStudent() throws Exception {
        Student testStudent = new Student();
        testStudent.setId(22);
        testStudent.setAge(22);
        testStudent.setName("Name");
        Student oneStudent =  this.testRestTemplate.postForObject("http://localhost:" + port + "/student", testStudent, Student.class);
        Assertions.assertThat(oneStudent).isNotNull();

    }
    @Test
    public void testPostStudent() throws Exception{
        Student testStudent = new Student();
        testStudent.setId(22);
        testStudent.setAge(22);
        testStudent.setName("Name");
          Student oneStudent =  this.testRestTemplate.postForObject("http://localhost:" + port + "/student", testStudent, Student.class);
          Assertions.assertThat(oneStudent).isNotNull();

       Student checkStudent = this.testRestTemplate.getForObject("http://localhost:" + port + "/student/" + oneStudent.getId(), Student.class);
       Assertions.assertThat(oneStudent.getName()).isEqualTo(checkStudent.getName());
    }
    @Test
    public void testFindStudent() throws Exception{
        Student testStudent = new Student();
        testStudent.setId(22);
        testStudent.setAge(22);
        testStudent.setName("Name");
        Student oneStudent = this.testRestTemplate.postForObject("http://localhost:" + port + "/student",testStudent, Student.class);
        Student checkStudent = this.testRestTemplate.getForObject("http://localhost:" + port + "/student/" + oneStudent.getId(), Student.class);
        Assertions.assertThat(oneStudent.getId()).isEqualTo(checkStudent.getId());


    }
    @Test
    public void testDeleteStudent() throws Exception{
        Student testStudent = new Student();
        testStudent.setId(22);
        testStudent.setAge(22);
        testStudent.setName("Name");
        Student newStudent = this.testRestTemplate.postForObject("http://localhost:" + port + "/student", testStudent, Student.class);
        this.testRestTemplate.delete("http://localhost:" + port + "/student/" + newStudent.getId());
        ResponseEntity<Student> checkError = this.testRestTemplate.exchange("http://localhost:" + port + "/student/" + newStudent.getId(), HttpMethod.GET, null, Student.class);
        Assertions
                .assertThat(checkError.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    public void testFindFaculty()throws Exception{
        Student[] students = testRestTemplate.getForObject("http://localhost:" + port + "/student", Student[].class);
        Faculty faculty = new Faculty();

        for (Student student:students){
            Long id = student.getId();
            faculty = testRestTemplate.getForObject("http://localhost:" + port + "/student/" + student.getId() + "/faculty", Faculty.class);
            if (faculty.getId() != null) break;
        }

        assertNotNull(faculty.getId());
        assertNotNull(faculty.getColor());
        assertNotNull(faculty.getName());
    }



}

