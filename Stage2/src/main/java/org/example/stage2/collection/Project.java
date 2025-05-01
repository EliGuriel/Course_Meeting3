package org.example.stage2.collection;

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
@Document(collection = "project")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project {
    @Id
    private String projectId;
    private String name;
    private String description;
    private Date startDate; // the format is yyyy-MM-dd
    private Date endDate;
    private String status; // ACTIVE, COMPLETED, POSTPONED
    private String personId; // personId of the person who created the project
}
