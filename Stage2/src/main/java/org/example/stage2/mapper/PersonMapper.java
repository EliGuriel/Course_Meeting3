package org.example.stage2.mapper;

import org.example.stage2.collection.Person;
import org.example.stage2.dto.*;
import org.example.stage2.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonMapper {

    private final ProjectMapper projectMapper;

    @Autowired
    public PersonMapper(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    public Person toEntity(PersonDto dto) {
        if (dto == null) {
            return null;
        }
        
        Person person = new Person();
        person.setPersonId(dto.getPersonId());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAge(dto.getAge());
        person.setEmail(dto.getEmail());
        person.setHobbies(dto.getHobbies());
        
        if (dto.getAddresses() != null) {
            List<Address> addresses = dto.getAddresses().stream()
                    .map(this::addressDtoToAddress)
                    .collect(Collectors.toList());
            person.setAddresses(addresses);
        }
        
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
        dto.setEmail(entity.getEmail());
        dto.setHobbies(entity.getHobbies());
        
        if (entity.getAddresses() != null) {
            List<AddressDto> addressDtos = entity.getAddresses().stream()
                    .map(this::addressToAddressDto)
                    .collect(Collectors.toList());
            dto.setAddresses(addressDtos);
        }
        
        return dto;
    }
    
    public PersonBasicDto toBasicDto(Person entity) {
        if (entity == null) {
            return null;
        }
        
        return new PersonBasicDto(
                entity.getPersonId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail()
        );
    }
    
    public PersonWithProjectsDto toPersonWithProjectsDto(Person person, List<ProjectDto> projects) {
        if (person == null) {
            return null;
        }
        
        PersonWithProjectsDto dto = new PersonWithProjectsDto();
        dto.setPersonId(person.getPersonId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAge(person.getAge());
        dto.setEmail(person.getEmail());
        dto.setHobbies(person.getHobbies());
        
        if (person.getAddresses() != null) {
            List<AddressDto> addressDtos = person.getAddresses().stream()
                    .map(this::addressToAddressDto)
                    .collect(Collectors.toList());
            dto.setAddresses(addressDtos);
        }
        
        dto.setProjects(projects);
        
        return dto;
    }
    
    public void updatePersonFromDto(Person person, PersonDto dto) {
        if (person == null || dto == null) {
            return;
        }
        
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAge(dto.getAge());
        person.setEmail(dto.getEmail());
        person.setHobbies(dto.getHobbies());
        
        if (dto.getAddresses() != null) {
            List<Address> addresses = dto.getAddresses().stream()
                    .map(this::addressDtoToAddress)
                    .collect(Collectors.toList());
            person.setAddresses(addresses);
        }
    }
    
    private Address addressDtoToAddress(AddressDto dto) {
        if (dto == null) {
            return null;
        }
        
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZip(dto.getZip());
        
        return address;
    }
    
    private AddressDto addressToAddressDto(Address entity) {
        if (entity == null) {
            return null;
        }
        
        AddressDto dto = new AddressDto();
        dto.setStreet(entity.getStreet());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setZip(entity.getZip());
        
        return dto;
    }
}