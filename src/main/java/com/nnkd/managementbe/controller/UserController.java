package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.*;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.MailService;
import com.nnkd.managementbe.service.UserService;
import com.nnkd.managementbe.service.project.ProjectResponseService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;
    MailService mailService;
    AuthenticationService authenticationService;
    ProjectResponseService projectResponseService;

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllUsers());
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse getUserById(@PathVariable("id") String id, @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(userService.getUserById(id));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
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
    public ApiResponse updatePassword(@RequestBody UserUpdateRequest request) {
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

    @GetMapping("/search")
    public ApiResponse searchUser(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String email) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(userService.searchUsersByEmail(email));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @GetMapping("/searchNotInProject")
    public ApiResponse searchUserNotInProject(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String email, String idProject) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            String creator = projectResponseService.getProjectById(idProject).getCreator();
            List<String> except = new ArrayList<>();
            List<String> members = projectResponseService.getProjectById(idProject).getMembers();
            List<String> pending = projectResponseService.getProjectById(idProject).getPending();
            except.add(creator);
            if (members != null) {
                except.addAll(members);
            }
            if (pending != null) {
                except.addAll(pending);
            }
            if (isValid) {
                List<User> all = userService.searchUsersByEmail(email);
                List<User> filter = all.stream().filter(user -> !except.contains(user.getId())).collect(Collectors.toList());
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(filter);
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateAvatar")
    public ApiResponse updateAvatar(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                request.setEmail(authenticationService.getEmailFromextractClaims(token));
                apiResponse.setResult(userService.updateUserAvatar(request));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updatePassAuth")
    public ApiResponse updatePasswordAuth(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                request.setEmail(authenticationService.getEmailFromextractClaims(token));
                apiResponse.setResult(userService.updateUserPassword(request));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateUsername")
    public ApiResponse updateUsername(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                request.setEmail(authenticationService.getEmailFromextractClaims(token));
                apiResponse.setResult(userService.updateUserUsername(request));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }


}
