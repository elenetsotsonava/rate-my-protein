package com.ratemyprotein.controller;

import com.ratemyprotein.dto.RegistrationRequest;
import com.ratemyprotein.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute(
                "registrationRequest",
                new RegistrationRequest()
        );

        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid
            @ModelAttribute("registrationRequest")
            RegistrationRequest request,
            BindingResult bindingResult
    ) {

        if (!Objects.equals(
                request.getPassword(),
                request.getConfirmPassword()
        )) {
            bindingResult.rejectValue(
                    "confirmPassword",
                    "password.mismatch",
                    "Passwords do not match"
            );
        }

        if (userService.emailExists(request.getEmail())) {
            bindingResult.rejectValue(
                    "email",
                    "email.exists",
                    "This email is already registered"
            );
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerUser(request);
        } catch (IllegalArgumentException exception) {

            bindingResult.reject(
                    "registration.failed",
                    exception.getMessage()
            );

            return "auth/register";
        }

        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }
}