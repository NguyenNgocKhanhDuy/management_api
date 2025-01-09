package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.model.log.LogResponse;
import com.nnkd.managementbe.model.subtask.SubTaskResponse;
import com.nnkd.managementbe.model.task.TaskResponse;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.UserService;
import com.nnkd.managementbe.service.log.LogResponseService;
import com.nnkd.managementbe.service.subtask.SubTaskResponseService;
import com.nnkd.managementbe.service.task.TaskResponseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LogController {
    LogResponseService logResponseService;
    AuthenticationService authenticationService;
    TaskResponseService taskResponseService;
    SubTaskResponseService subTaskResponseService;
    UserService userService;

    @GetMapping("/{id}")
    public ApiResponse getLogsOfProject(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();

                List<LogResponse> logs = logResponseService.getLogsOfProject(new ObjectId(id));

                for (LogResponse log : logs) {
                    updateTaskAndSubtaskNames(log);
                }

                apiResponse.setResult(logs);
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    private void updateTaskAndSubtaskNames(LogResponse log) {
        if (log.getUserLog().getId() != null) {
            User user = userService.getUserById(log.getUserLog().getId());
            if (user != null) {
                log.getUserLog().setName(user.getUsername());
            }
        }

        if (log.getTaskLog().getId() != null) {
            TaskResponse task = taskResponseService.getTaskById(log.getTaskLog().getId());
            if (task != null) {
                log.getTaskLog().setName(task.getName());
            }
        }


        if (log.getSubTaskLog().getId() != null) {
            SubTaskResponse subTask = subTaskResponseService.getSubTaskById(log.getSubTaskLog().getId());
            if (subTask != null) {
                log.getSubTaskLog().setName(subTask.getTitle());
            }
        }
    }
}
