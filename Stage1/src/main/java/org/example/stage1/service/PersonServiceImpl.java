package org.example.stage1.service;

import lombok.RequiredArgsConstructor;
import org.example.stage1.collection.Person;
import org.example.stage1.dto.PersonDto;
import org.example.stage1.exception.NotExistsException;
import org.example.stage1.mapper.PersonMapper;
import org.example.stage1.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    @Override
    public PersonDto addPerson(PersonDto personDto) {
        Person person = personMapper.toEntity(personDto);
        Person saved = personRepository.save(person);
        return personMapper.toDto(saved);
    }

    @Override
    public PersonDto getPersonById(String id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotExistsException("Person with id " + id + " does not exist"));
        return personMapper.toDto(person);
    }

    @Override
    public List<PersonDto> getAllPersons() {
        return personRepository.findAll().stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDto updatePerson(PersonDto personDto, String id) {
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new NotExistsException("Person with id " + id + " does not exist"));
        
        personMapper.updateEntityFromDto(existingPerson, personDto);
        Person updated = personRepository.save(existingPerson);
        return personMapper.toDto(updated);
    }

    @Override
    public void deletePerson(String id) {
        if (!personRepository.existsById(id)) {
            throw new NotExistsException("Person with id " + id + " does not exist");
        }
        personRepository.deleteById(id);
    }
}