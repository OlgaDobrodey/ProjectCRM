package com.itrex.java.lab.projectcrmspringboot.controller;

import com.itrex.java.lab.projectcrmspringboot.dto.UserDTO;
import com.itrex.java.lab.projectcrmspringboot.exceptions.CRMProjectRepositoryException;
import com.itrex.java.lab.projectcrmspringboot.exceptions.CRMProjectServiceException;
import com.itrex.java.lab.projectcrmspringboot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
public class HelloWorldController {

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/greeting")
    public String helloWorldController(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @ExceptionHandler({CRMProjectRepositoryException.class, CRMProjectServiceException.class})
    public void handleException() {

    }

    @GetMapping("/user/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        try {
            UserDTO userDto = new UserDTO();
            model.addAttribute("user", userDto);
            model.addAttribute("listRoles", roleService.getAllRoles());
        } catch (CRMProjectServiceException e) {
            e.getStackTrace();
        }
        return "registration";
    }

}