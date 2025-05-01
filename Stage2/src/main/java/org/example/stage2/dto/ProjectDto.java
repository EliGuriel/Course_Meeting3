package org.example.stage2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String projectId;
    
    @NotBlank(message = "Project name is required")
    @Size(min = 2, max = 100, message = "Project name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
    
    @NotNull(message = "Start date is required")
    private Date startDate;
    
    private Date endDate;
    
    @NotBlank(message = "Status is required")
    @Size(min = 2, max = 20, message = "Status must be between 2 and 20 characters")
    private String status; // ACTIVE, COMPLETED, POSTPONED
    
    private String personId;
}