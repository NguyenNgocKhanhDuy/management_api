package com.nnkd.managementbe.service.subtask;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.SubTaskCreationRequest;
import com.nnkd.managementbe.dto.request.SubTaskUpdateRequest;
import com.nnkd.managementbe.model.subtask.SubTaskRequest;
import com.nnkd.managementbe.model.subtask.SubTaskResponse;
import com.nnkd.managementbe.repository.subtask.SubTaskRequestRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubTaskRequestService {
    SubTaskRequestRepository subTaskRequestRepository;

    public SubTaskRequest addSubTask(SubTaskCreationRequest request) {
        SubTaskRequest subTaskRequest = SubTaskRequest.builder().title(request.getTitle())
                .completed(request.isCompleted()).task(request.getTask()).build();
        return subTaskRequestRepository.save(subTaskRequest);
    }

    public SubTaskRequest updateSubTaskTitle(SubTaskUpdateRequest request) {
        try {
            SubTaskRequest subTaskResponse = subTaskRequestRepository.findById(request.getId()).get();
            subTaskResponse.setTitle(request.getTitle());
            return subTaskRequestRepository.save(subTaskResponse);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No subtask found: "+request.getId());
        }
    }

    public SubTaskRequest updateSubTaskStatus(SubTaskUpdateRequest request) {
        try {
            SubTaskRequest subTaskResponse = subTaskRequestRepository.findById(request.getId()).get();
            subTaskResponse.setCompleted(request.isCompleted());
            return subTaskRequestRepository.save(subTaskResponse);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No subtask found: "+request.getId());
        }
    }

    public ApiResponse deleteSubTask(String id) {
        try {
            subTaskRequestRepository.findById(id);
            subTaskRequestRepository.deleteById(id);
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setResult("Delete Successfully");
            return apiResponse;
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No subtask found: " + id);
        }
    }

}
