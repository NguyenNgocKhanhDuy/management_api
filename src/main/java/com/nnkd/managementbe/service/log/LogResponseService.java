package com.nnkd.managementbe.service.log;

import com.nnkd.managementbe.model.User;
import com.nnkd.managementbe.model.log.LogResponse;
import com.nnkd.managementbe.model.subtask.SubTaskResponse;
import com.nnkd.managementbe.model.task.TaskResponse;
import com.nnkd.managementbe.repository.log.LogRequestRepository;
import com.nnkd.managementbe.repository.log.LogResponseRepository;
import com.nnkd.managementbe.repository.task.TaskResponseRepository;
import com.nnkd.managementbe.service.UserService;
import com.nnkd.managementbe.service.subtask.SubTaskResponseService;
import com.nnkd.managementbe.service.task.TaskResponseService;
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
