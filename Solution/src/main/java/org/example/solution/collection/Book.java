package org.example.solution.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
// This annotation is used to include only non-null properties in the JSON response
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    @Id
    private String id;
    
    @NotBlank(message = "Title is required")
    @Indexed
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