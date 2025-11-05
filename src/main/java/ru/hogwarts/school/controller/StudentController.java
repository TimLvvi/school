package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("{id}")
    public Student findStudent(@PathVariable long id) {
        return studentService.findStudent(id);
        }

    @PutMapping
    public void editStudent(@RequestBody Student student) {
        studentService.editStudent(student);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable long id) {
         studentService.deleteStudent(id);
         return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudent() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
    @GetMapping("/age")
    public Collection<Student> findByAge(@RequestParam int age) {
        return studentService.findByAge(age);}

    @GetMapping("/minAge,minAge")
    public Collection<Student> findByAgeBetween(@RequestParam int minAge,@RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge,maxAge);

    }

    @GetMapping("/faculty/{id}")
    public Faculty findFacultyByStudent_id(@PathVariable long id) {
        return studentService.findFacultyByStudent_id(id);
    }



}
