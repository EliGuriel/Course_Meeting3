package org.example.stage1.mapper;

import org.example.stage1.collection.Person;
import org.example.stage1.dto.PersonDto;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    
    public Person toEntity(PersonDto dto) {
        if (dto == null) {
            return null;
        }
        
        Person person = new Person();
        person.setPersonId(dto.getPersonId());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAge(dto.getAge());
        person.setHobbies(dto.getHobbies());
        person.setAddresses(dto.getAddresses());
        
        return person;
    }
    
    public PersonDto toDto(Person entity) {
        if (entity == null) {
            return null;
        }
        
        PersonDto dto = new PersonDto();
        dto.setPersonId(entity.getPersonId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAge(entity.getAge());
        dto.setHobbies(entity.getHobbies());
        dto.setAddresses(entity.getAddresses());
        
        return dto;
    }
    
    public void updateEntityFromDto(Person entity, PersonDto dto) {
        if (entity == null || dto == null) {
            return;
        }
        
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAge(dto.getAge());
        entity.setHobbies(dto.getHobbies());
        entity.setAddresses(dto.getAddresses());
    }
}