package org.example.stage3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicCourseDto {
    private String id;
    private String name;
    private String department;
    private Integer credits;
}