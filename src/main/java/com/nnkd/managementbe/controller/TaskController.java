package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.*;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.model.log.Action;
import com.nnkd.managementbe.model.log.TaskLog;
import com.nnkd.managementbe.model.log.UserLog;
import com.nnkd.managementbe.model.project.ProjectResponse;
import com.nnkd.managementbe.model.task.TaskResponse;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.MailService;
import com.nnkd.managementbe.service.UserService;
import com.nnkd.managementbe.service.log.LogRequestService;
import com.nnkd.managementbe.service.project.ProjectResponseService;
import com.nnkd.managementbe.service.task.TaskRequestService;
import com.nnkd.managementbe.service.task.TaskResponseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
    TaskResponseService taskResponseService;
    TaskRequestService  taskRequestService;
    AuthenticationService authenticationService;
    UserService userService;
    LogRequestService logRequestService;
    ProjectResponseService projectResponseService;
    MailService mailService;

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

    @PostMapping("/addTask")
    public ApiResponse addTask(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TaskCreationRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                String email = authenticationService.getEmailFromextractClaims(token);
                User user = userService.getUser(email);
                int position = taskResponseService.getTaskByStatus("todo").size();

//                String action = "Add Task "+request.getName();
//                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
//                        .action(action)
//                        .user(new ObjectId(user.getId()))
//                        .project(request.getProject()).build();
//                logRequestService.addLog(logCreationRequest);


                apiResponse.setResult(taskRequestService.addTask(request, new ObjectId(user.getId()), position));

                TaskResponse taskResponse = taskResponseService.getNewTask(user.getId());

                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
                        .action(Action.ADD_TASK.getDescription())
                        .userLog(UserLog.builder().id(user.getId()).build())
                        .taskLog(TaskLog.builder().id(taskResponse.getId()).build())
                        .project(request.getProject()).build();

                logRequestService.logTask(logCreationRequest);


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
                User user = userService.getUser(authenticationService.getEmailFromextractClaims(token));

                try {
                    for (TaskUpdateRequest re : request) {
                        TaskResponse taskResponse = taskResponseService.getTaskById(re.getId());
                        if (!taskResponse.getStatus().toString().trim().equals(re.getStatus().toString().trim())) {

                            LogCreationRequest logCreationRequest = LogCreationRequest.builder()
                                    .action(Action.MOVE_TASK.getDescription())
                                    .userLog(UserLog.builder().id(user.getId()).build())
                                    .taskLog(TaskLog.builder().id(re.getId()).status(re.getStatus().toString().trim()).build())
                                    .project(new ObjectId(taskResponse.getProject())).build();
                            logRequestService.logTask(logCreationRequest);

//                            String action = "Move Task \""+taskResponse.getName() +"\" From "+taskResponse.getStatus()+" To "+re.getStatus();
//                            LogCreationRequest logCreationRequest = LogCreationRequest.builder()
//                                    .action(action)
//                                    .user(new ObjectId(user.getId()))
//                                    .project(new ObjectId(taskResponse.getProject())).build();
//                            logRequestService.addLog(logCreationRequest);
                        }
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

////    @PutMapping("/updateTaskStatus")
////    public ApiResponse updateTaskStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TaskUpdateRequest request) {
////        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
////            String token = authorizationHeader.substring(7);
////            boolean isValid = authenticationService.verifyToken(token);
////            if (isValid) {
////                ApiResponse apiResponse = new ApiResponse();
////                apiResponse.setResult(taskRequestService.updateTaskStatus(request));
////                return apiResponse;
////            }else {
////                throw new RuntimeException("Invalid token");
////            }
////        }else {
////            throw new RuntimeException("Authorization header is missing or malformed");
////        }
//    }

    @PutMapping("/updateTaskName")
    public ApiResponse updateTaskName(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TaskUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                User user = userService.getUser(authenticationService.getEmailFromextractClaims(token));

                TaskResponse taskResponse = taskResponseService.getTaskById(request.getId());

//                String action = "Update Task Name From "+request.getName() +" To "+taskResponse.getName();
//                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
//                        .action(action)
//                        .user(new ObjectId(user.getId()))
//                        .project(new ObjectId(taskResponse.getProject())).build();
//                logRequestService.addLog(logCreationRequest);

                apiResponse.setResult(taskRequestService.updateTaskName(request));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateTaskDeadline")
    public ApiResponse updateTaskDeadline(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TaskUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                User user = userService.getUser(authenticationService.getEmailFromextractClaims(token));

                TaskResponse taskResponse = taskResponseService.getTaskById(request.getId());
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy, HH:mm")
//                        .withZone(ZoneId.of("UTC"));

                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
                        .action(Action.CHANGE_DEADLINE.getDescription())
                        .userLog(UserLog.builder().id(user.getId()).build())
                        .taskLog(TaskLog.builder().id(taskResponse.getId()).build())
                        .project(new ObjectId(taskResponse.getProject())).build();
                logRequestService.logTask(logCreationRequest);

//                String action = "Update Task \""+ taskResponse.getName() +"\" Deadline To "+formatter.format(request.getDeadline());
//                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
//                        .action(action)
//                        .user(new ObjectId(user.getId()))
//                        .project(new ObjectId(taskResponse.getProject())).build();
//                logRequestService.addLog(logCreationRequest);

                apiResponse.setResult(taskRequestService.updateTaskDeadline(request));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteTask(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                User user = userService.getUser(authenticationService.getEmailFromextractClaims(token));
                TaskResponse taskResponse = taskResponseService.getTaskById(id);

                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
                        .action(Action.DELETE_TASK.getDescription())
                        .userLog(UserLog.builder().id(user.getId()).build())
                        .taskLog(TaskLog.builder().id(id).build())
                        .project(new ObjectId(taskResponse.getProject())).build();
                logRequestService.logTask(logCreationRequest);

//                String action = "Delete Task "+taskResponse.getName();
//                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
//                        .action(action)
//                        .user(new ObjectId(user.getId()))
//                        .project(new ObjectId(taskResponse.getProject())).build();
//                logRequestService.addLog(logCreationRequest);

                return taskRequestService.deleteTask(id);
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @GetMapping("/search")
    public ApiResponse search(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String taskName) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                String email = authenticationService.getEmailFromextractClaims(token);
                User user = userService.getUser(email);
                apiResponse.setResult(taskResponseService.searchTaskByName(new ObjectId(user.getId()), taskName));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkTaskDeadline() {
        List<TaskResponse> tasks = taskResponseService.getAlls();

        Instant nowUtc = Instant.now();
        LocalDateTime nowLocal = LocalDateTime.ofInstant(nowUtc, ZoneId.of("UTC+7"));


        for (TaskResponse task : tasks) {
            if (!task.isSend() && task.getDeadline() != null) {
                System.out.println(task);
                Instant deadline = task.getDeadline();

                if (deadline.minus(Duration.ofHours(12)).isBefore(nowLocal.toInstant(ZoneOffset.UTC)) && !task.isSend()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy, HH:mm")
                            .withZone(ZoneId.of("UTC"));

                    ProjectResponse project = projectResponseService.getProjectById(task.getProject());
                    User user = userService.getUserById(task.getCreator());

                    String subject = String.format("Reminder: Task \"%s\" Deadline Approaching", task.getName());


                    String text = String.format("Dear %s,\n\nThis is a reminder that the deadline for your task \"%s\" is approaching in less than 12 hours.\n\n" +
                                    "**Task in Project:** %s\n" +
                                    "- **Task Name:** %s\n" +
                                    "- **Deadline:** %s\n\n" +
                                    "Please ensure that you complete the task before the deadline.\n\n" +
                                    "Best regards,\nNNKD",
                            user.getUsername(), task.getName(), project.getName(), task.getName(), formatter.format(deadline));

                    MailSendRequest mailSendRequest = MailSendRequest
                            .builder()
                            .to(user.getEmail()).text(text).subject(subject).build();

                    mailService.sendMail(mailSendRequest);
                    taskRequestService.updateSendMailDeadline(task.getId());
                }
            }
        }
    }



}
