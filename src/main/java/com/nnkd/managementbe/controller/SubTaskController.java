package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.subtask.SubTaskRequestService;
import com.nnkd.managementbe.service.subtask.SubTaskResponseService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subtasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubTaskController {
    SubTaskResponseService subTaskResponseService;
    SubTaskRequestService subTaskRequestService;
    AuthenticationService authenticationService;

    @GetMapping("/{idTask}")
    public ApiResponse getSubTasksOfTask(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("idTask") String idTask) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(subTaskResponseService.getSubTasksOfTask(new ObjectId(idTask)));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }
}
