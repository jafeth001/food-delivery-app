package com.backend.foodproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePassword {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}
