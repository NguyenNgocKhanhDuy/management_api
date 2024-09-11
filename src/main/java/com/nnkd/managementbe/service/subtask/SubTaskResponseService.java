package com.nnkd.managementbe.service.subtask;

import com.nnkd.managementbe.model.subtask.SubTaskResponse;
import com.nnkd.managementbe.repository.subtask.SubTaskRequestRepository;
import com.nnkd.managementbe.repository.subtask.SubTaskResponseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubTaskResponseService {
    SubTaskResponseRepository subTaskResponseRepository;

    public List<SubTaskResponse> getSubTasksOfTask(ObjectId id) {
        return subTaskResponseRepository.findByTask(id);
    }
}
