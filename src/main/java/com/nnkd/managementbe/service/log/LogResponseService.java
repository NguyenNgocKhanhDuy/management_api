package com.nnkd.managementbe.service.log;

import com.nnkd.managementbe.model.log.LogResponse;
import com.nnkd.managementbe.repository.log.LogResponseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<LogResponse> getLogsOfProjectPage(ObjectId id, int page) {
        Pageable pageable = PageRequest.of(page, 50);
        return repository.findAllByProject(id, pageable);
    }


}
