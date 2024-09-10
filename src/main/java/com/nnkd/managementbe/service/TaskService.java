package com.nnkd.managementbe.service;

import com.nnkd.managementbe.model.Task;
import com.nnkd.managementbe.repository.TaskRepository;
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
public class TaskService {
    TaskRepository taskRepository;

    public List<Task> getTasksOfProject(ObjectId id) {
        return taskRepository.findByProject(id);
    }
}
