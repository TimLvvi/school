package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplate {


        @LocalServerPort
        private int port;

        @Autowired
        private FacultyController facultyController;

        @Autowired
        private TestRestTemplate testRestTemplate;

        @Test
        public void contextLoads() throws Exception {
            Assertions.assertThat(facultyController).isNotNull();
        }

        @Test
        public void getAllFacultyTest() throws Exception{
            Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:"+port +"/faculty", Faculty[].class))
                    .isNotNull()
                    .isNotEmpty();
        }

        @Test
        public void CreateFacultyTest() throws Exception {
            Faculty faculty = new Faculty(null, "Тест", "Тестовый");
            Assertions.assertThat(this.testRestTemplate.postForObject("http://localhost:"+port +"/faculty", faculty, Faculty.class))
                    .isNotNull();
        }


        @Test
        public void DeleteFacultyTest() throws Exception{
            Long id=6L;
            testRestTemplate.delete("http://localhost:"+port +"/faculty/" + id);
            Assertions.assertThat(this.testRestTemplate.getForEntity("http://localhost:"+port +"/faculty/" + id,Faculty.class).getStatusCode())
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        public void findFacultyTest() throws Exception{
            long id = 1;
            Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:"+port +"/faculty/" + id,Faculty.class))

                    .isNotNull();
        }

    }
