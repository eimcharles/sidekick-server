package com.eimc.employee.dto;

public record EmployeePasswordUpdateDTO (

        String oldPassword,
        String newPassword,
        String newPasswordConfirmed

) {
}
