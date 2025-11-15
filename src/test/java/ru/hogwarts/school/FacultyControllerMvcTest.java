
package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import ru.hogwarts.school.service.FacultyService;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Test
    public void createFacultyTest() throws Exception {

        Faculty faculty = new Faculty(1L, "test", "синий");
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);


        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"test\",\"color\":\"синий\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.color").value("синий"));
    }

    @Test
    void findFacultyTest() throws Exception {

        Faculty faculty = new Faculty(1L, "test", "синий");
        when(facultyService.findFaculty(1L)).thenReturn(faculty);


        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.color").value("синий"));
    }

    @Test
    void deleteFacultyTest() throws Exception {

        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());
    }


    @Test
    void getAllFacultyTest() throws Exception {

        Faculty faculty1 = new Faculty(1L, "test1", "синий");
        Faculty faculty2 = new Faculty(1L, "test2", "синий");
        Collection<Faculty> faculties = new HashSet<>();
        faculties.add(faculty1);
        faculties.add(faculty2);
        when(facultyService.getAllFaculties()).thenReturn(faculties);


        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void editFacultyTest() throws Exception {

        Faculty faculty = new Faculty(1L, "test", "синий");


        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"test\",\"color\":\"синий\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void findByFaculty_IdTest() throws Exception {

        Collection<Student> students = new HashSet<>();
        students.add(new Student(1L, "test", 20));
        students.add(new Student(2L, "test2", 30));

        when(facultyService.findByFaculty_Id(1L)).thenReturn(students);


        mockMvc.perform(get("/faculty/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findByNameIgnoreCaseTest() throws Exception {
        Faculty faculty1 = new Faculty(1L, "test1", "синий");
        Faculty faculty2 = new Faculty(2L, "test1", "красный");
        Collection<Faculty> faculties = new HashSet<>();
        faculties.add(faculty1);
        faculties.add(faculty2);

        when(facultyService.findByNameIgnoreCase("test1")).thenReturn(faculties);



        mockMvc.perform(get("/faculty//color or name")
                        .param("name", "test1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    void findByColorIgnoreCaseTest() throws Exception {
        Faculty faculty1 = new Faculty(1L, "test1", "синий");
        Faculty faculty2 = new Faculty(2L, "test2", "синий");
        Collection<Faculty> faculties = new HashSet<>();
        faculties.add(faculty1);
        faculties.add(faculty2);

        when(facultyService.findByColorIgnoreCase("синий")).thenReturn(faculties);



        mockMvc.perform(get("/faculty//color or name")
                        .param("color", "синий"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}