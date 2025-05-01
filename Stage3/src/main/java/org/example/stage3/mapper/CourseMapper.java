package org.example.stage3.mapper;

import org.example.stage3.collection.Course;
import org.example.stage3.collection.Student;
import org.example.stage3.dto.BasicCourseDto;
import org.example.stage3.dto.BasicStudentDto;
import org.example.stage3.dto.CourseDto;
import org.example.stage3.dto.CourseWithStudentsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    private final StudentMapper studentMapper;

    @Autowired
    /**
     * @Lazy is used to avoid circular dependency issues, because CourseMapper is used in StudentMapper
     * and vice versa.
     */
    public CourseMapper(@Lazy StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public Course toEntity(CourseDto dto) {
        if (dto == null) {
            return null;
        }

        Course course = new Course();
        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setDepartment(dto.getDepartment());
        course.setCredits(dto.getCredits());
        course.setStartDate(dto.getStartDate());
        course.setEndDate(dto.getEndDate());
        course.setStudentIds(dto.getStudentIds());

        return course;
    }

    public CourseDto toDto(Course entity) {
        if (entity == null) {
            return null;
        }

        CourseDto dto = new CourseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setDepartment(entity.getDepartment());
        dto.setCredits(entity.getCredits());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStudentIds(entity.getStudentIds());

        return dto;
    }

    public BasicCourseDto toBasicDto(Course entity) {
        if (entity == null) {
            return null;
        }

        return new BasicCourseDto(
                entity.getId(),
                entity.getName(),
                entity.getDepartment(),
                entity.getCredits()
        );
    }

    public CourseWithStudentsDto toCourseWithStudentsDto(Course course, List<Student> students) {
        if (course == null) {
            return null;
        }

        List<BasicStudentDto> studentDtos = students.stream()
                .map(studentMapper::toBasicDto)
                .collect(Collectors.toList());

        CourseWithStudentsDto dto = new CourseWithStudentsDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setDepartment(course.getDepartment());
        dto.setCredits(course.getCredits());
        dto.setStartDate(course.getStartDate());
        dto.setEndDate(course.getEndDate());
        dto.setStudents(studentDtos);

        return dto;
    }

    public List<CourseDto> toDtoList(List<Course> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<BasicCourseDto> toBasicDtoList(List<Course> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toBasicDto)
                .collect(Collectors.toList());
    }

    public void updateCourseFromDto(Course course, CourseDto dto) {
        if (course == null || dto == null) {
            return;
        }

        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setDepartment(dto.getDepartment());
        course.setCredits(dto.getCredits());
        course.setStartDate(dto.getStartDate());
        course.setEndDate(dto.getEndDate());
        
        // Only update studentIds if it's provided in the DTO
        if (dto.getStudentIds() != null) {
            course.setStudentIds(dto.getStudentIds());
        }
    }
}
