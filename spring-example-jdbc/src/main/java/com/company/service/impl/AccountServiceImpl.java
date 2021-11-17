package com.company.service.impl;

import com.company.config.WebAppSecurityConfig;
import com.company.dto.ProfileDto;
import com.company.dto.RegisterDto;
import com.company.model.UserAccount;
import com.company.repository.AccountRepository;
import com.company.service.AccountService;
import com.company.service.impl.util.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
public class AccountServiceImpl extends AbstractService implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(PasswordEncoder passwordEncoder, AccountRepository accountRepository,
                              WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    @Override
    protected Class<?> getClassType() {
        return AccountServiceImpl.class;
    }

    @Override
    public void registerUser(RegisterDto registerDto){
        // create user
        UserAccount userAccount = webApplicationContext.getBean(UserAccount.class);
        userAccount.setEmail(registerDto.getEmail());
        userAccount.setPassword(passwordEncoder.encode(String.valueOf(registerDto.getPasswordDto().getPassword())));

        // add user
        accountRepository.insertUser(
                userAccount,
                registerDto.getIsEmployeeCheck() ?
                        WebAppSecurityConfig.Roles.ROLE_EMPLOYEE :
                        WebAppSecurityConfig.Roles.ROLE_CUSTOMER
        );
    }

    @Override
    public boolean userExists(String email) {
        return accountRepository.userExists(email);
    }

    @Nullable
    @Override
    public UserAccount getUser(String email) {
        return accountRepository.getUser(email);
    }

    @Override
    public void updateUser(ProfileDto profileDto, String email) {
        UserAccount userAccount = webApplicationContext.getBean(UserAccount.class);
        userAccount.setEmail(email);
        userAccount.setFirstName(profileDto.getFirstName());
        userAccount.setLastName(profileDto.getLastName());
        userAccount.getAddress().setFullAddress(profileDto.getAddressDto().getFullAddress());
        userAccount.getAddress().setPhone(profileDto.getAddressDto().getPhone());

        accountRepository.updateUser(userAccount);
    }

    @Override
    public boolean tryToUpdatePassword(String oldPassword, String newPassword) {
        return accountRepository.tryToUpdatePassword(oldPassword, newPassword);
    }

    @Override
    public void deleteUser(String email) {
        accountRepository.deleteUser(email);
    }
}
