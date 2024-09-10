package com.nnkd.managementbe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectCreationRequest {
    String name;
    LocalDateTime date;
    ObjectId creator;
}
