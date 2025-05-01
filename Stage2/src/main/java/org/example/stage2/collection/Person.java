package org.example.stage2.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stage2.model.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "person")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    @Id
    private String personId;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Min(value = 0, message = "Age must be a positive number")
    private Integer age;

    @NotEmpty(message = "Person must have at least one hobby")
    private List<String> hobbies;

    @Valid // This validates each Address object in the list
    private List<Address> addresses;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Indexed(unique = true) // Creates a unique index on this field
    private String email;
}