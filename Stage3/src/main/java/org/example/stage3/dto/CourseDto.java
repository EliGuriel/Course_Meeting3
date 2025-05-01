package org.example.stage3.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    
    private String id;
    
    @NotBlank(message = "Course name is required")
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    private Integer credits;
    
    private Date startDate;
    private Date endDate;
    
    // Not needed in request DTOs but useful in response DTOs
    private List<String> studentIds;
}