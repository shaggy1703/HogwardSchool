package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.Service.StudentService;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createStudentTest() throws Exception {
        Student student = new Student();
        student.setName("John Doe");
        student.setAge(20);

        when(studentService.create(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    void readStudentTest() throws Exception {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setName("John Doe");
        student.setAge(20);

        when(studentService.read(id)).thenReturn(student);

        mockMvc.perform(get("/student/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void updateStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Jane Doe");
        student.setAge(22);

        when(studentService.update(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void deleteStudentTest() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/student/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void findStudentsByAgeTest() throws Exception {
        int age = 20;
        List<Student> students = Collections.singletonList(new Student());

        when(studentService.findByAge(age)).thenReturn(students);

        mockMvc.perform(get("/student?age=" + age))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findStudentsByAgeBetweenTest() throws Exception {
        int min = 18;
        int max = 25;
        List<Student> students = Collections.singletonList(new Student());

        when(studentService.findByAgeBetween(min, max)).thenReturn(students);

        mockMvc.perform(get("/student/by-age-between?min={min}&max={max}", min, max))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getFacultyByStudentIdTest() throws Exception {
        Long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");

        when(studentService.getFacultyByStudentId(id)).thenReturn(faculty);

        mockMvc.perform(get("/student/{id}/faculty", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Faculty"));
    }
}