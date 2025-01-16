package com.nnkd.managementbe.service.subtask;

import com.nnkd.managementbe.dto.request.SubTaskUpdateRequest;
import com.nnkd.managementbe.model.subtask.SubTaskRequest;
import com.nnkd.managementbe.model.subtask.SubTaskResponse;
import com.nnkd.managementbe.repository.subtask.SubTaskRequestRepository;
import com.nnkd.managementbe.repository.subtask.SubTaskResponseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubTaskResponseService {
    SubTaskResponseRepository subTaskResponseRepository;

    public List<SubTaskResponse> getSubTasksOfTask(ObjectId id) {
        return subTaskResponseRepository.findByTask(id);
    }

    public SubTaskResponse getSubTaskById(String id) {
        try {
            SubTaskResponse subTaskResponse = subTaskResponseRepository.findById(id).orElse(null);
            return subTaskResponse;
        }catch(NoSuchElementException e) {
            throw new NoSuchElementException("No subtask found: "+id);
        }
    }


}
