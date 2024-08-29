package com.nnkd.managementbe.repository;

import com.nnkd.managementbe.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Override
    Optional<User> findById(String id);
    boolean existsByEmail(String email);
}
