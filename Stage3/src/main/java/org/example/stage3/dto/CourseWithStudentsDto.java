package org.example.stage3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseWithStudentsDto {
    private String id;
    private String name;
    private String description;
    private String department;
    private Integer credits;
    private Date startDate;
    private Date endDate;
    private List<BasicStudentDto> students;
}
