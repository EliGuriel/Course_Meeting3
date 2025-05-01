package org.example.stage3.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a student's grade in a course
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeUpdateRequest {
    
    @Size(max = 2, message = "Grade must be at most 2 characters")
    private String grade;
    
    @Min(value = 0, message = "Score cannot be negative")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Double score;
}