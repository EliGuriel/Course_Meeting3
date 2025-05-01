package org.example.stage3.service;

import org.example.stage3.collection.Course;
import org.example.stage3.collection.Student;
import org.example.stage3.dto.BasicCourseDto;
import org.example.stage3.dto.StudentDto;
import org.example.stage3.dto.StudentWithCoursesDto;
import org.example.stage3.exception.ResourceAlreadyExistsException;
import org.example.stage3.exception.ResourceNotFoundException;
import org.example.stage3.mapper.CourseMapper;
import org.example.stage3.mapper.StudentMapper;
import org.example.stage3.repository.CourseRepository;
import org.example.stage3.repository.EnrollmentRepository;
import org.example.stage3.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              CourseRepository courseRepository,
                              EnrollmentRepository enrollmentRepository,
                              StudentMapper studentMapper,
                              CourseMapper courseMapper) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public List<StudentDto> getAllStudentsAsDto() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto getStudentById(String id) {
        Student student = findStudentById(id);
        return studentMapper.toDto(student);
    }

    @Override
    public StudentWithCoursesDto getStudentWithCourses(String id) {
        Student student = findStudentById(id);
        
        List<Course> courses = new ArrayList<>();
        if (student.getCourseIds() != null && !student.getCourseIds().isEmpty()) {
            courses = courseRepository.findAllById(student.getCourseIds());
        }
        
        return studentMapper.toStudentWithCoursesDto(student, courses);
    }

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        // Check if email already exists
        if (studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Student with email '" + studentDto.getEmail() + "' already exists");
        }
        
        try {
            Student student = studentMapper.toEntity(studentDto);
            Student savedStudent = studentRepository.save(student);
            return studentMapper.toDto(savedStudent);
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistsException("Student with email '" + studentDto.getEmail() + "' already exists");
        }
    }

    @Override
    public StudentDto updateStudent(String id, StudentDto studentDto) {
        Student existingStudent = findStudentById(id);
        
        // Check if email already exists for another student
        if (!existingStudent.getEmail().equals(studentDto.getEmail()) && 
            studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Student with email '" + studentDto.getEmail() + "' already exists");
        }
        
        try {
            studentMapper.updateStudentFromDto(existingStudent, studentDto);
            Student updatedStudent = studentRepository.save(existingStudent);
            return studentMapper.toDto(updatedStudent);
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistsException("Student with email '" + studentDto.getEmail() + "' already exists");
        }
    }

    @Override
    public void deleteStudent(String id) {
        Student student = findStudentById(id);
        
        // Delete all enrollments for this student
        enrollmentRepository.deleteByStudentId(student.getId());
        
        // Remove student from courses' students
        if (student.getCourseIds() != null && !student.getCourseIds().isEmpty()) {
            for (String courseId : student.getCourseIds()) {
                Course course = courseRepository.findById(courseId)
                        .orElse(null);
                
                if (course != null && course.getStudentIds() != null) {
                    course.getStudentIds().remove(student.getId());
                    courseRepository.save(course);
                }
            }
        }
        
        // Delete the student
        studentRepository.delete(student);
    }

    @Override
    public List<StudentDto> getStudentsByLastName(String lastName) {
        List<Student> students = studentRepository.findByLastName(lastName);
        return students.stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> getStudentsByYearOfStudy(Integer yearOfStudy) {
        List<Student> students = studentRepository.findByYearOfStudy(yearOfStudy);
        return students.stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> getStudentsWithGpaGreaterThan(Double minGpa) {
        List<Student> students = studentRepository.findByGpaGreaterThanEqual(minGpa);
        return students.stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDto> getStudentsWithoutCourses() {
        List<Student> students = studentRepository.findStudentsWithoutCourses();
        return students.stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BasicCourseDto> getCoursesForStudent(String studentId) {
        Student student = findStudentById(studentId);
        
        List<Course> courses = new ArrayList<>();
        if (student.getCourseIds() != null && !student.getCourseIds().isEmpty()) {
            courses = courseRepository.findAllById(student.getCourseIds());
        }
        
        return courses.stream()
                .map(courseMapper::toBasicDto)
                .collect(Collectors.toList());
    }
    
    // Helper method to find a student by ID or throw an exception
    private Student findStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }
}
