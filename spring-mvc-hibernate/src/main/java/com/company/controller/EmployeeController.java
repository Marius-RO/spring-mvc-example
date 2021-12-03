package com.company.controller;

import com.company.controller.util.AbstractController;
import com.company.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

@Controller
@RequestMapping(path = EmployeeController.PathHandler.BASE_URL)
public class EmployeeController extends AbstractController {

    public interface ViewHandler {
        String EMPLOYEES = "employees";
        String EMPLOYEE_ACTIVITIES = "employee-activity";
    }

    public interface PathHandler {
        String BASE_URL = "/employees";
        String EMPLOYEES_URL = "/all";
        String FULL_EMPLOYEES_URL = BASE_URL + EMPLOYEES_URL;
        String PROCESS_DELETE_EMPLOYEE_URL = "/process-delete";
        String EMPLOYEE_ACTIVITY_URL = "/activity";
    }

    public interface Security {
        String[] PERMIT_ONLY_ADMIN = {
                PathHandler.BASE_URL + "/**",
        };
    }

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(WebApplicationContext webApplicationContext, EmployeeService employeeService) {
        super(webApplicationContext);
        this.employeeService = employeeService;
    }

    @Override
    protected Class<?> getClassType() {
        return EmployeeController.class;
    }

    @RequestMapping(path = PathHandler.EMPLOYEES_URL, method = RequestMethod.GET)
    public String getEmployeesPage(Model model){
        model.addAttribute("allEmployeesList", employeeService.getAllEmployeesWithLastActivityTimestamp());
        return ViewHandler.EMPLOYEES;
    }

    @RequestMapping(path = PathHandler.PROCESS_DELETE_EMPLOYEE_URL, method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<String> processDeleteEmployee(@RequestParam("email") String email){
        employeeService.deleteEmployeeByEmail(email);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @RequestMapping(path = PathHandler.EMPLOYEE_ACTIVITY_URL, method = RequestMethod.GET)
    public String getEmployeeActivityPage(@RequestParam("email") String email, Model model){
        model.addAttribute("employeeActivityList", employeeService.getEmployeeActivityByEmail(email));
        return ViewHandler.EMPLOYEE_ACTIVITIES;
    }
}
