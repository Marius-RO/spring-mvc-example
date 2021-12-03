package com.company.repository;

import com.company.model.UserAccount;
import org.springframework.lang.Nullable;

public interface AccountRepository {
    void insertUser(UserAccount userAccount, String... roles);
    boolean userExists(String email);
    @Nullable
    UserAccount getUser(String email);
    void updateUser(UserAccount userAccount);
    boolean tryToUpdatePassword(String oldPassword, String newPassword);
    void deleteUser(String email);
}
