package org.example.stage3.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    
    private String id;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @Min(value = 1, message = "Year of study must be at least 1")
    @Max(value = 7, message = "Year of study cannot exceed 7")
    private Integer yearOfStudy;
    
    @Min(value = 0, message = "GPA cannot be negative")
    @Max(value = 4, message = "GPA cannot exceed 4.0")
    private Double gpa;
    
    // Not needed in request DTOs but useful in response DTOs
    private List<String> courseIds;
}