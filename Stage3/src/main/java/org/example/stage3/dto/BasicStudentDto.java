package org.example.stage3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicStudentDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer yearOfStudy;
}