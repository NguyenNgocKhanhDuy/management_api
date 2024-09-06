package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.*;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.service.MailService;
import com.nnkd.managementbe.service.UserService;
import jakarta.validation.Valid;
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
    MailService mailService;

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUsers());
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<User> createUser(@Valid @RequestBody UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.save(request));
        return apiResponse;
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody UserCreationRequest request) {
        String code = userService.randomCodeVerify();
        User user = userService.register(request, code);
        ApiResponse apiResponse = mailService.sendCodeVerify(request, code);
        apiResponse.setStatus(user != null && apiResponse.isStatus());
        return apiResponse;
    }

    @PostMapping("/verifyCode")
    public ApiResponse verifyCode(@RequestBody VerifyCodeRequest request) {
        return userService.verifyCode(request);
    }

    @PostMapping("/sendCodeToUser")
    public ApiResponse sendCodeToUser(@RequestBody VerifyCodeRequest request) {
        String code = userService.randomCodeVerify();
        User user = userService.sendCodeToUser(request.getEmail(), code);
        ApiResponse apiResponse = mailService.sendCodeVerify(UserCreationRequest.builder().email(request.getEmail()).build(), code);
        apiResponse.setStatus(user != null && apiResponse.isStatus());
        return apiResponse;
    }

    @PostMapping("/updatePassword")
    public ApiResponse userUpdatePassword(@RequestBody UserUpdatePasswordRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userService.updateUserPassword(request));
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
