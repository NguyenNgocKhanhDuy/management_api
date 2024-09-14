package com.nnkd.managementbe.repository.task;

import com.nnkd.managementbe.model.task.TaskResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskResponseRepository extends MongoRepository<TaskResponse, String> {
    List<TaskResponse> findByProject(ObjectId id);
    List<TaskResponse> findByStatus(String status);
}
