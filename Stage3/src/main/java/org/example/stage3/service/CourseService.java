package org.example.stage3.service;

import org.example.stage3.dto.BasicStudentDto;
import org.example.stage3.dto.CourseDto;
import org.example.stage3.dto.CourseWithStudentsDto;

import java.util.List;

public interface CourseService {
    
    List<CourseDto> getAllCoursesAsDto();
    
    CourseDto getCourseById(String id);
    
    CourseWithStudentsDto getCourseWithStudents(String id);
    
    CourseDto createCourse(CourseDto courseDto);
    
    CourseDto updateCourse(String id, CourseDto courseDto);
    
    void deleteCourse(String id);
    
    List<CourseDto> getCoursesByDepartment(String department);
    
    List<CourseDto> getCoursesByCredits(Integer credits);
    
    List<CourseDto> searchCoursesByName(String keyword);
    
    List<CourseDto> getCoursesWithoutStudents();
    
    List<BasicStudentDto> getStudentsForCourse(String courseId);
}
