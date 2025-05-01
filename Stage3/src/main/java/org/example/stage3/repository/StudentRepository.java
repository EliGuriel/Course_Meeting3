package org.example.stage3.repository;

import org.example.stage3.collection.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    // Find student by email
    Optional<Student> findByEmail(String email);
    
    // Find students by last name
    List<Student> findByLastName(String lastName);
    
    // Find students by year of study
    List<Student> findByYearOfStudy(Integer yearOfStudy);
    
    // Find students with GPA greater than or equal to
    List<Student> findByGpaGreaterThanEqual(Double minGpa);
    
    // Find students enrolled in a specific course
    @Query("{ 'courseIds': ?0 }")
    List<Student> findByCourseId(String courseId);
    
    // Find students without any courses
    @Query("{ $or: [ { 'courseIds': { $exists: false } }, { 'courseIds': { $size: 0 } } ] }")
    List<Student> findStudentsWithoutCourses();
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Count students by year of study
    long countByYearOfStudy(Integer yearOfStudy);
}