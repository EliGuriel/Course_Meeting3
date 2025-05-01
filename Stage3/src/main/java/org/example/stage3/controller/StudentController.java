package org.example.stage3.controller;

import jakarta.validation.Valid;
import org.example.stage3.dto.BasicCourseDto;
import org.example.stage3.dto.StudentDto;
import org.example.stage3.dto.StudentWithCoursesDto;
import org.example.stage3.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getStudents(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer yearOfStudy,
            @RequestParam(required = false) Double minGpa) {

        // Filter by last name if specified
        if (lastName != null && !lastName.isEmpty()) {
            return ResponseEntity.ok(studentService.getStudentsByLastName(lastName));
        }

        // Filter by year of study if specified
        if (yearOfStudy != null) {
            return ResponseEntity.ok(studentService.getStudentsByYearOfStudy(yearOfStudy));
        }

        // Filter by minimum GPA if specified
        if (minGpa != null) {
            return ResponseEntity.ok(studentService.getStudentsWithGpaGreaterThan(minGpa));
        }

        // No filters - return all students
        return ResponseEntity.ok(studentService.getAllStudentsAsDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/{id}/with-courses")
    public ResponseEntity<StudentWithCoursesDto> getStudentWithCourses(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentWithCourses(id));
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentDto createdStudent = studentService.createStudent(studentDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdStudent.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable String id, @Valid @RequestBody StudentDto studentDto) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // Keep the original path-based endpoints for backward compatibility
    @GetMapping("/last-name/{lastName}")
    public ResponseEntity<List<StudentDto>> getStudentsByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(studentService.getStudentsByLastName(lastName));
    }

    @GetMapping("/year/{yearOfStudy}")
    public ResponseEntity<List<StudentDto>> getStudentsByYearOfStudy(@PathVariable Integer yearOfStudy) {
        return ResponseEntity.ok(studentService.getStudentsByYearOfStudy(yearOfStudy));
    }

    @GetMapping("/gpa")
    public ResponseEntity<List<StudentDto>> getStudentsWithGpaGreaterThan(@RequestParam Double minGpa) {
        return ResponseEntity.ok(studentService.getStudentsWithGpaGreaterThan(minGpa));
    }

    @GetMapping("/without-courses")
    public ResponseEntity<List<StudentDto>> getStudentsWithoutCourses() {
        return ResponseEntity.ok(studentService.getStudentsWithoutCourses());
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<BasicCourseDto>> getCoursesForStudent(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getCoursesForStudent(id));
    }
}