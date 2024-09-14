package com.nnkd.managementbe.service.task;

import com.nnkd.managementbe.model.task.TaskResponse;
import com.nnkd.managementbe.repository.task.TaskResponseRepository;
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
public class TaskResponseService {
    TaskResponseRepository taskRepository;

    public List<TaskResponse> getTasksOfProject(ObjectId id) {
        return taskRepository.findByProject(id);
    }

    public TaskResponse getTaskById(String id) {
        try {
            TaskResponse taskResponse = taskRepository.findById(id).get();
            return taskResponse;
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("No Task found: "+id);
        }
    }

    public List<TaskResponse> getTaskByStatus(String status) {
        return taskRepository.findByStatus(status).stream().toList();
    }
}
