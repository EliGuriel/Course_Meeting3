package org.example.stage3.service;

import org.example.stage3.collection.Course;
import org.example.stage3.collection.Student;
import org.example.stage3.dto.BasicStudentDto;
import org.example.stage3.dto.CourseDto;
import org.example.stage3.dto.CourseWithStudentsDto;
import org.example.stage3.exception.ResourceAlreadyExistsException;
import org.example.stage3.exception.ResourceNotFoundException;
import org.example.stage3.mapper.CourseMapper;
import org.example.stage3.mapper.StudentMapper;
import org.example.stage3.repository.CourseRepository;
import org.example.stage3.repository.EnrollmentRepository;
import org.example.stage3.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                            StudentRepository studentRepository,
                            EnrollmentRepository enrollmentRepository,
                            CourseMapper courseMapper,
                            StudentMapper studentMapper) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseMapper = courseMapper;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<CourseDto> getAllCoursesAsDto() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseById(String id) {
        Course course = findCourseById(id);
        return courseMapper.toDto(course);
    }

    @Override
    public CourseWithStudentsDto getCourseWithStudents(String id) {
        Course course = findCourseById(id);
        
        List<Student> students = new ArrayList<>();
        if (course.getStudentIds() != null && !course.getStudentIds().isEmpty()) {
            students = studentRepository.findAllById(course.getStudentIds());
        }
        
        return courseMapper.toCourseWithStudentsDto(course, students);
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        // Check if name already exists
        List<Course> existingCourses = courseRepository.findByNameContainingIgnoreCase(courseDto.getName());
        if (!existingCourses.isEmpty()) {
            for (Course course : existingCourses) {
                if (course.getName().equalsIgnoreCase(courseDto.getName()) && 
                    course.getDepartment().equals(courseDto.getDepartment())) {
                    throw new ResourceAlreadyExistsException("Course with name '" + courseDto.getName() + 
                                                            "' already exists in department '" + courseDto.getDepartment() + "'");
                }
            }
        }
        
        Course course = courseMapper.toEntity(courseDto);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }

    @Override
    public CourseDto updateCourse(String id, CourseDto courseDto) {
        Course existingCourse = findCourseById(id);
        
        // Check if name already exists for another course
        List<Course> existingCourses = courseRepository.findByNameContainingIgnoreCase(courseDto.getName());
        for (Course course : existingCourses) {
            if (!course.getId().equals(id) && course.getName().equalsIgnoreCase(courseDto.getName()) && 
                course.getDepartment().equals(courseDto.getDepartment())) {
                throw new ResourceAlreadyExistsException("Course with name '" + courseDto.getName() + 
                                                        "' already exists in department '" + courseDto.getDepartment() + "'");
            }
        }
        
        courseMapper.updateCourseFromDto(existingCourse, courseDto);
        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toDto(updatedCourse);
    }

    @Override
    public void deleteCourse(String id) {
        Course course = findCourseById(id);
        
        // Delete all enrollments for this course
        enrollmentRepository.deleteByCourseId(course.getId());
        
        // Remove course from students' courses
        if (course.getStudentIds() != null && !course.getStudentIds().isEmpty()) {
            for (String studentId : course.getStudentIds()) {
                Student student = studentRepository.findById(studentId)
                        .orElse(null);
                
                if (student != null && student.getCourseIds() != null) {
                    student.getCourseIds().remove(course.getId());
                    studentRepository.save(student);
                }
            }
        }
        
        // Delete the course
        courseRepository.delete(course);
    }

    @Override
    public List<CourseDto> getCoursesByDepartment(String department) {
        List<Course> courses = courseRepository.findByDepartment(department);
        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getCoursesByCredits(Integer credits) {
        List<Course> courses = courseRepository.findByCredits(credits);
        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> searchCoursesByName(String keyword) {
        List<Course> courses = courseRepository.findByNameContainingIgnoreCase(keyword);
        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getCoursesWithoutStudents() {
        List<Course> courses = courseRepository.findCoursesWithoutStudents();
        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BasicStudentDto> getStudentsForCourse(String courseId) {
        Course course = findCourseById(courseId);
        
        List<Student> students = new ArrayList<>();
        if (course.getStudentIds() != null && !course.getStudentIds().isEmpty()) {
            students = studentRepository.findAllById(course.getStudentIds());
        }
        
        return students.stream()
                .map(studentMapper::toBasicDto)
                .collect(Collectors.toList());
    }
    
    // Helper method to find a course by ID or throw an exception
    private Course findCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }
}