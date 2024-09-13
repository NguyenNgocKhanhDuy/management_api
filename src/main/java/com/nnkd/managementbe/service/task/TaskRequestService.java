package com.nnkd.managementbe.service.task;

import com.nnkd.managementbe.dto.request.TaskUpdateRequest;
import com.nnkd.managementbe.model.task.TaskRequest;
import com.nnkd.managementbe.model.task.TaskResponse;
import com.nnkd.managementbe.repository.task.TaskRequestRepository;
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
public class TaskRequestService {
    TaskRequestRepository taskRepository;

    public TaskRequest updateTaskPosition(TaskUpdateRequest request) {
        try {
            TaskRequest taskRequest = taskRepository.findById(request.getId()).get();
            taskRequest.setPosition(request.getPosition());
            return taskRepository.save(taskRequest);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("Task not found: "+request.getId());
        }
    }

    public TaskRequest updateTaskStatusAndPosition(TaskUpdateRequest request) {
        try {
            TaskRequest taskRequest = taskRepository.findById(request.getId()).get();
            taskRequest.setPosition(request.getPosition());
            taskRequest.setStatus(request.getStatus());
            return taskRepository.save(taskRequest);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("Task not found: "+request.getId());
        }
    }

    public TaskRequest updateTaskStatus(TaskUpdateRequest request) {
        try {
            TaskRequest taskRequest = taskRepository.findById(request.getId()).get();
            taskRequest.setStatus(request.getStatus());
            return taskRepository.save(taskRequest);
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("Task not found: "+request.getId());
        }
    }
}
