package com.nnkd.managementbe.model.log;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogResponse {
    @Id
    String id;
    String action;
    TaskLog taskLog;
    SubTaskLog subTaskLog;
    UserLog userLog;
    LocalDateTime dateTime;
    String project;
}
