package org.example.stage3.repository;

import org.example.stage3.collection.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {

    // Find enrollments by student ID
    List<Enrollment> findByStudentId(String studentId);
    
    // Find enrollments by course ID
    List<Enrollment> findByCourseId(String courseId);
    
    // Find enrollment by student ID and course ID
    Optional<Enrollment> findByStudentIdAndCourseId(String studentId, String courseId);
    
    // Find enrollments by status
    List<Enrollment> findByStatus(String status);
    
    // Find enrollments by grade
    List<Enrollment> findByGrade(String grade);
    
    // Find enrollments with score greater than or equal to
    List<Enrollment> findByScoreGreaterThanEqual(Double minScore);
    
    // Delete enrollment by student ID and course ID
    void deleteByStudentIdAndCourseId(String studentId, String courseId);
    
    // Count enrollments by status
    long countByStatus(String status);
    
    // Check if enrollment exists
    boolean existsByStudentIdAndCourseId(String studentId, String courseId);
    
    // Delete all enrollments for a student
    void deleteByStudentId(String studentId);
    
    // Delete all enrollments for a course
    void deleteByCourseId(String courseId);
}