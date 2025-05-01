package org.example.stage3.repository;

import org.example.stage3.collection.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {

    // Find courses by department
    List<Course> findByDepartment(String department);
    
    // Find courses by name containing
    List<Course> findByNameContainingIgnoreCase(String name);
    
    // Find courses by credits
    List<Course> findByCredits(Integer credits);
    
    // Find courses with credits greater than or equal to
    List<Course> findByCreditsGreaterThanEqual(Integer minCredits);
    
    // Find courses with a specific student enrolled
    @Query("{ 'studentIds': ?0 }")
    List<Course> findByStudentId(String studentId);
    
    // Find courses that start after a certain date
    List<Course> findByStartDateAfter(Date date);
    
    // Find courses that end before a certain date
    List<Course> findByEndDateBefore(Date date);
    
    // Find courses without any students
    @Query("{ $or: [ { 'studentIds': { $exists: false } }, { 'studentIds': { $size: 0 } } ] }")
    List<Course> findCoursesWithoutStudents();
    
    // Count courses by department
    long countByDepartment(String department);
}