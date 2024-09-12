package com.nnkd.managementbe.service.project;

import com.nnkd.managementbe.dto.request.ProjectCreationRequest;
import com.nnkd.managementbe.model.project.ProjectRequest;
import com.nnkd.managementbe.model.project.ProjectResponse;
import com.nnkd.managementbe.repository.project.ProjectRequestRepository;
import com.nnkd.managementbe.repository.project.ProjectResponseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectResponseService {
    ProjectResponseRepository projectRepository;

    public List<ProjectResponse> getProjectsHasUser(ObjectId id) {
        return projectRepository.findByCreatorOrMembers(id);
    }

    public ProjectResponse getProjectById(String id) {
        try {
            ProjectResponse projectResponse = projectRepository.findById(id).get();
            return projectResponse;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No project found: " + id);
        }
    }

}
