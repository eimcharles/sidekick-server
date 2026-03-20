package com.eimc.auth;

import java.util.Optional;

/**
 *     ApplicationUserDao provides a contract for
 *     retrieving user authentication and authorization
 *     data from persistent storage.
 * */

public interface ApplicationUserDao {

    Optional<ApplicationUser> selectApplicationUserByUsername(String username);

}
