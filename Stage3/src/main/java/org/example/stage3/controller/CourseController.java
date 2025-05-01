package org.example.stage3.controller;

import jakarta.validation.Valid;
import org.example.stage3.dto.BasicStudentDto;
import org.example.stage3.dto.CourseDto;
import org.example.stage3.dto.CourseWithStudentsDto;
import org.example.stage3.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getCourses(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer credits,
            @RequestParam(required = false) String name) {

        // Filter by department if specified
        if (department != null && !department.isEmpty()) {
            return ResponseEntity.ok(courseService.getCoursesByDepartment(department));
        }

        // Filter by credits if specified
        if (credits != null) {
            return ResponseEntity.ok(courseService.getCoursesByCredits(credits));
        }

        // Filter by name if specified
        if (name != null && !name.isEmpty()) {
            return ResponseEntity.ok(courseService.searchCoursesByName(name));
        }

        // No filters - return all courses
        return ResponseEntity.ok(courseService.getAllCoursesAsDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/{id}/with-students")
    public ResponseEntity<CourseWithStudentsDto> getCourseWithStudents(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getCourseWithStudents(id));
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto courseDto) {
        CourseDto createdCourse = courseService.createCourse(courseDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCourse.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable String id, @Valid @RequestBody CourseDto courseDto) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // Keep the original path-based endpoints for backward compatibility
    @GetMapping("/department/{department}")
    public ResponseEntity<List<CourseDto>> getCoursesByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(courseService.getCoursesByDepartment(department));
    }

    @GetMapping("/credits/{credits}")
    public ResponseEntity<List<CourseDto>> getCoursesByCredits(@PathVariable Integer credits) {
        return ResponseEntity.ok(courseService.getCoursesByCredits(credits));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchCoursesByName(@RequestParam String name) {
        return ResponseEntity.ok(courseService.searchCoursesByName(name));
    }

    @GetMapping("/without-students")
    public ResponseEntity<List<CourseDto>> getCoursesWithoutStudents() {
        return ResponseEntity.ok(courseService.getCoursesWithoutStudents());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<BasicStudentDto>> getStudentsForCourse(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getStudentsForCourse(id));
    }
}