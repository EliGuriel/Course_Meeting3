package org.example.stage3.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Course {
    
    @Id
    private String id;
    
    private String name;
    private String description;
    private String department;
    private Integer credits;
    private Date startDate;
    private Date endDate;
    private List<String> studentIds; // IDs of students enrolled in the course
}
