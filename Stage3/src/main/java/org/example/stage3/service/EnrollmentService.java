package org.example.stage3.service;

import org.example.stage3.dto.EnrollmentDto;

import java.util.List;

public interface EnrollmentService {
    
    List<EnrollmentDto> getAllEnrollmentsAsDto();
    
    EnrollmentDto getEnrollmentById(String id);
    
    EnrollmentDto getEnrollmentByStudentAndCourse(String studentId, String courseId);
    
    EnrollmentDto enrollStudentInCourse(String studentId, String courseId);
    
    EnrollmentDto updateEnrollment(String id, EnrollmentDto enrollmentDto);
    
    EnrollmentDto updateGrade(String studentId, String courseId, String grade, Double score);
    
    void unenrollStudentFromCourse(String studentId, String courseId);
    
    List<EnrollmentDto> getEnrollmentsForStudent(String studentId);
    
    List<EnrollmentDto> getEnrollmentsForCourse(String courseId);
    
    List<EnrollmentDto> getEnrollmentsByStatus(String status);
}
