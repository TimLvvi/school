package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarServise;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;
    private final AvatarServise avatarServise;

    public StudentController(StudentService studentService, AvatarServise avatarServise) {
        this.studentService = studentService;
        this.avatarServise = avatarServise;
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
        return studentService.findByAge(age);
    }

    @GetMapping("/minAge,minAge")
    public Collection<Student> findByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);

    }

    @GetMapping("/faculty/{id}")
    public Faculty findFacultyByStudent_id(@PathVariable long id) {
        return studentService.findFacultyByStudent_id(id);
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("Слишком большой файл");
        }
        avatarServise.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();


    }

    @GetMapping("/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadPreviewAvatar(@PathVariable Long id) {
        Avatar avatar = avatarServise.findStudentAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(avatar.getData());

    }


    @GetMapping("/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarServise.findStudentAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();
        ) {
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("/number_of_all_students")
    public Integer numberAllStudents() {
        return studentService.numberAllStudents();

    }

    @GetMapping("/average_age_of_students")
    public Integer averageAgeStudents() {
        return studentService.averageAgeStudents();
    }


    @GetMapping("/last_five_students")
    public List<Student> lastFiveStudents() {
        return studentService.lastFiveStudents();
    }

    @GetMapping("/name start A")
    public List<String> nameStartA() {
        return studentService.nameStartA();
    }

    @GetMapping("/average age students stream")
    public Double averageAgeStudentsStream() {
        return studentService.averageAgeStudentsStream();
    }

    @GetMapping("/get value formula")
    public Long getValueFormula() {
        return studentService.getValueFormula();
    }
}
