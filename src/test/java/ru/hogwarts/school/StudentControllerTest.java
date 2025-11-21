package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createStudentTest() {
        Student student = new Student();
        student.setName("John Doe");
        student.setAge(20);

        ResponseEntity<Student> response = restTemplate.postForEntity("/student", student, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
        assertThat(response.getBody().getAge()).isEqualTo(20);
    }

    @Test
    void readStudentTest() {
        Long id = 1L;

        ResponseEntity<Student> response = restTemplate.getForEntity("/student/" + id, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateStudentTest() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Jane Doe");
        student.setAge(22);

        HttpEntity<Student> requestEntity = new HttpEntity<>(student);

        ResponseEntity<Student> response = restTemplate.exchange(
                "/student",
                HttpMethod.PUT,
                requestEntity,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Jane Doe");
    }

    @Test
    void deleteStudentTest() {
        Long id = 1L;

        ResponseEntity<Void> response = restTemplate.exchange(
                "/student/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void findStudentsByAgeTest() {
        int age = 20;

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "/student?age=" + age,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void findStudentsByAgeBetweenTest() {
        int min = 18;
        int max = 25;

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "/student/by-age-between?min=" + min + "&max=" + max,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getFacultyByStudentIdTest() {
        Long id = 1L;

        ResponseEntity<Object> response = restTemplate.getForEntity("/student/" + id + "/faculty", Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}