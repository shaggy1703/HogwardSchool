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
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Science Faculty");
        faculty.setColor("Blue");

        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculty", faculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Science Faculty");
    }

    @Test
    void readFacultyTest() {
        Long id = 1L;

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + id, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Arts Faculty");
        faculty.setColor("Red");

        HttpEntity<Faculty> requestEntity = new HttpEntity<>(faculty);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "/faculty",
                HttpMethod.PUT,
                requestEntity,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Arts Faculty");
    }

    @Test
    void deleteFacultyTest() {
        Long id = 1L;

        ResponseEntity<Void> response = restTemplate.exchange(
                "/faculty/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void findFacultiesByColorTest() {
        String color = "Blue";

        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "/faculty?color=" + color,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void findFacultiesByNameOrColorIgnoreCaseTest() {
        String nameOrColor = "science";

        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "/faculty/by-name-or-color?nameOrColor=" + nameOrColor,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getStudentsByFacultyIdTest() {
        Long id = 1L;

        ResponseEntity<Collection<?>> response = restTemplate.exchange(
                "/faculty/" + id + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<?>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}