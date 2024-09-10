package com.nnkd.managementbe.repository;

import com.nnkd.managementbe.model.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByProject(ObjectId id);
}
