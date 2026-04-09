package com.eimc.employee.dto.request;

public record PasswordUpdateRequest(

        String oldPassword,
        String newPassword,
        String newPasswordConfirmed

) {
}
