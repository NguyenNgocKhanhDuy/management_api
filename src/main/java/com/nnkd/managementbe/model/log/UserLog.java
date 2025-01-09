package com.nnkd.managementbe.model.log;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLog {
    @Id
    String id;
    String name;
}
