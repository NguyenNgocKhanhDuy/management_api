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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectResponseService {
    ProjectResponseRepository projectRepository;

    public List<ProjectResponse> getProjectsHasUser(ObjectId id) {
        return projectRepository.findByCreatorOrMembers(id);
    }

}
