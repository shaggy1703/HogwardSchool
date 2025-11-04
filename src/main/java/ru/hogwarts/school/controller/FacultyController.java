package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.Service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> create(@RequestBody Faculty faculty) {
        Faculty created = facultyService.create(faculty);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> read(@PathVariable Long id) {
        Faculty faculty = facultyService.read(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> update(@RequestBody Faculty faculty) {
        Faculty updated = facultyService.update(faculty);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facultyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findByColor(@RequestParam(required = false) String color) {
        if (color != null && !color.trim().isEmpty()) {
            return ResponseEntity.ok(facultyService.findByColor(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}