package com.nnkd.managementbe.service.log;

import com.nnkd.managementbe.model.log.LogResponse;
import com.nnkd.managementbe.repository.log.LogRequestRepository;
import com.nnkd.managementbe.repository.log.LogResponseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LogResponseService {
    LogResponseRepository repository;

    public List<LogResponse> getLogsOfProject(ObjectId id) {
        return repository.findAllByProject(id).stream().toList();
    }
}
