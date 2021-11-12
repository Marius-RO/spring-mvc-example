package com.company.service;

import com.company.dto.RegisterDto;

public interface AccountService {
    void registerUser(RegisterDto registerDto);
    boolean userExists(String email);
}
