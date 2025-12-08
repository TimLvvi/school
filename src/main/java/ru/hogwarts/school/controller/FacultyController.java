package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;


import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("{id}")
    public Faculty findFaculty(@PathVariable long id) {
        return facultyService.findFaculty(id);

    }

    @PutMapping
    public void editFaculty(@RequestBody Faculty faculty) {
        facultyService.editFaculty(faculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteStudent(@PathVariable long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllFaculty() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("/color or name")
    public Collection<Faculty> findByNameOrColorIgnoreCase(@RequestParam(required = false) String name,
                                                           @RequestParam(required = false) String color) {
        if (name != null && !name.isBlank()) {
            return facultyService.findByNameIgnoreCase(name);
        }
        if (color != null && !color.isBlank()) {
            return facultyService.findByColorIgnoreCase(color);
        }
        return null;
    }

    @GetMapping("/students/{id}")
    public Collection<Student> findByFaculty_Id(@PathVariable long id) {
        return facultyService.findByFaculty_Id(id);
    }

    @GetMapping("/max lenght name faculty")
    public String maxLenghtNameFaculty() {
        return facultyService.maxLenghtNameFaculty();
    }

}
