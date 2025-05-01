package org.example.stage2.controller;

import jakarta.validation.Valid;
import org.example.stage2.dto.PersonBasicDto;
import org.example.stage2.dto.ProjectDto;
import org.example.stage2.exception.ResourceIdMismatchException;
import org.example.stage2.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjectsAsDto();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable String id) {
        ProjectDto projectDto = projectService.getProjectDtoById(id);
        return ResponseEntity.ok(projectDto);
    }
    
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<ProjectDto>> getProjectsByPersonId(@PathVariable String personId) {
        List<ProjectDto> projects = projectService.getProjectsByPersonIdAsDto(personId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectDto projectDto) {
        ProjectDto createdProject = projectService.createProjectAndReturnDto(projectDto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProject.getProjectId())
                .toUri();
        
        return ResponseEntity.created(location).body(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable String id,
            @Valid @RequestBody ProjectDto projectDto) {
        
        // Check if the ID in the path matches the ID in the DTO
        if (projectDto.getProjectId() != null && !id.equals(projectDto.getProjectId())) {
            throw new ResourceIdMismatchException("Path ID doesn't match the ID in the request body");
        }
        
        // Ensure the ID is set in the DTO
        projectDto.setProjectId(id);
        
        ProjectDto updatedProject = projectService.updateProjectAndReturnDto(projectDto);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{projectId}/assign-person/{personId}")
    public ResponseEntity<ProjectDto> assignProjectToPerson(
            @PathVariable String projectId,
            @PathVariable String personId) {
        
        ProjectDto updatedProject = projectService.assignProjectToPerson(projectId, personId);
        return ResponseEntity.ok(updatedProject);
    }
    
    @DeleteMapping("/{projectId}/remove-person")
    public ResponseEntity<ProjectDto> removePersonFromProject(@PathVariable String projectId) {
        ProjectDto updatedProject = projectService.removePersonFromProject(projectId);
        return ResponseEntity.ok(updatedProject);
    }
    
    @GetMapping("/{id}/person")
    public ResponseEntity<PersonBasicDto> getPersonForProject(@PathVariable String id) {
        PersonBasicDto person = projectService.getPersonForProject(id);
        return ResponseEntity.ok(person);
    }
}