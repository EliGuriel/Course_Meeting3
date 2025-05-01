package org.example.stage2.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotBlank(message = "Street is required")
    @Size(max = 100, message = "Street must be less than 100 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must be less than 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 2, message = "State must be a 2-letter code")
    private String state;

    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Zip code must be in format 12345 or 12345-6789")
    private String zip;
}