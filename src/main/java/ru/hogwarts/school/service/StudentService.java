package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    Logger logger = LoggerFactory.getLogger(StudentService.class);


    public StudentService(StudentRepository studentRepository) {

        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        student.setId(null);
        return studentRepository.save(student);

    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student");
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not student with id = " + id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Студент с id " + id + " не найден"
                    );
                });
    }

    public void editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        Student oldStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Студент с id " + student.getId() + " не найден"
                ));
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        studentRepository.save(oldStudent);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for find by age between");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty findFacultyByStudent_id(long id) {
        logger.info("Was invoked method for find faculty by student_id");
        return findStudent(id).getFaculty();
    }


    public Integer numberAllStudents() {
        logger.info("Was invoked method for number all students");
        return studentRepository.numberAllStudents();
    }

    ;

    public Integer averageAgeStudents() {
        logger.info("Was invoked method for average age students");
        return studentRepository.averageAgeStudents();
    }

    public List<Student> lastFiveStudents() {
        logger.info("Was invoked method for last five students");
        return studentRepository.lastFiveStudents();
    }

    public List<String> nameStartA() {
        return studentRepository.findAll().stream()
                .map(s -> s.getName().toUpperCase())
                .filter(s -> s.startsWith("А"))
                .sorted()
                .toList();
    }

    public double averageAgeStudentsStream() {
        return studentRepository.findAll().stream()
                .mapToInt(s -> s.getAge())
                .average()
                .orElse(0);

    }

    public long getValueFormula() {
        return LongStream.rangeClosed(1, 1000000)
                .parallel()
                .sum();
    }

    public void printStudentsParallel() {
        List<Student> allStudents = studentRepository.findAll();

        System.out.println("первый студент: " + allStudents.get(0).getName());
        System.out.println("второй студент: " + allStudents.get(1).getName());


        new Thread(() -> {
            System.out.println("третий студент: " + allStudents.get(2).getName());
            System.out.println("четвертый студент: " + allStudents.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println("пятый студент: " + allStudents.get(4).getName());
            System.out.println("шестой студент: " + allStudents.get(5).getName());
        }).start();

        new Thread(() -> {
            System.out.println("седьмой студент: " + allStudents.get(6).getName());
            System.out.println("восьмой студент: " + allStudents.get(7).getName());
        }).start();
    }

    private synchronized void printStudentName(String studentInfo) {
        System.out.println(studentInfo);
    }

    public void printStudentsSynchronized() {
        List<Student> allStudents = studentRepository.findAll();

        printStudentName("первый студент: " + allStudents.get(0).getName());
        printStudentName("второй студент: " + allStudents.get(1).getName());

        new Thread(() -> {
            printStudentName("третий студент: " + allStudents.get(2).getName());
            printStudentName("четвертый студент: " + allStudents.get(3).getName());
        }).start();

        new Thread(() -> {
            printStudentName("пятый студент: " + allStudents.get(4).getName());
            printStudentName("шестой студент: " + allStudents.get(5).getName());
        }).start();

        new Thread(() -> {
            printStudentName("седьмой студент: " + allStudents.get(6).getName());
            printStudentName("восьмой студент: " + allStudents.get(7).getName());
        }).start();
    }
}
