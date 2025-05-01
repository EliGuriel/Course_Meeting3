package org.example.stage2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stage2.collection.Project;
import org.example.stage2.model.Address;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonWithProjects {
    private String personId;
    private String firstName;
    private String lastName;
    private Integer age;
    private List<String> hobbies;
    private List<Address> addresses;
    private List<Project> projects;
}