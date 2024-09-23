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

    public LogRequest addLog(LogCreationRequest request){
        LogRequest logRequest = LogRequest.builder()
                .action(request.getAction())
                .user(request.getUser())
                .project(request.getProject()).build();
        return repository.save(logRequest);
    }
}
