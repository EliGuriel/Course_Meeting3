package org.example.stage1.service;

import org.example.stage1.dto.PersonDto;

import java.util.List;

public interface PersonService {
    PersonDto addPerson(PersonDto personDto);
    PersonDto getPersonById(String id);
    List<PersonDto> getAllPersons();
    PersonDto updatePerson(PersonDto personDto, String id);
    void deletePerson(String id);
}