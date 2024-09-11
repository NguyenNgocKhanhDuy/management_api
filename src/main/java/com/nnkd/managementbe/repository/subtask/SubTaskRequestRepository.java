package com.nnkd.managementbe.repository.subtask;

import com.nnkd.managementbe.model.subtask.SubTaskRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTaskRequestRepository extends MongoRepository<SubTaskRequest, String> {
}
