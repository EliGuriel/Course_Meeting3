package org.example.stage2.service;

import org.example.stage2.collection.Person;
import org.example.stage2.dto.PersonDto;
import org.example.stage2.dto.PersonWithProjectsDto;
import org.example.stage2.dto.ProjectDto;
import org.example.stage2.exception.ResourceAlreadyExistsException;
import org.example.stage2.exception.ResourceNotFoundException;
import org.example.stage2.mapper.PersonMapper;
import org.example.stage2.mapper.ProjectMapper;
import org.example.stage2.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final ProjectService projectService;
    private final PersonMapper personMapper;
    private final ProjectMapper projectMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository,
                             ProjectService projectService,
                             PersonMapper personMapper,
                             ProjectMapper projectMapper) {
        this.personRepository = personRepository;
        this.projectService = projectService;
        this.personMapper = personMapper;
        this.projectMapper = projectMapper;
    }

    @Override
    public List<PersonDto> getAllPersonsAsDto() {
        List<Person> persons = personRepository.findAll();
        return persons.stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDto getPersonDtoById(String id) {
        Person person = getPersonById(id);
        return personMapper.toDto(person);
    }

    @Override
    public PersonWithProjectsDto getPersonWithProjects(String id) {
        Person person = getPersonById(id);
        List<ProjectDto> projects = projectService.getProjectsByPersonIdAsDto(id);
        
        return personMapper.toPersonWithProjectsDto(person, projects);
    }

    @Override
    public PersonDto createPersonAndReturnDto(PersonDto personDto) {
        try {
            Person person = personMapper.toEntity(personDto);
            Person savedPerson = personRepository.save(person);
            return personMapper.toDto(savedPerson);
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistsException("A person with this email already exists");
        }
    }

    @Override
    public PersonDto updatePersonAndReturnDto(PersonDto personDto) {
        String id = personDto.getPersonId();
        if (id == null) {
            throw new IllegalArgumentException("Person ID cannot be null for update operation");
        }
        
        Person existingPerson = getPersonById(id);
        
        try {
            personMapper.updatePersonFromDto(existingPerson, personDto);
            Person updatedPerson = personRepository.save(existingPerson);
            return personMapper.toDto(updatedPerson);
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistsException("A person with this email already exists");
        }
    }

    @Override
    public void deletePerson(String id) {
        if (!personRepository.existsById(id)) {
            throw new ResourceNotFoundException("Person not found with id: " + id);
        }
        
        // Delete associated projects first
        projectService.deleteProjectsByPersonId(id);
        
        // Then delete the person
        personRepository.deleteById(id);
    }

    @Override
    public List<PersonDto> getPersonsWithoutProjects() {
        List<Person> allPersons = personRepository.findAll();
        
        return allPersons.stream()
                .filter(person -> projectService.getProjectsByPersonId(person.getPersonId()).isEmpty())
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }
    
    // Helper method to get a Person by ID or throw an exception
    private Person getPersonById(String id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
    }
}