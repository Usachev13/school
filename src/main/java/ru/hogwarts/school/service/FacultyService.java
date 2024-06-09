package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.NotFoundObject;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FacultyService {

    Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Failed get id " + id);
                    return new NotFoundObject();
                });
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.debug("Was invoked method for edit faculty {}", faculty);
        Faculty newFaculty = facultyRepository.save(faculty);
        logger.debug("The faculty for {}", faculty);
        return newFaculty;
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getColorOrName(String color, String name) {
        logger.info("Was invoked method for find faculty");
        return facultyRepository.findFacultiesByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public List<Faculty> getAll() {
        logger.info("Was invoked method for find all faculties");
        return new ArrayList<>(facultyRepository.findAll());
    }

    public Integer num() {
        return Stream.iterate(1, a -> a + 1).limit(1_000_000).reduce(0, (a, b) -> a + b);

    }
    public String getLongestFaculty(){
        List<Faculty> faculties = facultyRepository.findAll();
        return faculties.stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }
    public Integer integer() {
        Stream<Integer> numbers = Stream.iterate(1, a -> a + 1).limit(1_000_000);
        return numbers.parallel().reduce(0, Integer::sum);
    }
}
