package org.example.stage3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentWithCoursesDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer yearOfStudy;
    private Double gpa;
    private List<BasicCourseDto> courses;
}