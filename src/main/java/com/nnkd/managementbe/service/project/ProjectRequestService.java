package com.nnkd.managementbe.service.project;

import com.nnkd.managementbe.dto.request.ProjectCreationRequest;
import com.nnkd.managementbe.model.project.ProjectRequest;
import com.nnkd.managementbe.repository.project.ProjectRequestRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectRequestService {
    ProjectRequestRepository projectRepository;

    public ProjectRequest addProject(ProjectCreationRequest request) {
        ProjectRequest project = ProjectRequest.builder().name(request.getName()).creator(request.getCreator()).date(request.getDate()).build();
        return projectRepository.save(project);
    }
}
