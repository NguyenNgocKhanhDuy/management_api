package com.nnkd.managementbe.repository.task;

import com.nnkd.managementbe.model.task.TaskRequest;
import com.nnkd.managementbe.model.task.TaskResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRequestRepository extends MongoRepository<TaskRequest, String> {
}
