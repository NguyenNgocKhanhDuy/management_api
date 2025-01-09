package com.nnkd.managementbe.service.log;

import com.nnkd.managementbe.dto.request.LogCreationRequest;
import com.nnkd.managementbe.model.log.LogRequest;
import com.nnkd.managementbe.repository.log.LogRequestRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LogRequestService {
    LogRequestRepository repository;

    public LogRequest logTask(LogCreationRequest request){
        LogRequest logRequest = LogRequest.builder()
                .action(request.getAction())
                .userLog(request.getUserLog())
                .taskLog(request.getTaskLog())
                .subTaskLog(null)
                .dateTime(LocalDateTime.now())
                .project(request.getProject()).build();
        return repository.save(logRequest);
    }

    public LogRequest logSubTask(LogCreationRequest request){
        LogRequest logRequest = LogRequest.builder()
                .action(request.getAction())
                .userLog(request.getUserLog())
                .taskLog(null)
                .subTaskLog(request.getSubTaskLog())
                .dateTime(LocalDateTime.now())
                .project(request.getProject()).build();
        return repository.save(logRequest);
    }

    public LogRequest logAddUser(LogCreationRequest request){
        LogRequest logRequest = LogRequest.builder()
                .action(request.getAction())
                .userLog(request.getUserLog())
                .taskLog(null)
                .subTaskLog(null)
                .dateTime(LocalDateTime.now())
                .project(request.getProject()).build();
        return repository.save(logRequest);
    }


}
