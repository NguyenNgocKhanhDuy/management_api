package com.nnkd.managementbe.repository;

import com.nnkd.managementbe.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findUserByEmail(String email);
    List<User> findUserByEmailContaining(String email);

}
