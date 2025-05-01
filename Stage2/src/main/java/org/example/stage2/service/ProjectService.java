package org.example.stage2.service;

import org.example.stage2.collection.Project;
import org.example.stage2.dto.PersonBasicDto;
import org.example.stage2.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    
    List<ProjectDto> getAllProjectsAsDto();
    
    ProjectDto getProjectDtoById(String id);
    
    List<Project> getProjectsByPersonId(String personId);
    
    List<ProjectDto> getProjectsByPersonIdAsDto(String personId);
    
    ProjectDto createProjectAndReturnDto(ProjectDto projectDto);
    
    ProjectDto updateProjectAndReturnDto(ProjectDto projectDto);
    
    void deleteProject(String id);
    
    void deleteProjectsByPersonId(String personId);
    
    ProjectDto assignProjectToPerson(String projectId, String personId);
    
    ProjectDto removePersonFromProject(String projectId);
    
    PersonBasicDto getPersonForProject(String projectId);
}