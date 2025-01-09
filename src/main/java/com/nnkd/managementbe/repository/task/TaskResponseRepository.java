package com.nnkd.managementbe.repository.task;

import com.nnkd.managementbe.model.project.ProjectResponse;
import com.nnkd.managementbe.model.task.TaskResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TaskResponseRepository extends MongoRepository<TaskResponse, String> {
    List<TaskResponse> findByProject(ObjectId id);
    List<TaskResponse> findByCreator(ObjectId creatorId, Sort sort);
    List<TaskResponse> findByStatus(String status);
    @Query("{ '$and': [ { 'name': { '$regex': ?1, '$options': 'i' } }, { '$or': [ { 'creator': ?0 }, { 'members': ?0 } ] } ] }")
    List<TaskResponse> findByNameContainingAndCreatorOrMembers(ObjectId id, String taskName);
}
