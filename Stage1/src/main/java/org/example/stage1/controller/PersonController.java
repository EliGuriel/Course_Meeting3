package org.example.stage1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.stage1.dto.PersonDto;
import org.example.stage1.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable String id) {
        PersonDto personDto = personService.getPersonById(id);
        return ResponseEntity.ok(personDto);
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAllPersons() {
        List<PersonDto> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @PostMapping
    public ResponseEntity<PersonDto> addPerson(@Valid @RequestBody PersonDto personDto) {
        PersonDto addedDto = personService.addPerson(personDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedDto.getPersonId())
                .toUri();

        return ResponseEntity.created(location).body(addedDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> updatePerson(@Valid @RequestBody PersonDto personDto, @PathVariable String id) {
        PersonDto updatedDto = personService.updatePerson(personDto, id);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}