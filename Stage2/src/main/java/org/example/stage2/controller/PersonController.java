package org.example.stage2.controller;

import jakarta.validation.Valid;
import org.example.stage2.dto.PersonDto;
import org.example.stage2.dto.PersonWithProjectsDto;
import org.example.stage2.exception.ResourceIdMismatchException;
import org.example.stage2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAllPersons() {
        List<PersonDto> persons = personService.getAllPersonsAsDto();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPersonById(@PathVariable String id) {
        PersonDto personDto = personService.getPersonDtoById(id);
        return ResponseEntity.ok(personDto);
    }
    
    @GetMapping("/{id}/with-projects")
    public ResponseEntity<PersonWithProjectsDto> getPersonWithProjects(@PathVariable String id) {
        PersonWithProjectsDto dto = personService.getPersonWithProjects(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@Valid @RequestBody PersonDto personDto) {
        PersonDto createdPerson = personService.createPersonAndReturnDto(personDto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPerson.getPersonId())
                .toUri();
        
        return ResponseEntity.created(location).body(createdPerson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> updatePerson(
            @PathVariable String id,
            @Valid @RequestBody PersonDto personDto) {
        
        // Check if the ID in the path matches the ID in the DTO
        if (personDto.getPersonId() != null && !id.equals(personDto.getPersonId())) {
            throw new ResourceIdMismatchException("Path ID doesn't match the ID in the request body");
        }
        
        // Ensure the ID is set in the DTO
        personDto.setPersonId(id);
        
        PersonDto updatedPerson = personService.updatePersonAndReturnDto(personDto);
        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/without-projects")
    public ResponseEntity<List<PersonDto>> getPersonsWithoutProjects() {
        List<PersonDto> persons = personService.getPersonsWithoutProjects();
        return ResponseEntity.ok(persons);
    }
}