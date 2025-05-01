package org.example.stage3.mapper;

import org.example.stage3.collection.Course;
import org.example.stage3.collection.Student;
import org.example.stage3.dto.BasicCourseDto;
import org.example.stage3.dto.BasicStudentDto;
import org.example.stage3.dto.StudentDto;
import org.example.stage3.dto.StudentWithCoursesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    private CourseMapper courseMapper;

    @Autowired
    public void setCourseMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    public Student toEntity(StudentDto dto) {
        if (dto == null) {
            return null;
        }

        Student student = new Student();
        student.setId(dto.getId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setYearOfStudy(dto.getYearOfStudy());
        student.setGpa(dto.getGpa());
        student.setCourseIds(dto.getCourseIds());

        return student;
    }

    public StudentDto toDto(Student entity) {
        if (entity == null) {
            return null;
        }

        StudentDto dto = new StudentDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setYearOfStudy(entity.getYearOfStudy());
        dto.setGpa(entity.getGpa());
        dto.setCourseIds(entity.getCourseIds());

        return dto;
    }

    public BasicStudentDto toBasicDto(Student entity) {
        if (entity == null) {
            return null;
        }

        return new BasicStudentDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getYearOfStudy()
        );
    }

    public StudentWithCoursesDto toStudentWithCoursesDto(Student student, List<Course> courses) {
        if (student == null) {
            return null;
        }

        List<BasicCourseDto> courseDtos = courses.stream()
                .map(courseMapper::toBasicDto)
                .collect(Collectors.toList());

        StudentWithCoursesDto dto = new StudentWithCoursesDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setYearOfStudy(student.getYearOfStudy());
        dto.setGpa(student.getGpa());
        dto.setCourses(courseDtos);

        return dto;
    }

    public List<StudentDto> toDtoList(List<Student> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<BasicStudentDto> toBasicDtoList(List<Student> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toBasicDto)
                .collect(Collectors.toList());
    }

    public void updateStudentFromDto(Student student, StudentDto dto) {
        if (student == null || dto == null) {
            return;
        }

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setYearOfStudy(dto.getYearOfStudy());
        student.setGpa(dto.getGpa());
        
        // Only update courseIds if it's provided in the DTO
        if (dto.getCourseIds() != null) {
            student.setCourseIds(dto.getCourseIds());
        }
    }
}