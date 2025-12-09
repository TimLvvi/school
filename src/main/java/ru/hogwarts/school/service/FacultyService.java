package ru.hogwarts.school.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;

import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Collection;
import java.util.Comparator;


@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);

    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Факультет с id " + id + " не найден"
                ));
    }

    public void editFaculty(Faculty faculty) {
        Faculty oldFaculty = facultyRepository.findById(faculty.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Факультет с id " + faculty.getId() + " не найден"
                ));
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        facultyRepository.save(oldFaculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByNameIgnoreCase(String name) {
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Collection<Faculty> findByColorIgnoreCase(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Collection<Student> findByFaculty_Id(long id) {
        return studentRepository.findByFaculty_Id(id);
    }

    public String maxLenghtNameFaculty() {
        return facultyRepository.findAll().stream()
                .map(s -> s.getName())
                .max(Comparator.comparing(s -> s.length()))
                .orElse("");
    }
}
