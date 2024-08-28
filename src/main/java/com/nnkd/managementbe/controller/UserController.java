package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.UserCreationRequest;
import com.nnkd.managementbe.dto.request.UserUpdateRequest;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUsers());
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.save(request));
        return apiResponse;
    }

    @GetMapping("/{userId}")
    public ApiResponse<User> getUserById(@PathVariable String userId) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUserById(userId));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    public ApiResponse<User> updateUser(@PathVariable String userId, UserUpdateRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userService.updateUser(userId, request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<User> deleteUser(@PathVariable String userId) {
        ApiResponse apiResponse = new ApiResponse();
        userService.deleteUser(userId);
        return apiResponse;
    }
}
