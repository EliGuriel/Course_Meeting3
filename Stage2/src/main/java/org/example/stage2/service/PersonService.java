package org.example.stage2.service;

import org.example.stage2.dto.PersonDto;
import org.example.stage2.dto.PersonWithProjectsDto;

import java.util.List;

public interface PersonService {
    
    List<PersonDto> getAllPersonsAsDto();
    
    PersonDto getPersonDtoById(String id);
    
    PersonWithProjectsDto getPersonWithProjects(String id);
    
    PersonDto createPersonAndReturnDto(PersonDto personDto);
    
    PersonDto updatePersonAndReturnDto(PersonDto personDto);
    
    void deletePerson(String id);
    
    List<PersonDto> getPersonsWithoutProjects();
}