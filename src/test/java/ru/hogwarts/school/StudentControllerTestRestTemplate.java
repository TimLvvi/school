package ru.hogwarts.school;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
   public void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void getAllStudentTest() throws Exception{
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:"+port +"/student",Student[].class))
                .isNotNull()
                .isNotEmpty();
    }

    @Test
     public void CreateStudentTest() throws Exception {
        Student student = new Student(null, "Тест", 100);
        Assertions.assertThat(this.testRestTemplate.postForObject("http://localhost:"+port +"/student", student, Student.class))
                .isNotNull();
    }


    @Test
    public void DeleteStudentTest() throws Exception{
        Long id=10L;
        testRestTemplate.delete("http://localhost:"+port +"/student/" + id);
        Assertions.assertThat(this.testRestTemplate.getForEntity("http://localhost:"+port +"/student/" + id,Student.class).getStatusCode())
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findStudentTest() {
        long id = 1;
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:"+port +"/student/" + id,Student.class))
                .isNotNull();
    }

    @Test
    public void findByAgeTest() throws Exception{
        int age = 26;
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:"+port +"/student/age?age=" + age,Student[].class))
                .isNotEmpty()
                .isNotNull();
    }

    @Test
    public void findByAgeBetweenTest() throws Exception{
        int minAge = 10;
        int maxAge = 30;
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:"+port +"/student//minAge,minAge?minAge="+minAge+"&maxAge=" + maxAge,Student[].class))
                .isNotEmpty()
                .isNotNull();
    }




    }








































