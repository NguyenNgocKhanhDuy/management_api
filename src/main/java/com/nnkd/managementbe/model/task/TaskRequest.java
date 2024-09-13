package com.nnkd.managementbe.model.task;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRequest {
    @Id
    String id;
    String name;
    LocalDateTime date;
    ObjectId creator;
    List<ObjectId> members;
    LocalDateTime deadline;
    String status;
    int position;
    ObjectId project;
}
