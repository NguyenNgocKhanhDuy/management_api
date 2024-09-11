package com.nnkd.managementbe.service.task;

import com.nnkd.managementbe.model.task.TaskResponse;
import com.nnkd.managementbe.repository.task.TaskResponseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TaskResponseService {
    TaskResponseRepository taskRepository;

    public List<TaskResponse> getTasksOfProject(ObjectId id) {
        return taskRepository.findByProject(id);
    }
}
