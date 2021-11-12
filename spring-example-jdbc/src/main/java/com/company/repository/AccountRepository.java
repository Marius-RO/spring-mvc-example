package com.company.repository;

import com.company.model.UserAccount;

public interface AccountRepository {
    void insertUser(UserAccount userAccount, String... roles);
    boolean userExists(String email);
}
