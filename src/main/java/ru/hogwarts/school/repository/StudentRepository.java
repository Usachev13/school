package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAgeBetween(int min, int max);

    @Query(value = "SELECT count(*) FROM student", nativeQuery = true)
    int getStudentsBySchool();

    @Query(value = "select avg(age) from student", nativeQuery = true)
    int getAvgStudentsAge();

    @Query(value = "select * from student order by id desc limit 5", nativeQuery = true)
    List<Student> getLastStudents();
}
