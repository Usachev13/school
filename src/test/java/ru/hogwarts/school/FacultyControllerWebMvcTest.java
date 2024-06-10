package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = FacultyController.class)
@AutoConfigureMockMvc
public class FacultyControllerWebMvcTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    FacultyRepository facultyRepository;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    AvatarRepository avatarRepository;
    @SpyBean
    FacultyService facultyService;
    @SpyBean
    StudentService studentService;

    @InjectMocks
    FacultyController facultyController;

    @Test
    void testGetFaculty() throws Exception {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(new Faculty(1L, "faculty", "color")));
        mvc.perform(MockMvcRequestBuilders.get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("faculty"))
                .andExpect(jsonPath("$.color").value("color"));

    }

    @Test
    void testUpdate() throws Exception {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(new Faculty(1L, "faculty", "color")));
        Faculty faculty = new Faculty(1L, "updated_name", "updated_color");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(MockMvcRequestBuilders.put("/faculty?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("updated_name"))
                .andExpect(jsonPath("$.color").value("updated_color"));
    }

    @Test
    void testDelete() throws Exception {
        when(facultyRepository.findById(2L)).thenReturn(Optional.of(new Faculty(1L, "faculty", "color")));
        mvc.perform(MockMvcRequestBuilders.delete("/faculty/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testAdd() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).then(invocationOnMock -> {
            Faculty input = invocationOnMock.getArgument(0, Faculty.class);
            Faculty f = new Faculty();
            f.setId(100L);
            f.setColor(input.getColor());
            f.setName(input.getName());
            return f;
        });

        Faculty faculty = new Faculty(null, "foo", "bar");

        ObjectMapper objectMapper = new ObjectMapper();
        mvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.name").value("foo"))
                .andExpect(jsonPath("$.color").value("bar"));
    }

    @Test
    void testByColorAndName() throws Exception {
        when(facultyRepository.findFacultiesByColorIgnoreCaseOrNameIgnoreCase(anyString(), anyString()))
                .thenReturn(Set.of(
                        new Faculty(0L, "name1", "color1"),
                        new Faculty(1L, "name2", "color2")));

        mvc.perform(MockMvcRequestBuilders.get("/faculty/color_or_name?color=color1&name=name1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("color1"))
                .andExpect(jsonPath("$[0].name").value("name1"))
                .andExpect(jsonPath("$[1].color").value("color2"))
                .andExpect(jsonPath("$[1].name").value("name2"));

    }

    @Test
    void testGetStudents() throws Exception {
        Faculty f = new Faculty(1L, "f1", "c1");
        f.setStudents(List.of(new Student(1L, "s1", 10)));

        when(facultyRepository.findById(1L)).thenReturn(Optional.of(f));

        mvc.perform(MockMvcRequestBuilders.get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("s1"))
                .andExpect(jsonPath("$[0].age").value(10));

    }
}
