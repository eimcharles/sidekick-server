package com.eimc.employee.mapper;

import com.eimc.employee.dto.response.EmployeeAdminResponse;
import com.eimc.employee.dto.response.EmployeeResponse;
import com.eimc.employee.dto.response.ProfileView;
import com.eimc.employee.model.Employee;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProfileMapper {

    /**
     *      mapToDTO is responsible for
     *      returning a ProfileView DTO
     *      based on user authorities.
     * */

    public ProfileView mapToDTO(Employee employee, Authentication authentication) {

        boolean isAdmin = authentication
                .getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return EmployeeAdminResponse.mapToResponse(employee);
        }

        return EmployeeResponse.mapToResponse(employee);
    }

}
