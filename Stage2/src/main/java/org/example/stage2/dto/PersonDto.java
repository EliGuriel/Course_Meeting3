package org.example.stage2.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private String personId;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @Min(value = 0, message = "Age must be a positive number")
    private Integer age;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotEmpty(message = "Person must have at least one hobby")
    private List<String> hobbies;
    
    @Valid
    private List<AddressDto> addresses;
}
