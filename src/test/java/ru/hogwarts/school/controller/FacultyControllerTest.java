package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.Service.FacultyService;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Science Faculty");
        faculty.setColor("Blue");

        when(facultyService.create(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Science Faculty"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }

    @Test
    void readFacultyTest() throws Exception {
        Long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName("Science Faculty");
        faculty.setColor("Blue");

        when(facultyService.read(id)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Science Faculty"));
    }

    @Test
    void updateFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Arts Faculty");
        faculty.setColor("Red");

        when(facultyService.update(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Arts Faculty"));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/faculty/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void findFacultiesByColorTest() throws Exception {
        String color = "Blue";
        List<Faculty> faculties = Collections.singletonList(new Faculty());

        when(facultyService.findByColor(color)).thenReturn(faculties);

        mockMvc.perform(get("/faculty?color=" + color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findFacultiesByNameOrColorIgnoreCaseTest() throws Exception {
        String nameOrColor = "science";
        List<Faculty> faculties = Collections.singletonList(new Faculty());

        when(facultyService.findByNameOrColorIgnoreCase(nameOrColor)).thenReturn(faculties);

        mockMvc.perform(get("/faculty/by-name-or-color?nameOrColor={nameOrColor}", nameOrColor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getStudentsByFacultyIdTest() throws Exception {
        Long id = 1L;
        List<Student> students = Collections.singletonList(new Student());

        when(facultyService.getStudentsByFacultyId(id)).thenReturn(students);

        mockMvc.perform(get("/faculty/{id}/students", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}