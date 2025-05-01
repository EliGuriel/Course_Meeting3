package org.example.stage1.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.stage1.model.Address;
import org.springframework.data.annotation.Id;
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
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    private Integer age;
    
    private List<String> hobbies;
    
    @Valid
    private List<Address> addresses;
}