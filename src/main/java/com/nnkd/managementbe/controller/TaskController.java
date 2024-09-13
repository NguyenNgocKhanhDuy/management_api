package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.TaskUpdateRequest;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.task.TaskRequestService;
import com.nnkd.managementbe.service.task.TaskResponseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
    TaskResponseService taskResponseService;
    TaskRequestService  taskRequestService;
    AuthenticationService authenticationService;

    @GetMapping("/tasksOfProject/{idProject}")
    public ApiResponse getTasksOfProject(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("idProject") String idProject) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                ObjectId id = new ObjectId(idProject);
                apiResponse.setResult(taskResponseService.getTasksOfProject(id));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @GetMapping("/{id}")
    public ApiResponse getTaskById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(taskResponseService.getTaskById(id));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateTaskStatusAndPosition")
    public ApiResponse updateTaskStatusAndPosition(@RequestHeader("Authorization") String authorizationHeader, @RequestBody List<TaskUpdateRequest> request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                try {
                    for (TaskUpdateRequest re : request) {
                        taskRequestService.updateTaskStatusAndPosition(re);
                    }
                    apiResponse.setStatus(true);
                    apiResponse.setResult("Task positions updated successfully");
                } catch (Exception e) {
                    apiResponse.setStatus(false);
                    apiResponse.setMessage("Error updating task positions: " + e.getMessage());
                }
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateTaskStatus")
    public ApiResponse updateTaskStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TaskUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(taskRequestService.updateTaskStatus(request));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

}
