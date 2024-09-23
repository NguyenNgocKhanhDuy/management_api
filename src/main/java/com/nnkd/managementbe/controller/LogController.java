package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.log.LogResponseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LogController {
    LogResponseService logResponseService;
    AuthenticationService authenticationService;

    @GetMapping("/{id}")
    public ApiResponse getLogsOfProject(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(logResponseService.getLogsOfProject(new ObjectId(id)));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }
}
