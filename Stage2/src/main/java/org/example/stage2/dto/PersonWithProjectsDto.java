package org.example.stage2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonWithProjectsDto {
    private String personId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private List<String> hobbies;
    private List<AddressDto> addresses;
    private List<ProjectDto> projects;
}