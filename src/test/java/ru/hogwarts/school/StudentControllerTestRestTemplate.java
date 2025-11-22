package ru.hogwarts.school;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StudentControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    public void setUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void getAllStudentsTest() throws Exception {

        Student student1 = new Student(null, "test1", 1);
        Student student2 = new Student(null, "test2", 2);

        restTemplate.postForEntity("http://localhost:" + port + "/student", student1, Student.class);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student2, Student.class);


        ResponseEntity<Student[]> response = restTemplate.getForEntity("http://localhost:" + port + "/student", Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    public void CreateStudentTest() throws Exception {

        Student student = new Student(null, "test1000", 1000);


        ResponseEntity<Student> response = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("test1000");
        assertThat(response.getBody().getAge()).isEqualTo(1000);
    }


    @Test
    public void DeleteStudentTest() throws Exception {
        Student student = new Student(null, "test", 10);
        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/student", student, Student.class);
        Long studentId = createdStudent.getId();


        restTemplate.delete("http://localhost:" + port + "/student/" + studentId);


        ResponseEntity<Student> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + studentId,
                Student.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findStudentTest() {

        Student student = new Student(null, "test1000", 1000);
        ResponseEntity<Student> createdStudent = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        Long studentId = createdStudent.getBody().getId();

        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/student/" + studentId, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(studentId);
        assertThat(response.getBody().getName()).isEqualTo("test1000");
        assertThat(response.getBody().getAge()).isEqualTo(1000);
    }

    @Test
    public void editStudentTest() throws Exception {

        Student student = new Student(null, "test1", 100);
        Student createdStudent = restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);

        Student updatedStudent = new Student(createdStudent.getId(), "test2", 1);


        restTemplate.put("http://localhost:" + port + "/student", updatedStudent);


        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + createdStudent.getId(),
                Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("test2");
        assertThat(response.getBody().getAge()).isEqualTo(1);
    }


    @Test
    public void findByAgeTest() throws Exception {
        int age = 20;
        Student student1 = new Student(null, "test1", age);
        Student student2 = new Student(null, "test2", age);

        restTemplate.postForEntity("http://localhost:" + port + "/student", student1, Student.class);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student2, Student.class);

        ResponseEntity<Student[]> response = restTemplate.getForEntity("http://localhost:" + port + "/student/age?age=" + age, Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    public void findByAgeBetweenTest() throws Exception {

        Student student1 = new Student(null, "test1", 23);
        Student student2 = new Student(null, "test2", 44);
        Student student3 = new Student(null, "test2", 11);

        restTemplate.postForEntity("http://localhost:" + port + "/student", student1, Student.class);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student2, Student.class);
        restTemplate.postForEntity("http://localhost:" + port + "/student", student3, Student.class);

        ResponseEntity<Student[]> response = restTemplate.getForEntity("http://localhost:" + port + "/student/minAge,minAge?minAge=10&maxAge=30", Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }
    @Test

    public void findFacultyByStudentIdTest() throws Exception {

        Faculty faculty = new Faculty(null, "ff", "синий");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty createdFaculty = facultyResponse.getBody();


        Student student = new Student(null, "test", 100);
        student.setFaculty(createdFaculty);

        Student createdStudent = restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);


        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/student/faculty/" + createdStudent.getId(), Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("ff");
        assertThat(response.getBody().getColor()).isEqualTo("синий");

    }
}








































