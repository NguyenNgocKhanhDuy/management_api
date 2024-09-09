package com.nnkd.managementbe.controller;

import com.nimbusds.jose.JOSEException;
import com.nnkd.managementbe.dto.request.*;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.MailService;
import com.nnkd.managementbe.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;
    MailService mailService;
    AuthenticationService authenticationService;

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUsers());
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

    @GetMapping("/user")
    public ApiResponse getUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(userService.getUser(authenticationService.getEmailFromextractClaims(token)));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

}
