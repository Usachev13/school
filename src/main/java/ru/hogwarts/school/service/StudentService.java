package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
@Service
public class StudentService {
    HashMap<Long, Student> students = new HashMap<>();
    long lastId = 0;

    public Student createStudent(Student student){
        student.setId(++lastId);
        return students.put(lastId, student);
    }
    public Student findStudent(long id){
        return students.get(id);
    }
    public Student editStudent(Student student){
        return students.put(student.getId(), student);
    }
    public Student deleteStudent(long id){
        return students.remove(id);
    }

    public Collection<Student> findAge(int age){
        ArrayList<Student>result = new ArrayList<>();
        for (Student student: students.values()){
            if (student.getAge() == age){
                result.add(student);
            }
        }
        return result;
    }
}

