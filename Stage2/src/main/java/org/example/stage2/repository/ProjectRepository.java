package org.example.stage2.repository;

import org.example.stage2.collection.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    
    // Find projects by person ID
    List<Project> findByPersonId(String personId);
    
    // Find projects by status
    List<Project> findByStatus(String status);
    
    // Find projects by name containing keyword
    List<Project> findByNameContainingIgnoreCase(String keyword);
    
    // Find projects starting after a date
    List<Project> findByStartDateAfter(Date date);
    
    // Find projects ending before a date
    List<Project> findByEndDateBefore(Date date);
    
    // Find projects within a date range
    List<Project> findByStartDateAfterAndEndDateBefore(Date startDate, Date endDate);
    
    // Find active projects for a person
    @Query("{ 'personId': ?0, 'status': 'ACTIVE' }")
    List<Project> findActiveProjectsByPersonId(String personId);
    
    // Find projects without a person assigned
    @Query("{ 'personId': null }")
    List<Project> findProjectsWithoutPerson();
    
    // Find projects with no end date (ongoing)
    @Query("{ 'endDate': null }")
    List<Project> findOngoingProjects();
    
    // Count projects by status
    long countByStatus(String status);
}