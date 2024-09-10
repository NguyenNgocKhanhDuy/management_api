package com.nnkd.managementbe.repository.project;

import com.nnkd.managementbe.model.project.ProjectRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRequestRepository extends MongoRepository<ProjectRequest, String> {
}
