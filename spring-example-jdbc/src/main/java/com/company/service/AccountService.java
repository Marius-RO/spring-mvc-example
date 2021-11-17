package com.company.service;

import com.company.dto.ProfileDto;
import com.company.dto.RegisterDto;
import com.company.model.UserAccount;
import org.springframework.lang.Nullable;

public interface AccountService {
    void registerUser(RegisterDto registerDto);
    boolean userExists(String email);
    @Nullable
    UserAccount getUser(String email);
    void updateUser(ProfileDto profileDto, String email);
    boolean tryToUpdatePassword(String oldPassword, String newPassword);
    void deleteUser(String email);
}
