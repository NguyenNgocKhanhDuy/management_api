package com.nnkd.managementbe.service.project;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.ProjectCreationRequest;
import com.nnkd.managementbe.dto.request.ProjectUpdateRequest;
import com.nnkd.managementbe.model.project.ProjectRequest;
import com.nnkd.managementbe.model.task.TaskResponse;
import com.nnkd.managementbe.repository.project.ProjectRequestRepository;
import com.nnkd.managementbe.service.task.TaskRequestService;
import com.nnkd.managementbe.service.task.TaskResponseService;
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
public class ProjectRequestService {
    ProjectRequestRepository projectRepository;
    TaskResponseService taskResponseService;
    TaskRequestService taskRequestService;

    public ProjectRequest addProject(ProjectCreationRequest request) {
        ProjectRequest project = ProjectRequest.builder().name(request.getName()).creator(request.getCreator()).date(request.getDate()).build();
        return projectRepository.save(project);
    }

    public ProjectRequest updateProjectName(ProjectUpdateRequest request) {
        try {
            ProjectRequest projectRequest = projectRepository.findById(request.getId()).get();
            projectRequest.setName(request.getName());
            return projectRepository.save(projectRequest);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No Project has id: "+request.getId());
        }
    }

    public ProjectRequest updateProjectMembers(ProjectUpdateRequest request) {
        try {
            ProjectRequest projectRequest = projectRepository.findById(request.getId()).get();
            projectRequest.setMembers(request.getMembers());
            return projectRepository.save(projectRequest);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No Project has id: "+request.getId());
        }
    }

    public ProjectRequest updateProjectPending(ProjectUpdateRequest request) {
        try {
            ProjectRequest projectRequest = projectRepository.findById(request.getId()).get();
            projectRequest.setPending(request.getPending());
            return projectRepository.save(projectRequest);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No Project has id: "+request.getId());
        }
    }

    public ApiResponse deleteProject(String id) {
        try {
            projectRepository.findById(id);
            projectRepository.deleteById(id);
            List<TaskResponse> list = taskResponseService.getTasksOfProject(new ObjectId(id));
            for (TaskResponse t: list) {
                taskRequestService.deleteTask(t.getId());
            }
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setResult("Delete Successfully");
            return apiResponse;
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No subtask found: " + id);
        }
    }


}
