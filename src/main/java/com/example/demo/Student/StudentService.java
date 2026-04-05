package com.example.demo.Student;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> GetStudent() {
        return studentRepository.findAll();
    }

    public Student SaveStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    public Student UpdateStudent(Long id, Student student) {

        Student StudentDB = studentRepository.findById(id).orElse(null);

        assert StudentDB != null;

        if (Objects.nonNull(student.getName()) && !"".equalsIgnoreCase(student.getName())) {
            StudentDB.setName(student.getName());
        }

        if (Objects.nonNull(student.getEmail()) && !"".equalsIgnoreCase(student.getEmail())) {
            StudentDB.setEmail(student.getEmail());
        }

        if (Objects.nonNull(student.getDob())) {
            StudentDB.setDob(student.getDob());
        }

        if (Objects.nonNull(student.getAge())) {
            StudentDB.setAge(student.getAge());
        }

        return studentRepository.save(StudentDB);
    }

    public void DeleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
