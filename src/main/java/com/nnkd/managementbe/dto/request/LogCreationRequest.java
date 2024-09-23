package com.nnkd.managementbe.dto.request;

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
    ObjectId user;
    LocalDateTime dateTime;
    ObjectId project;
}
