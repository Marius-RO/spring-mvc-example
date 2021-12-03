package com.company.service;

import com.company.model.UserAccount;
import com.company.model.UserActivity;
import com.company.util.Pair;

import java.sql.Timestamp;
import java.util.List;

public interface EmployeeService {
    List<UserAccount> getAllEmployees();
    List<Pair<UserAccount, Timestamp>> getAllEmployeesWithLastActivityTimestamp();
    void deleteEmployeeByEmail(String email);
    List<UserActivity> getEmployeeActivityByEmail(String email);
}
