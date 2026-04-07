package com.example.demo.Student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/api/v1")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student")
    public List<Student> GetStudent() {
        return studentService.GetStudent();
    }

    @PostMapping("/student")
    public Student AddStudent(@RequestBody Student student) {
        return studentService.SaveStudent(student);
    }

    @PutMapping("/student/{id}")
    public Student UpdateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.UpdateStudent(id, student);
    }

    @DeleteMapping("/student/{id}")
    public String DeleteStudent(@PathVariable Long id) {
        studentService.DeleteStudent(id);
        return "Delete Completed";
    }
}