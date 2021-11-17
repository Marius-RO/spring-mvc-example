package com.company.controller;

import com.company.controller.util.AbstractController;
import com.company.dto.AddressDto;
import com.company.dto.ChangePasswordDto;
import com.company.dto.ProfileDto;
import com.company.dto.RegisterDto;
import com.company.model.UserAccount;
import com.company.service.AccountService;
import com.company.util.GeneralUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(path = AccountController.PathHandler.BASE_URL)
@SessionAttributes(names = {"profileDto"})
public class AccountController extends AbstractController {

    public interface ViewHandler {
        String LOGIN = "login";
        String REGISTER = "register";
        String PROFILE = "profile";
    }

    public interface PathHandler {
        String BASE_URL = "/account";
        String LOGIN_URL = "/login";
        String FULL_LOGIN_URL = BASE_URL + LOGIN_URL;
        String REGISTER_URL = "/register";
        String FULL_REGISTER_URL = BASE_URL + REGISTER_URL;
        String PROCESS_REGISTER_URL = "/process-register";
        String FULL_PROCESS_REGISTER_URL = BASE_URL + PROCESS_REGISTER_URL;
        String PROFILE_URL = "/profile";
        String FULL_PROFILE_URL = BASE_URL + PROFILE_URL;
        String PROCESS_UPDATE_URL = "/process-update";
        String FULL_PROCESS_UPDATE_URL = BASE_URL + PROCESS_UPDATE_URL;
        String PROCESS_UPDATE_PASSWORD_URL = "/process-update-password";
        String FULL_PROCESS_UPDATE_PASSWORD_URL = BASE_URL + PROCESS_UPDATE_PASSWORD_URL;
        String PROCESS_DELETE_URL = "/process-delete";
        String FULL_PROCESS_DELETE_URL = BASE_URL + PROCESS_DELETE_URL;
    }

    public interface Security {
        String[] PERMIT_ALL = {
                PathHandler.FULL_LOGIN_URL,
                PathHandler.FULL_REGISTER_URL,
                PathHandler.FULL_PROCESS_REGISTER_URL
        };

        String[] PERMIT_ONLY_CUSTOMER_EMPLOYEE = {
                PathHandler.FULL_PROCESS_DELETE_URL
        };

        String[] PERMIT_ONLY_CUSTOMER_EMPLOYEE_ADMIN = {
                PathHandler.FULL_PROFILE_URL,
                PathHandler.FULL_PROCESS_UPDATE_URL,
                PathHandler.FULL_PROCESS_UPDATE_PASSWORD_URL
        };
    }

    private final AccountService accountService;

    @Autowired
    public AccountController(WebApplicationContext webApplicationContext, AccountService accountService) {
        super(webApplicationContext);
        this.accountService = accountService;
    }

    @Override
    protected Class<?> getClassType() {
        return AccountController.class;
    }

    @RequestMapping(path = PathHandler.LOGIN_URL, method = RequestMethod.GET)
    public String getLoginPage(){
        return ViewHandler.LOGIN;
    }

    @RequestMapping(path = PathHandler.REGISTER_URL, method = RequestMethod.GET)
    public String getRegisterPage(@ModelAttribute("registerDto") RegisterDto registerDto){
        return ViewHandler.REGISTER;
    }

    @ModelAttribute("profileDto")
    private ProfileDto getProfileDto(){
        return new ProfileDto();
    }

    @RequestMapping(path = PathHandler.PROFILE_URL, method = RequestMethod.GET)
    public String getProfilePage(Principal principal,
                                 @ModelAttribute("profileDto") ProfileDto profileDto,
                                 Model model){
        if(principal == null){
            return GeneralUtilities.REDIRECT + RootController.PathHandler.FULL_HOME_URL;
        }

        UserAccount userAccount = accountService.getUser(principal.getName());
        if(userAccount == null){
            return GeneralUtilities.REDIRECT + RootController.PathHandler.FULL_HOME_URL;
        }

        profileDto.setFirstName(userAccount.getFirstName());
        profileDto.setLastName(userAccount.getLastName());

        AddressDto addressDto = new AddressDto();
        addressDto.setFullAddress(userAccount.getAddress() == null ? "" : userAccount.getAddress().getFullAddress());
        addressDto.setPhone(userAccount.getAddress() == null ? "" : userAccount.getAddress().getPhone());
        profileDto.setAddressDto(addressDto);

        model.addAttribute("changePasswordDto", new ChangePasswordDto());
        return ViewHandler.PROFILE;
    }

    @RequestMapping(path = PathHandler.PROCESS_REGISTER_URL, method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("registerDto") RegisterDto registerDto, BindingResult bindingResult,
                               Model model){

        if(bindingResult.hasErrors()){
            return ViewHandler.REGISTER;
        }
        accountService.registerUser(registerDto);

        model.addAttribute("attributeSuccessRegister", Boolean.TRUE);
        return registerDto.getIsEmployeeCheck() ?
            GeneralUtilities.REDIRECT + EmployeeController.PathHandler.FULL_EMPLOYEES_URL :
            GeneralUtilities.REDIRECT + PathHandler.FULL_LOGIN_URL;
    }

    @RequestMapping(path = PathHandler.PROCESS_UPDATE_URL, method = {RequestMethod.POST, RequestMethod.PUT})
    public String updateUser(Principal principal,
                             @Valid @ModelAttribute("profileDto") ProfileDto profileDto,
                             BindingResult bindingResult,
                             Model model){

        if(principal == null){
            return GeneralUtilities.REDIRECT + RootController.PathHandler.FULL_HOME_URL;
        }

        if(bindingResult.hasErrors()){
            return ViewHandler.PROFILE;
        }

        accountService.updateUser(profileDto, principal.getName());

        model.addAttribute("attributeSuccessUpdate", Boolean.TRUE);
        return GeneralUtilities.REDIRECT + PathHandler.FULL_PROFILE_URL;
    }

    @RequestMapping(path = PathHandler.PROCESS_UPDATE_PASSWORD_URL, method = {RequestMethod.POST, RequestMethod.PUT})
    public String updateUserPassword(@Valid @ModelAttribute("changePasswordDto") ChangePasswordDto changePasswordDto,
                                     BindingResult bindingResult,
                                     Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("errorUpdatePassword", Boolean.TRUE);
            return ViewHandler.PROFILE;
        }

        if(accountService.tryToUpdatePassword(changePasswordDto.getOldPassword(), String.valueOf(changePasswordDto.getPasswordDto().getPassword()))){
            model.addAttribute("attributeSuccessUpdatePassword", Boolean.TRUE);
        }
        else{
            model.addAttribute("errorUpdatePassword", Boolean.TRUE);
        }

        return GeneralUtilities.REDIRECT + PathHandler.FULL_PROFILE_URL;
    }

    @RequestMapping(path = PathHandler.PROCESS_DELETE_URL, method = {RequestMethod.POST, RequestMethod.DELETE})
    public String deleteUser(Principal principal, Model model, HttpServletRequest request){
        if(principal == null){
            model.addAttribute("errorDeletedAccount", Boolean.TRUE);
            return ViewHandler.PROFILE;
        }

        // delete user
        accountService.deleteUser(principal.getName());

        // clear security context
        // https://stackoverflow.com/questions/18957445/how-to-perform-logout-programmatically-in-spring-3
        new SecurityContextLogoutHandler().logout(request, null, null);

        model.addAttribute("successDeletedAccount", Boolean.TRUE);
        return GeneralUtilities.REDIRECT + PathHandler.FULL_REGISTER_URL;
    }
}
