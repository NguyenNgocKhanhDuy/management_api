package com.nnkd.managementbe.dto.request;

import com.nnkd.managementbe.model.log.SubTaskLog;
import com.nnkd.managementbe.model.log.TaskLog;
import com.nnkd.managementbe.model.log.UserLog;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogCreationRequest {
    String action;
    TaskLog taskLog;
    SubTaskLog subTaskLog;
    UserLog userLog;
    ObjectId project;
}
