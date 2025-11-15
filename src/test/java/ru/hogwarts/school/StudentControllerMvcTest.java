
package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarServise;
import ru.hogwarts.school.service.StudentService;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private AvatarServise avatarServise;

    @Test
    public void createStudentTest() throws Exception {

        Student student = new Student(1L, "test", 100);
        when(studentService.createStudent(any(Student.class))).thenReturn(student);


        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"test\",\"age\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.age").value(100));
    }

    @Test
    void findStudentTest() throws Exception {

        Student student = new Student(1L, "test", 100);
        when(studentService.findStudent(1L)).thenReturn(student);


        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.age").value(100));
    }

    @Test
    void deleteStudentTest() throws Exception {

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }


    @Test
    void getAllStudentsTest() throws Exception {

        Student student1 = new Student(1L, "test1", 100);
        Student student2 = new Student(2L, "test2", 100);
        Collection<Student> students = new HashSet<>();
        students.add(student1);
        students.add(student2);
        when(studentService.getAllStudents()).thenReturn(students);


        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void editStudentTest() throws Exception {

        Student student = new Student(1L, "test", 100);


        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"test\",\"age\":100}"))
                .andExpect(status().isOk());
    }

    @Test
    void findByAgeTest() throws Exception {
        Student student1 = new Student(1L, "test1", 100);
        Student student2 = new Student(1L, "test2", 100);
        Collection<Student> students = new HashSet<>();
        students.add(student1);
        students.add(student2);

        when(studentService.findByAge(100)).thenReturn(students);


        mockMvc.perform(get("/student/age")
                        .param("age", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findByAgeBetweenTest() throws Exception {

        Collection<Student> students = new HashSet<>();
        Student student1 = new Student(1L, "test1", 100);
        Student student2 = new Student(1L, "test2", 120);
        students.add(student1);
        students.add(student2);

        when(studentService.findByAgeBetween(90, 130)).thenReturn(students);


        mockMvc.perform(get("/student/minAge,minAge")
                        .param("minAge", "90")
                        .param("maxAge", "130"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findFacultyByStudent_id_Test() throws Exception {

        Faculty faculty = new Faculty(1L, "test", "красный");
        when(studentService.findFacultyByStudent_id(1L)).thenReturn(faculty);


        mockMvc.perform(get("/student/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));
    }
}





































