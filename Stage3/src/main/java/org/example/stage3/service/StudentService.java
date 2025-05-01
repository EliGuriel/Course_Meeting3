package org.example.stage3.service;

import org.example.stage3.dto.BasicCourseDto;
import org.example.stage3.dto.StudentDto;
import org.example.stage3.dto.StudentWithCoursesDto;

import java.util.List;

public interface StudentService {
    
    List<StudentDto> getAllStudentsAsDto();
    
    StudentDto getStudentById(String id);
    
    StudentWithCoursesDto getStudentWithCourses(String id);
    
    StudentDto createStudent(StudentDto studentDto);
    
    StudentDto updateStudent(String id, StudentDto studentDto);
    
    void deleteStudent(String id);
    
    List<StudentDto> getStudentsByLastName(String lastName);
    
    List<StudentDto> getStudentsByYearOfStudy(Integer yearOfStudy);
    
    List<StudentDto> getStudentsWithGpaGreaterThan(Double minGpa);
    
    List<StudentDto> getStudentsWithoutCourses();
    
    List<BasicCourseDto> getCoursesForStudent(String studentId);
}