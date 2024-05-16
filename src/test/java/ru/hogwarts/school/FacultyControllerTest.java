package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;

import static java.lang.Math.random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void contextLoads() throws Exception{
        Assertions.assertThat(facultyController).isNotNull();
    }
    @Test
    public void testGetFaculty() throws Exception{
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/faculty", String.class))
                .isNotNull();
    }

    @Test
    public void testCreateFaculty() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setName("Name");
        faculty.setId(22L);
        faculty.setColor("color");

        Faculty testFaculty = this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Faculty checkFaculty = this.testRestTemplate.getForObject("http://localhost:" + port + "/faculty/" + testFaculty.getId(), Faculty.class);

        Assertions.assertThat(testFaculty.getName()).isEqualTo(checkFaculty.getName());
    }
}
