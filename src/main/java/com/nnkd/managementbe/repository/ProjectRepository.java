package com.nnkd.managementbe.repository;

import com.nnkd.managementbe.model.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    @Query("{ '$or': [ { 'creator': ?0 }, { 'members': ?0 } ] }")
    List<Project> findByCreatorOrMembers(ObjectId id);
}
