package com.nnkd.managementbe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectUpdateRequest {
    String id;
    String name;
    List<ObjectId> members;
}
