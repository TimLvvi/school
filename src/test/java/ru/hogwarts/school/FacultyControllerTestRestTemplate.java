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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FacultyControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        facultyRepository.deleteAll();
        studentRepository.deleteAll();

    }

    @Test
    public void contextLoadsTest() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void getAllFacultyTest() throws Exception {

        Faculty faculty1 = new Faculty(null, "test1", "синий");
        Faculty faculty2 = new Faculty(null, "test2", "красный");

        restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty1, Faculty.class);
        restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty2, Faculty.class);


        ResponseEntity<Faculty[]> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty", Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    public void CreateFacultyTest() throws Exception {

        Faculty faculty = new Faculty(null, "test1000", "синий");


        ResponseEntity<Faculty> response = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("test1000");
        assertThat(response.getBody().getColor()).isEqualTo("синий");
    }


    @Test
    public void DeleteFacultyTest() throws Exception {
        Faculty faculty = new Faculty(null, "test", "синий");
        Faculty createdFaculty = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long facultyId = createdFaculty.getId();


        restTemplate.delete("http://localhost:" + port + "/faculty/" + facultyId);


        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + facultyId,
                Faculty.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findFacultyTestTest() {


        Faculty faculty = new Faculty(null, "test1000", "синий");
        ResponseEntity<Faculty> createdFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long facultyId = createdFaculty.getBody().getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + facultyId, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(facultyId);
        assertThat(response.getBody().getName()).isEqualTo("test1000");
        assertThat(response.getBody().getColor()).isEqualTo("синий");
    }

    @Test
    public void editFacultyTestTest() throws Exception {

        Faculty  faculty = new Faculty (null, "test1", "синий");
        Faculty  createdFaculty = restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty updatedFaculty  = new Faculty (createdFaculty.getId(), "test2", "синий");


        restTemplate.put("http://localhost:" + port + "/faculty", updatedFaculty);


        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + createdFaculty.getId(),
                Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("test2");
        assertThat(response.getBody().getColor()).isEqualTo("синий");
    }


    @Test
    public void findByNameIgnoreCaseTest() {


        Faculty faculty = new Faculty(null, "test1000", "синий");
        ResponseEntity<Faculty> createdFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);


        ResponseEntity<Faculty[]> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/color or name?name=test1000", Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    public void findByColorIgnoreCaseTest() {


        Faculty faculty = new Faculty(null, "test1000", "синий");
        ResponseEntity<Faculty> createdFaculty = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);


        ResponseEntity<Faculty[]> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/color or name?color=синий", Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    public void findByFaculty_IdTest() {
        Faculty faculty = new Faculty(null, "ff", "синий");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);

        Faculty createdFaculty = facultyResponse.getBody();

        Student student1 = new Student(null, "test1", 100);
        student1.setFaculty(createdFaculty);
        Student student2 = new Student(null, "test2", 100);
        student2.setFaculty(createdFaculty);

        Student createdStudent1 = restTemplate.postForObject("http://localhost:" + port + "/student", student1, Student.class);
        Student createdStudent2 = restTemplate.postForObject("http://localhost:" + port + "/student", student2, Student.class);


        ResponseEntity<Student[]> response = restTemplate.getForEntity("http://localhost:" + port + "/faculty/students/" + createdFaculty.getId(), Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

}
