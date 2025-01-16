package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.AuthenticationRequest;
import com.nnkd.managementbe.dto.response.AuthenticationResponse;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.model.project.ProjectResponse;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.UserService;
import com.nnkd.managementbe.service.project.ProjectResponseService;
import com.nnkd.managementbe.utils.TokenUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.StringTokenizer;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }


}
