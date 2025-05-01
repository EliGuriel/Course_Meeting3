package org.example.stage3.service;

import org.example.stage3.collection.Course;
import org.example.stage3.collection.Enrollment;
import org.example.stage3.collection.Student;
import org.example.stage3.dto.EnrollmentDto;
import org.example.stage3.exception.EnrollmentAlreadyExistsException;
import org.example.stage3.exception.InvalidOperationException;
import org.example.stage3.exception.ResourceNotFoundException;
import org.example.stage3.mapper.EnrollmentMapper;
import org.example.stage3.repository.CourseRepository;
import org.example.stage3.repository.EnrollmentRepository;
import org.example.stage3.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 StudentRepository studentRepository,
                                 CourseRepository courseRepository,
                                 EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    @Override
    public List<EnrollmentDto> getAllEnrollmentsAsDto() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        return enrollments.stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDto getEnrollmentById(String id) {
        Enrollment enrollment = findEnrollmentById(id);

        Student student = studentRepository.findById(enrollment.getStudentId()).orElse(null);
        Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);

        return enrollmentMapper.toDtoWithDetails(enrollment, student, course);
    }

    @Override
    public EnrollmentDto getEnrollmentByStudentAndCourse(String studentId, String courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for student id: " + studentId +
                        " and course id: " + courseId));

        Student student = studentRepository.findById(enrollment.getStudentId()).orElse(null);
        Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);

        return enrollmentMapper.toDtoWithDetails(enrollment, student, course);
    }

    @Override
    public EnrollmentDto enrollStudentInCourse(String studentId, String courseId) {
        // Check if student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        // Check if course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Check if enrollment already exists
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new EnrollmentAlreadyExistsException("Student is already enrolled in this course");
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrollmentDate(new Date());
        enrollment.setStatus("ACTIVE");

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Update student's courses
        if (student.getCourseIds() == null) {
            student.setCourseIds(new ArrayList<>());
        }
        student.getCourseIds().add(courseId);
        studentRepository.save(student);

        // Update course's students
        if (course.getStudentIds() == null) {
            course.setStudentIds(new ArrayList<>());
        }
        course.getStudentIds().add(studentId);
        courseRepository.save(course);

        return enrollmentMapper.toDtoWithDetails(savedEnrollment, student, course);
    }

    @Override
    public EnrollmentDto updateEnrollment(String id, EnrollmentDto enrollmentDto) {
        Enrollment existingEnrollment = findEnrollmentById(id);

        // Update only certain fields
        enrollmentMapper.updateEnrollmentFromDto(existingEnrollment, enrollmentDto);

        Enrollment updatedEnrollment = enrollmentRepository.save(existingEnrollment);

        Student student = studentRepository.findById(updatedEnrollment.getStudentId()).orElse(null);
        Course course = courseRepository.findById(updatedEnrollment.getCourseId()).orElse(null);

        return enrollmentMapper.toDtoWithDetails(updatedEnrollment, student, course);
    }

    @Override
    public EnrollmentDto updateGrade(String studentId, String courseId, String grade, Double score) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for student id: " + studentId +
                        " and course id: " + courseId));

        enrollment.setGrade(grade);
        enrollment.setScore(score);

        // If a grade is assigned, mark as completed
        if (grade != null && !grade.isEmpty()) {
            enrollment.setStatus("COMPLETED");
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        Student student = studentRepository.findById(updatedEnrollment.getStudentId()).orElse(null);
        Course course = courseRepository.findById(updatedEnrollment.getCourseId()).orElse(null);

        return enrollmentMapper.toDtoWithDetails(updatedEnrollment, student, course);
    }

    @Override
    public void unenrollStudentFromCourse(String studentId, String courseId) {
        // Check if student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        // Check if course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Check if enrollment exists
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for student id: " + studentId +
                        " and course id: " + courseId));

        // Check if enrollment can be deleted (e.g., not completed)
        if ("COMPLETED".equals(enrollment.getStatus())) {
            throw new InvalidOperationException("Cannot unenroll a student from a completed course");
        }

        // Delete enrollment
        enrollmentRepository.delete(enrollment);

        // Update student's courses
        if (student.getCourseIds() != null) {
            student.getCourseIds().remove(courseId);
            studentRepository.save(student);
        }

        // Update course's students
        if (course.getStudentIds() != null) {
            course.getStudentIds().remove(studentId);
            courseRepository.save(course);
        }
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsForStudent(String studentId) {
        // Check if student exists
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        return enrollments.stream()
                .map(enrollment -> {
                    Student student = studentRepository.findById(enrollment.getStudentId()).orElse(null);
                    Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
                    return enrollmentMapper.toDtoWithDetails(enrollment, student, course);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsForCourse(String courseId) {
        // Check if course exists
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        return enrollments.stream()
                .map(enrollment -> {
                    Student student = studentRepository.findById(enrollment.getStudentId()).orElse(null);
                    Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
                    return enrollmentMapper.toDtoWithDetails(enrollment, student, course);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDto> getEnrollmentsByStatus(String status) {
        List<Enrollment> enrollments = enrollmentRepository.findByStatus(status);

        return enrollments.stream()
                .map(enrollment -> {
                    Student student = studentRepository.findById(enrollment.getStudentId()).orElse(null);
                    Course course = courseRepository.findById(enrollment.getCourseId()).orElse(null);
                    return enrollmentMapper.toDtoWithDetails(enrollment, student, course);
                })
                .collect(Collectors.toList());
    }

    // Helper method to find an enrollment by ID or throw an exception
    private Enrollment findEnrollmentById(String id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }
}
