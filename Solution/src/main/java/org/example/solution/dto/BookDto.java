package org.example.solution.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Author is required")
    private String author;
    
    private Integer publishYear;
    
    @NotBlank(message = "Genre is required")
    private String genre;
    
    @NotNull(message = "Availability status is required")
    private Boolean available = true;
    
    private List<String> tags;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private Integer rating;
}