package org.example.stage2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A simplified DTO for Person entities when used in relationships
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonBasicDto {
    private String personId;
    private String firstName;
    private String lastName;
    private String email;
}