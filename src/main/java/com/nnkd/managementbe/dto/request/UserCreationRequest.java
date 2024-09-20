package com.nnkd.managementbe.dto.request;

import com.nnkd.managementbe.validator.PasswordConstraint;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Email(message = "Invalid email format")
    String email;
    String username;
    @PasswordConstraint
    String password;
    String avatar;
}
