package com.nnkd.managementbe.service;

import com.nnkd.managementbe.dto.request.UserCreationRequest;
import com.nnkd.managementbe.dto.request.UserUpdateRequest;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User save(UserCreationRequest request) {
        User user = new User().builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        return userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(String userId, UserUpdateRequest request){
        User user = getUserById(userId);
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

}
