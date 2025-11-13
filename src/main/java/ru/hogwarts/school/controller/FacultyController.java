package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.Service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @GetMapping("/{id}")
    public Faculty read(@PathVariable Long id) {
        return facultyService.read(id);
    }

    @PutMapping
    public Faculty update(@RequestBody Faculty faculty) {
        return facultyService.update(faculty);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        facultyService.delete(id);
    }

    @GetMapping
    public Collection<Faculty> findByColor(@RequestParam(required = false) String color) {
        if (color != null && !color.trim().isEmpty()) {
            return facultyService.findByColor(color);
        }
        return java.util.Collections.emptyList();
    }

    @GetMapping("/by-name-or-color")
    public Collection<Faculty> findByNameOrColorIgnoreCase(@RequestParam String nameOrColor) {
        return facultyService.findByNameOrColorIgnoreCase(nameOrColor);
    }

    @GetMapping("/{id}/students")
    public Collection<?> getStudentsByFacultyId(@PathVariable Long id) {
        return facultyService.getStudentsByFacultyId(id);
    }
}