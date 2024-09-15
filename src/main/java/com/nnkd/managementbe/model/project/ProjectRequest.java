package com.nnkd.managementbe.model.project;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "project")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectRequest {
    @Id
    String id;
    String name;
    LocalDateTime date;
    ObjectId creator;
    List<ObjectId> members;
    List<ObjectId> pending;
}
