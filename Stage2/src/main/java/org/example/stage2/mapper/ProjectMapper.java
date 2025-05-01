package org.example.stage2.mapper;

import org.example.stage2.collection.Project;
import org.example.stage2.dto.ProjectDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectDto dto) {
        if (dto == null) {
            return null;
        }
        
        Project project = new Project();
        project.setProjectId(dto.getProjectId());
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setStatus(dto.getStatus());
        project.setPersonId(dto.getPersonId());
        
        return project;
    }
    
    public ProjectDto toDto(Project entity) {
        if (entity == null) {
            return null;
        }
        
        ProjectDto dto = new ProjectDto();
        dto.setProjectId(entity.getProjectId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setPersonId(entity.getPersonId());
        
        return dto;
    }
    
    public List<ProjectDto> toDtoList(List<Project> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public void updateProjectFromDto(Project project, ProjectDto dto) {
        if (project == null || dto == null) {
            return;
        }
        
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setStatus(dto.getStatus());
        project.setPersonId(dto.getPersonId());
    }
}