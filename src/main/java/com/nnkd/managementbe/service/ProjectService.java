package com.nnkd.managementbe.service;

import com.nnkd.managementbe.model.Project;
import com.nnkd.managementbe.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectService {
    ProjectRepository projectRepository;

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsHasUser(ObjectId id) {
        return projectRepository.findByCreatorOrMembers(id);
    }

}
