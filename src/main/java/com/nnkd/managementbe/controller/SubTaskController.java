package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.LogCreationRequest;
import com.nnkd.managementbe.dto.request.SubTaskCreationRequest;
import com.nnkd.managementbe.dto.request.SubTaskUpdateRequest;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.model.subtask.SubTaskResponse;
import com.nnkd.managementbe.model.task.TaskResponse;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.UserService;
import com.nnkd.managementbe.service.log.LogRequestService;
import com.nnkd.managementbe.service.subtask.SubTaskRequestService;
import com.nnkd.managementbe.service.subtask.SubTaskResponseService;
import com.nnkd.managementbe.service.task.TaskResponseService;
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
    TaskResponseService taskResponseService;
    LogRequestService logRequestService;
    UserService userService;

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

    @PostMapping("/addSubtask")
    public ApiResponse addSubTask(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SubTaskCreationRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();

                User user = userService.getUser(authenticationService.getEmailFromextractClaims(token));
                TaskResponse taskResponse = taskResponseService.getTaskById(request.getTask().toString());

                String action = "Add Subtask "+request.getTitle() + " in task "+taskResponse.getName();
                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
                        .action(action)
                        .user(new ObjectId(user.getId()))
                        .project(new ObjectId(taskResponse.getProject())).build();
                logRequestService.addLog(logCreationRequest);

                apiResponse.setResult(subTaskRequestService.addSubTask(request));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateSubtaskTitle")
    public ApiResponse updateSubTaskTitle(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SubTaskUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();

                User user = userService.getUser(authenticationService.getEmailFromextractClaims(token));
                SubTaskResponse subTaskResponse = subTaskResponseService.getSubTaskById(request.getId());
                TaskResponse taskResponse = taskResponseService.getTaskById(subTaskResponse.getTask());

                String action = "Update Subtask Name From "+subTaskResponse.getTitle() +" To "+request.getTitle() + " in task "+taskResponse.getName();;

                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
                        .action(action)
                        .user(new ObjectId(user.getId()))
                        .project(new ObjectId(taskResponse.getProject())).build();
                logRequestService.addLog(logCreationRequest);

                apiResponse.setResult(subTaskRequestService.updateSubTaskTitle(request));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateSubtaskStatus")
    public ApiResponse updateSubTaskStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SubTaskUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();

                User user = userService.getUser(authenticationService.getEmailFromextractClaims(token));
                SubTaskResponse subTaskResponse = subTaskResponseService.getSubTaskById(request.getId());
                TaskResponse taskResponse = taskResponseService.getTaskById(subTaskResponse.getTask());
                String action = "";
                if (request.isCompleted()) {
                    action += "Check Subtask "+request.getTitle() + " in task "+taskResponse.getName();
                }else {
                    action += "Uncheck Subtask "+request.getTitle() + " in task "+taskResponse.getName();
                }
                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
                        .action(action)
                        .user(new ObjectId(user.getId()))
                        .project(new ObjectId(taskResponse.getProject())).build();
                logRequestService.addLog(logCreationRequest);


                apiResponse.setResult(subTaskRequestService.updateSubTaskStatus(request));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteSubTask(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                User user = userService.getUser(authenticationService.getEmailFromextractClaims(token));
                SubTaskResponse subTaskResponse = subTaskResponseService.getSubTaskById(id);
                TaskResponse taskResponse = taskResponseService.getTaskById(subTaskResponse.getTask());

                String action = "Delete Subtask "+subTaskResponse.getTitle() + " in task "+taskResponse.getName();;

                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
                        .action(action)
                        .user(new ObjectId(user.getId()))
                        .project(new ObjectId(taskResponse.getProject())).build();
                logRequestService.addLog(logCreationRequest);

                return subTaskRequestService.deleteSubTask(id);
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

}
