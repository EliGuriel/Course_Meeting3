package org.example.stage3.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Document(collection = "students")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student {
    
    @Id
    private String id;
    
    private String firstName;
    private String lastName;
    
    @Indexed(unique = true)
    private String email;
    
    private Integer yearOfStudy;
    private Double gpa;
    private List<String> courseIds; // IDs of courses the student is enrolled in
}