package org.example.stage2.repository;

import org.example.stage2.collection.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
    
    // Find person by email
    Optional<Person> findByEmail(String email);
    
    // Find persons by last name
    List<Person> findByLastName(String lastName);
    
    // Find persons by city in any of their addresses
    @Query("{ 'addresses.city': ?0 }")
    List<Person> findByAddressCity(String city);
    
    // Find persons by age greater than
    List<Person> findByAgeGreaterThan(int age);
    
    // Find persons by age between
    List<Person> findByAgeBetween(int minAge, int maxAge);
    
    // Find persons by hobby
    @Query("{ 'hobbies': ?0 }")
    List<Person> findByHobby(String hobby);
    
    // Check if email exists
    boolean existsByEmail(String email);
}