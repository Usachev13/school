package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

import static java.lang.Math.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTemplateTest {
    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoadsForFacultyController()  {
        assertThat(facultyController).isNotNull();
    }
    @Test
    public void testGetFaculty() throws Exception {
        assertThat(this.restTemplate.getForObject(
                        "http://localhost:" + port + "/faculty", String.class))
                .isNotNull();
    }
    @Test
    public void testCreateFaculty()  {
        Faculty faculty = new Faculty(22L,"faculty", "color");

        ResponseEntity<Faculty> newFacultyRs =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty newFacultyRsBody = newFacultyRs.getBody();

        assertThat(newFacultyRsBody.getName()).isEqualTo(faculty.getName());
        assertThat(newFacultyRsBody.getColor()).isEqualTo(faculty.getColor());
    }
    @Test
    public void testGetFacultyById()  {
        Faculty faculty = new Faculty(22L,"faculty", "color");

        ResponseEntity<Faculty> newFacultyRs =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty newFacultyFromRepForTest =
                restTemplate.getForObject("http://localhost:" + port + "/faculty/" + newFacultyRs.getBody().getId(), Faculty.class);

        assertEquals(newFacultyRs.getBody().getId(), newFacultyFromRepForTest.getId());
    }

    @Test
    public void testUpdateFaculty() {
        Faculty faculty = new Faculty(22L,"faculty", "color");

        ResponseEntity<Faculty> newFacultyRs =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty newFacultyRsBody = newFacultyRs.getBody();

        String newName = "faculty test";
        String newColor = "color test";
        newFacultyRsBody.setColor(newColor);
        newFacultyRsBody.setName(newName);

        restTemplate.put("http://localhost:" + port + "/faculty", newFacultyRsBody, Faculty.class);

        Faculty facultyForTest = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + newFacultyRsBody.getId(), Faculty.class);

        assertEquals(newName, facultyForTest.getName());
        assertEquals(newColor, facultyForTest.getColor());
    }
    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty(22L,"faculty", "color");

        ResponseEntity<Faculty> newFacultyRs =
                restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty newFacultyRsBody = newFacultyRs.getBody();

        Long id = newFacultyRsBody.getId();

        restTemplate.delete("http://localhost:" + port + "/faculty/" + id);

        Faculty facultyForTest = restTemplate.getForObject(
                "http://localhost:" + port + "/faculty/" + id, Faculty.class);

        assertNull(facultyForTest.getId());
        assertNull(facultyForTest.getName());
        assertNull(facultyForTest.getColor());
    }

}