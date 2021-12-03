package com.company.service.impl;

import com.company.model.UserAccount;
import com.company.model.UserActivity;
import com.company.repository.EmployeeRepository;
import com.company.service.EmployeeService;
import com.company.service.impl.util.AbstractService;
import com.company.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.List;

@Service
public class EmployeeServiceImpl extends AbstractService implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(WebApplicationContext webApplicationContext, EmployeeRepository employeeRepository) {
        super(webApplicationContext);
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected Class<?> getClassType() {
        return EmployeeServiceImpl.class;
    }

    @Override
    public List<UserAccount> getAllEmployees(){
        return employeeRepository.getAllEmployees();
    }

    @Override
    public List<Pair<UserAccount, Timestamp>> getAllEmployeesWithLastActivityTimestamp() {
        return employeeRepository.getAllEmployeesWithLastActivityTimestamp();
    }

    @Override
    public void deleteEmployeeByEmail(String email) {
        employeeRepository.deleteEmployeeByEmail(email);
    }

    @Override
    public List<UserActivity> getEmployeeActivityByEmail(String email) {
        return employeeRepository.getEmployeeActivityByEmail(email);
    }
}
