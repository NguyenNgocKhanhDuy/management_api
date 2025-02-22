package com.nnkd.managementbe.controller;

import com.nnkd.managementbe.dto.request.*;
import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.model.project.ProjectRequest;
import com.nnkd.managementbe.model.project.ProjectResponse;
import com.nnkd.managementbe.service.AuthenticationService;
import com.nnkd.managementbe.service.MailService;
import com.nnkd.managementbe.service.log.LogRequestService;
import com.nnkd.managementbe.service.project.ProjectRequestService;
import com.nnkd.managementbe.service.UserService;
import com.nnkd.managementbe.service.project.ProjectResponseService;
import com.nnkd.managementbe.utils.TokenUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
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
    LogRequestService logRequestService;

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

    @GetMapping({"/id"})
    public ApiResponse getProjectByIdNoToken(@RequestParam String token) {
        ApiResponse apiResponse = new ApiResponse();
        String tokenDecode = TokenUtils.decodeToken(token);
        StringTokenizer tokenizer = new StringTokenizer(tokenDecode, "-");
        String idProject = tokenizer.nextToken();
        String idUser = tokenizer.nextToken();
        ProjectResponse projectResponse = projectResponseService.getProjectById(idProject);
        boolean hasInvite = projectResponse.getPending().contains(idUser);
        if (!hasInvite) {
            projectResponse.setPending(null);
        }
        apiResponse.setResult(projectResponse);
        return apiResponse;
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

    @GetMapping("/projectHasPendingUser")
    public ApiResponse projectHasPendingUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                String email = authenticationService.getEmailFromextractClaims(token);
                User user = userService.getUser(email);
                apiResponse.setResult(projectResponseService.getProjectHasPendingUser(new ObjectId(user.getId())));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/updateName")
    public ApiResponse updateProjectName(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProjectUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
//                User user = userService.getUser(authenticationService.getEmailFromextractClaims(token));
                ApiResponse apiResponse = new ApiResponse();
//                String action = "Updated project name to " + request.getName();
//                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
//                        .action(action)
//                        .user(new ObjectId(user.getId()))
//                        .project(new ObjectId(request.getId())).build();
//                logRequestService.addLog(logCreationRequest);
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

    @PutMapping("/addUserIntoPending")
    public ApiResponse addUserIntoPending(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProjectUpdateRequest request) {
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
                User creator = userService.getUserById(projectResponse.getCreator());
                String subject = "Invitation";
                String text = "Invitation to project: "+projectResponse.getName()+" from: "+creator.getUsername()+" ("+creator.getEmail()+")";
                text += "\nPlease use application to confirm or link below\n";
                for (ObjectId id: filter) {
                    String tokenText = projectResponse.getId()+"-"+id.toString();
                    text += "https://nnkd-management.onrender.com?invite=yes&token="+TokenUtils.encodeToken(tokenText);
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

    @PutMapping("/userAcceptPending")
    public ApiResponse userAcceptPending(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProjectUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                ProjectResponse projectResponse = projectResponseService.getProjectById(request.getId());

                List<String> pendingOld = projectResponse.getPending();
                String email = authenticationService.getEmailFromextractClaims(token);

                User user = userService.getUser(email);

                List<ObjectId> pendingOldObjectId = pendingOld.stream().map(id -> new ObjectId(id)).collect(Collectors.toList());
                List<ObjectId> pendingNew = pendingOldObjectId.stream().filter(id -> !id.toString().equals(user.getId())).collect(Collectors.toList());

                List<String> members = projectResponse.getMembers();

                List<ObjectId> membersObjectId = new ArrayList<>();
                if (members != null) {
                    membersObjectId = members.stream().map(id -> new ObjectId(id)).collect(Collectors.toList());
                }

                membersObjectId.add(new ObjectId(user.getId()));
                request.setMembers(membersObjectId);
                request.setPending(pendingNew);

                ProjectRequest updatePending = projectRequestService.updateProjectPending(request);
                ProjectRequest updateMember = projectRequestService.updateProjectMembers(request);

//                String action = "Joined the project";
//                LogCreationRequest logCreationRequest = LogCreationRequest.builder()
//                        .action(action)
//                        .user(new ObjectId(user.getId()))
//                        .project(new ObjectId(request.getId())).build();
//                logRequestService.addLog(logCreationRequest);

                apiResponse.setResult(updatePending != null && updateMember != null);
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/userAcceptPendingURL")
    public ApiResponse userAcceptPendingURL(@RequestParam String token) {
        ApiResponse apiResponse = new ApiResponse();
        String tokenDecode = TokenUtils.decodeToken(token);
        StringTokenizer tokenizer = new StringTokenizer(tokenDecode, "-");
        String idProject = tokenizer.nextToken();
        String idUser = tokenizer.nextToken();
        ProjectResponse projectResponse = projectResponseService.getProjectById(idProject);

        List<String> pendingOld = projectResponse.getPending();

        User user = userService.getUserById(idUser);

        List<ObjectId> pendingOldObjectId = pendingOld.stream().map(id -> new ObjectId(id)).collect(Collectors.toList());
        List<ObjectId> pendingNew = pendingOldObjectId.stream().filter(id -> !id.toString().equals(user.getId())).collect(Collectors.toList());

        List<String> members = projectResponse.getMembers();

        List<ObjectId> membersObjectId = new ArrayList<>();
        if (members != null) {
            membersObjectId = members.stream().map(id -> new ObjectId(id)).collect(Collectors.toList());
        }

        membersObjectId.add(new ObjectId(user.getId()));

        ProjectUpdateRequest projectUpdateRequest = ProjectUpdateRequest.builder()
                .id(idProject).members(membersObjectId).pending(pendingNew).build();

        ProjectRequest updatePending = projectRequestService.updateProjectPending(projectUpdateRequest);
        ProjectRequest updateMember = projectRequestService.updateProjectMembers(projectUpdateRequest);

        apiResponse.setResult(updatePending != null && updateMember != null);
        return apiResponse;
    }



    @PutMapping("/userRejectPending")
    public ApiResponse userRejectPending(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProjectUpdateRequest request) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            boolean isValid = authenticationService.verifyToken(token);
            if (isValid) {
                ApiResponse apiResponse = new ApiResponse();
                ProjectResponse projectResponse = projectResponseService.getProjectById(request.getId());
                List<String> pendingOld = projectResponse.getPending();
                String email = authenticationService.getEmailFromextractClaims(token);
                User user = userService.getUser(email);

                List<ObjectId> pendingOldObjectId = pendingOld.stream().map(id -> new ObjectId(id)).collect(Collectors.toList());
                List<ObjectId> pendingNew = pendingOldObjectId.stream().filter(id -> !id.toString().equals(user.getId())).collect(Collectors.toList());
                request.setPending(pendingNew);

                User creator = userService.getUserById(projectResponse.getCreator());
                String subject = "Reject invitation";

                String text = "User: "+user.getUsername()+" ("+email+")"+", rejected your invitation";

                MailSendRequest mail = MailSendRequest.builder().to(creator.getEmail()).subject(subject).text(text).build();
                mailService.sendMail(mail);
                apiResponse.setResult(projectRequestService.updateProjectPending(request));
                return apiResponse;
            }else {
                throw new RuntimeException("Invalid token");
            }
        }else  {
            throw new RuntimeException("Authorization header is missing or malformed");
        }
    }

    @PutMapping("/userRejectPendingURL")
    public ApiResponse userRejectPendingURL(@RequestParam String token) {
        ApiResponse apiResponse = new ApiResponse();
        String tokenDecode = TokenUtils.decodeToken(token);
        StringTokenizer tokenizer = new StringTokenizer(tokenDecode, "-");
        String idProject = tokenizer.nextToken();
        String idUser = tokenizer.nextToken();

        ProjectResponse projectResponse = projectResponseService.getProjectById(idProject);
        List<String> pendingOld = projectResponse.getPending();

        User user = userService.getUserById(idUser);

        List<ObjectId> pendingOldObjectId = pendingOld.stream().map(id -> new ObjectId(id)).collect(Collectors.toList());
        List<ObjectId> pendingNew = pendingOldObjectId.stream().filter(id -> !id.toString().equals(user.getId())).collect(Collectors.toList());

        ProjectUpdateRequest projectUpdateRequest = ProjectUpdateRequest.builder()
                .id(idProject).pending(pendingNew).build();

        User creator = userService.getUserById(projectResponse.getCreator());
        String subject = "Reject invitation";

        String text = "User: "+user.getUsername()+" ("+user.getEmail()+")"+", rejected your invitation";

        MailSendRequest mail = MailSendRequest.builder().to(creator.getEmail()).subject(subject).text(text).build();
        mailService.sendMail(mail);

        apiResponse.setResult(projectRequestService.updateProjectPending(projectUpdateRequest));
        return apiResponse;
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
