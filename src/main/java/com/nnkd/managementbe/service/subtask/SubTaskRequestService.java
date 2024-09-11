package com.nnkd.managementbe.service.subtask;

import com.nnkd.managementbe.repository.subtask.SubTaskRequestRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubTaskRequestService {
    SubTaskRequestRepository subTaskRequestRepository;
}
