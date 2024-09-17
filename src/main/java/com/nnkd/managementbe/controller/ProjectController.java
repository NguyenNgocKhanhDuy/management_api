package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.ApiResponse;
import com.nnkd.managementbe.dto.request.MailSendRequest;
import com.nnkd.managementbe.dto.request.ProjectCreationRequest;
import com.nnkd.managementbe.dto.request.ProjectUpdateRequest;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.model.project.ProjectResponse;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.MailService;
import com.nnkd.managementbe.service.project.ProjectRequestService;
import com.nnkd.managementbe.service.UserService;
import com.nnkd.managementbe.service.project.ProjectResponseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectController {
    UserService userService;
    ProjectRequestService projectRequestService;
    ProjectResponseService projectResponseService;
    AuthenticationService authenticationService;
    MailService mailService;

    @GetMapping("/{id}")
    public ApiResponse getProjectById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(projectResponseService.getProjectById(id));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @GetMapping("/projectsHasUser")
    public ApiResponse getProjectsHasUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                String email = authenticationService.getEmailFromextractClaims(token);
                User user = userService.getUser(email);
                ObjectId id = new ObjectId(user.getId());
                apiResponse.setResult(projectResponseService.getProjectsHasUser(id));
                return apiResponse;
            } else {
                throw new RuntimeException("Invalid token");
            }
        } else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PostMapping("/addProject")
    public ApiResponse addProject(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProjectCreationRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                String email = authenticationService.getEmailFromextractClaims(token);
                User user = userService.getUser(email);
                request.setCreator(new ObjectId(user.getId()));
                request.setDate(LocalDateTime.now());
                apiResponse.setResult(projectRequestService.addProject(request));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateName")
    public ApiResponse updateProjectName(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProjectUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(projectRequestService.updateProjectName(request));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateMembers")
    public ApiResponse updateProjectMembers(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProjectUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(projectRequestService.updateProjectMembers(request));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updatePending")
    public ApiResponse updateProjectPending(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProjectUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setResult(projectRequestService.updateProjectPending(request));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteProject(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") String id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                return projectRequestService.deleteProject(id);
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/addMemberPending")
    public ApiResponse addMemberPending(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProjectUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                ProjectResponse projectResponse = projectResponseService.getProjectById(request.getId());
                List<String> pendingOld = projectResponse.getPending();
                List<ObjectId> pendingNew = request.getPending();
                List<ObjectId> filter = new ArrayList<>();

                if (pendingOld != null) {
                     filter = pendingNew.stream().filter(id -> !pendingOld.contains(id.toString())).collect(Collectors.toList());
                }else {
                    filter = pendingNew;
                }
                String subject = "Invitation";
                String text = "Invitation to project: "+projectResponse.getName();
                for (ObjectId id: filter) {
                    User user = userService.getUserById(id.toString());
                    MailSendRequest mail = MailSendRequest.builder().to(user.getEmail()).subject(subject).text(text).build();
                    mailService.sendMail(mail);
                }
                apiResponse.setResult(projectRequestService.updateProjectPending(request));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @GetMapping("/search")
    public ApiResponse search(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String projectName) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                String email = authenticationService.getEmailFromextractClaims(token);
                User user = userService.getUser(email);
                apiResponse.setResult(projectResponseService.searchProjectByName(new ObjectId(user.getId()), projectName));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

}
