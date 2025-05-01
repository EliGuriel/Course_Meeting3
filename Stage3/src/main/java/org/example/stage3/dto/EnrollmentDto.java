package org.example.stage3.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    
    private String id;
    private String studentId;
    private String courseId;
    private Date enrollmentDate;
    
    @Size(max = 2, message = "Grade must be at most 2 characters")
    private String grade;
    
    @Min(value = 0, message = "Score cannot be negative")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Double score;
    
    @Size(max = 20, message = "Status must be at most 20 characters")
    private String status;
    
    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
    
    // These fields will not be part of the JSON but are used to provide 
    // additional information in responses
    private BasicStudentDto student;
    private BasicCourseDto course;
}
