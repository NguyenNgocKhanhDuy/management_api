package com.nnkd.managementbe.repository.project;

import com.nnkd.managementbe.model.project.ProjectRequest;
import com.nnkd.managementbe.model.project.ProjectResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectResponseRepository extends MongoRepository<ProjectResponse, String> {
    @Query("{ '$or': [ { 'creator': ?0 }, { 'members': ?0 } ] }")
    List<ProjectResponse> findByCreatorOrMembers(ObjectId id);

    @Query("{ '$and': [ { 'name': { '$regex': ?1, '$options': 'i' } }, { '$or': [ { 'creator': ?0 }, { 'members': ?0 } ] } ] }")
    List<ProjectResponse> findByNameContainingAndCreatorOrMembers(ObjectId id, String projectName);

    List<ProjectResponse> findByPending(Object id);


}
