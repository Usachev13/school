package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.NotFoundObject;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Value("${avatars.dir.path}")
    private String avatarsDir;
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }


    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");

        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id)
                .orElseThrow(()->{
                    logger.error("Failed to get id" + id);
                    return new NotFoundObject();
                });
    }

    public Student editStudent(Student student) {
        logger.debug("Was invoked method for edit student {}" + student);
        Student student1 = studentRepository.save(student);
        logger.debug("The student for {}", student1);
        return student1;
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllByAge(int age) {
        logger.info("Was invoked method for get by age students");
        return studentRepository.findAll()
                .stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
    public List<Student> getAll(){
        logger.info("Was invoked method for get all students");

        return new ArrayList<>(studentRepository.findAll());
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find between by age students");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Avatar findAvatar(Long studentId) {

        logger.info("Was invoked method for find avatar");
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("Failed to get id" + studentId);
                    return new NotFoundObject();
                });
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload avatar");
        Student student = studentRepository.getReferenceById(studentId);
        Path filepath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filepath.getParent());
        Files.deleteIfExists(filepath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filepath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = new Avatar();
        avatar.setStudent(student);
        avatar.setFilePath(filepath.toString());
        avatar.setFileSize((int) avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    private String getExtensions(String fileName) {
        logger.info("Was invoked method for get extensions file name");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public int getStudentsBySchool(){
        logger.info("Was invoked method for get students by school");
        return studentRepository.getStudentsBySchool();
    }
    public int getAvgStudentsAge(){
        logger.info("Was invoked method for get avg age students");
        return studentRepository.getAvgStudentsAge();
    }

    public List<Student> getLastStudents(){
        logger.info("Was invoked method for get last student");
        return new ArrayList<>(studentRepository.getLastStudents());
    }
    public List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize){
        logger.info("Was invoked method get all avatars");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

}
