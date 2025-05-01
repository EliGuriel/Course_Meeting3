package org.example.stage2.service;

import org.example.stage2.collection.Person;
import org.example.stage2.collection.Project;
import org.example.stage2.dto.PersonBasicDto;
import org.example.stage2.dto.ProjectDto;
import org.example.stage2.exception.ResourceNotFoundException;
import org.example.stage2.mapper.PersonMapper;
import org.example.stage2.mapper.ProjectMapper;
import org.example.stage2.repository.PersonRepository;
import org.example.stage2.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    private final ProjectMapper projectMapper;
    private final PersonMapper personMapper;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              PersonRepository personRepository,
                              ProjectMapper projectMapper,
                              PersonMapper personMapper) {
        this.projectRepository = projectRepository;
        this.personRepository = personRepository;
        this.projectMapper = projectMapper;
        this.personMapper = personMapper;
    }

    @Override
    public List<ProjectDto> getAllProjectsAsDto() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto getProjectDtoById(String id) {
        Project project = getProjectById(id);
        return projectMapper.toDto(project);
    }

    @Override
    public List<Project> getProjectsByPersonId(String personId) {
        return projectRepository.findByPersonId(personId);
    }

    @Override
    public List<ProjectDto> getProjectsByPersonIdAsDto(String personId) {
        List<Project> projects = getProjectsByPersonId(personId);
        return projectMapper.toDtoList(projects);
    }

    @Override
    public ProjectDto createProjectAndReturnDto(ProjectDto projectDto) {
        // If personId is provided, verify that the person exists
        if (projectDto.getPersonId() != null && !projectDto.getPersonId().isEmpty()) {
            if (!personRepository.existsById(projectDto.getPersonId())) {
                throw new ResourceNotFoundException("Person not found with id: " + projectDto.getPersonId());
            }
        }
        
        Project project = projectMapper.toEntity(projectDto);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    @Override
    public ProjectDto updateProjectAndReturnDto(ProjectDto projectDto) {
        String id = projectDto.getProjectId();
        if (id == null) {
            throw new IllegalArgumentException("Project ID cannot be null for update operation");
        }
        
        // Check if project exists
        Project existingProject = getProjectById(id);
        
        // If personId is provided, verify that the person exists
        if (projectDto.getPersonId() != null && !projectDto.getPersonId().isEmpty()) {
            if (!personRepository.existsById(projectDto.getPersonId())) {
                throw new ResourceNotFoundException("Person not found with id: " + projectDto.getPersonId());
            }
        }
        
        projectMapper.updateProjectFromDto(existingProject, projectDto);
        Project updatedProject = projectRepository.save(existingProject);
        return projectMapper.toDto(updatedProject);
    }

    @Override
    public void deleteProject(String id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        
        projectRepository.deleteById(id);
    }

    @Override
    public void deleteProjectsByPersonId(String personId) {
        List<Project> projects = projectRepository.findByPersonId(personId);
        projectRepository.deleteAll(projects);
    }

    @Override
    public ProjectDto assignProjectToPerson(String projectId, String personId) {
        // Check if project exists
        Project project = getProjectById(projectId);
        
        // Check if person exists
        if (!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("Person not found with id: " + personId);
        }
        
        project.setPersonId(personId);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDto(updatedProject);
    }

    @Override
    public ProjectDto removePersonFromProject(String projectId) {
        Project project = getProjectById(projectId);
        
        project.setPersonId(null);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDto(updatedProject);
    }

    @Override
    public PersonBasicDto getPersonForProject(String projectId) {
        Project project = getProjectById(projectId);
        
        if (project.getPersonId() == null || project.getPersonId().isEmpty()) {
            return null;
        }
        
        Person person = personRepository.findById(project.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + project.getPersonId()));
        
        return personMapper.toBasicDto(person);
    }
    
    // Helper method to get a Project by ID or throw an exception
    private Project getProjectById(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }
}