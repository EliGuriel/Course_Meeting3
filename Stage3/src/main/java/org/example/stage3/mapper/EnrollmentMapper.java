package org.example.stage3.mapper;

import org.example.stage3.collection.Course;
import org.example.stage3.collection.Enrollment;
import org.example.stage3.collection.Student;
import org.example.stage3.dto.EnrollmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnrollmentMapper {
    
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    @Autowired
    public EnrollmentMapper(StudentMapper studentMapper, CourseMapper courseMapper) {
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
    }

    public Enrollment toEntity(EnrollmentDto dto) {
        if (dto == null) {
            return null;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setId(dto.getId());
        enrollment.setStudentId(dto.getStudentId());
        enrollment.setCourseId(dto.getCourseId());
        enrollment.setEnrollmentDate(dto.getEnrollmentDate());
        enrollment.setGrade(dto.getGrade());
        enrollment.setScore(dto.getScore());
        enrollment.setStatus(dto.getStatus());
        enrollment.setNotes(dto.getNotes());

        return enrollment;
    }

    public EnrollmentDto toDto(Enrollment entity) {
        if (entity == null) {
            return null;
        }

        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudentId());
        dto.setCourseId(entity.getCourseId());
        dto.setEnrollmentDate(entity.getEnrollmentDate());
        dto.setGrade(entity.getGrade());
        dto.setScore(entity.getScore());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());

        return dto;
    }

    public EnrollmentDto toDtoWithDetails(Enrollment entity, Student student, Course course) {
        if (entity == null) {
            return null;
        }

        EnrollmentDto dto = toDto(entity);
        
        if (student != null) {
            dto.setStudent(studentMapper.toBasicDto(student));
        }
        
        if (course != null) {
            dto.setCourse(courseMapper.toBasicDto(course));
        }

        return dto;
    }

    public List<EnrollmentDto> toDtoList(List<Enrollment> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void updateEnrollmentFromDto(Enrollment enrollment, EnrollmentDto dto) {
        if (enrollment == null || dto == null) {
            return;
        }

        // Only update if values are provided
        if (dto.getGrade() != null) {
            enrollment.setGrade(dto.getGrade());
        }
        
        if (dto.getScore() != null) {
            enrollment.setScore(dto.getScore());
        }
        
        if (dto.getStatus() != null) {
            enrollment.setStatus(dto.getStatus());
        }
        
        if (dto.getNotes() != null) {
            enrollment.setNotes(dto.getNotes());
        }
    }
}