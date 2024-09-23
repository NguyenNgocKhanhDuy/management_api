package com.nnkd.managementbe.repository.log;

import com.nnkd.managementbe.model.log.LogRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRequestRepository extends MongoRepository<LogRequest, String> {

}
