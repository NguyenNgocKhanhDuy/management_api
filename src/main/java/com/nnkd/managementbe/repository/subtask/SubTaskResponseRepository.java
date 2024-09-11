package com.nnkd.managementbe.repository.subtask;

import com.nnkd.managementbe.model.subtask.SubTaskRequest;
import com.nnkd.managementbe.model.subtask.SubTaskResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTaskResponseRepository extends MongoRepository<SubTaskResponse, String> {
    List<SubTaskResponse> findByTask(ObjectId id);
}
