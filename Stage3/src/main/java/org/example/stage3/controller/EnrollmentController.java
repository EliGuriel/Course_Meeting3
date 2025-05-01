package org.example.stage3.controller;

import jakarta.validation.Valid;
import org.example.stage3.dto.EnrollmentDto;
import org.example.stage3.dto.GradeUpdateRequest;
import org.example.stage3.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentDto>> getEnrollments(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String courseId,
            @RequestParam(required = false) String status) {
        
        // Filter by student ID if specified
        if (studentId != null && !studentId.isEmpty()) {
            return ResponseEntity.ok(enrollmentService.getEnrollmentsForStudent(studentId));
        }
        
        // Filter by course ID if specified
        if (courseId != null && !courseId.isEmpty()) {
            return ResponseEntity.ok(enrollmentService.getEnrollmentsForCourse(courseId));
        }
        
        // Filter by status if specified
        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(enrollmentService.getEnrollmentsByStatus(status));
        }
        
        // No filters - return all enrollments
        return ResponseEntity.ok(enrollmentService.getAllEnrollmentsAsDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDto> getEnrollmentById(@PathVariable String id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<EnrollmentDto> getEnrollmentByStudentAndCourse(
            @PathVariable String studentId,
            @PathVariable String courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentByStudentAndCourse(studentId, courseId));
    }

    @PostMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<EnrollmentDto> enrollStudentInCourse(
            @PathVariable String studentId,
            @PathVariable String courseId) {
        EnrollmentDto enrollmentDto = enrollmentService.enrollStudentInCourse(studentId, courseId);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(enrollmentDto.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(enrollmentDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentDto> updateEnrollment(
            @PathVariable String id,
            @Valid @RequestBody EnrollmentDto enrollmentDto) {
        return ResponseEntity.ok(enrollmentService.updateEnrollment(id, enrollmentDto));
    }

    // Original method kept for backward compatibility
    @PutMapping("/student/{studentId}/course/{courseId}/grade")
    public ResponseEntity<EnrollmentDto> updateGrade(
            @PathVariable String studentId,
            @PathVariable String courseId,
            @RequestParam String grade,
            @RequestParam(required = false) Double score) {
        return ResponseEntity.ok(enrollmentService.updateGrade(studentId, courseId, grade, score));
    }
    
    // New method with better support for special characters in grade values
    @PutMapping("/student/{studentId}/course/{courseId}/update-grade")
    public ResponseEntity<EnrollmentDto> updateGradeWithBody(
            @PathVariable String studentId,
            @PathVariable String courseId,
            @Valid @RequestBody GradeUpdateRequest request) {
        return ResponseEntity.ok(enrollmentService.updateGrade(
                studentId, courseId, request.getGrade(), request.getScore()));
    }

    @DeleteMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Void> unenrollStudentFromCourse(
            @PathVariable String studentId,
            @PathVariable String courseId) {
        enrollmentService.unenrollStudentFromCourse(studentId, courseId);
        return ResponseEntity.noContent().build();
    }

    // Keep the original path-based endpoints for backward compatibility
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsForStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsForStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsForCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsForCourse(courseId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStatus(status));
    }
}