package org.example.stage3.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "enrollments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Enrollment {
    
    @Id
    private String id;
    
    private String studentId;
    private String courseId;
    private Date enrollmentDate;
    private String grade;
    private Double score;
    private String status; // e.g., "ACTIVE", "COMPLETED", "WITHDRAWN"
    private String notes;
    
    public Enrollment(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = new Date();
        this.status = "ACTIVE";
    }
}