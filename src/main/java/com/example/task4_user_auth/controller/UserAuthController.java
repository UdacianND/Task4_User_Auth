package com.example.task4_user_auth.controller;

import com.example.task4_user_auth.base_service.UserBaseService;
import com.example.task4_user_auth.entity.User;
import com.example.task4_user_auth.payload.response.UserDto;
import com.example.task4_user_auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class UserAuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserBaseService userBaseService;

    @GetMapping("/")
    public String mainPage(Model model){
        List<UserDto> userList = userBaseService.getUserList();
        model.addAttribute("userList", userList);
        return "user-list";
    }

    @GetMapping("register")
    public String getSignUpView(){
        return "sign-up";
    }



    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }

    @PostMapping("register")
    public String signUp(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            Model model

    ){
        return registerUser(username, email, password, model);
    }

    void saveUser(String username, String email, String password){
        User user = new User(
                username,
                email,
                passwordEncoder.encode(password),
                Date.valueOf(LocalDate.now())
        );
        userRepository.save(user);
    }

    String registerUser(String username, String email, String password, Model model){
        Optional<User> byUsername = userRepository.findByUsername(username);

        if(byUsername.isPresent()){
            model.addAttribute("registerMsg","User already exists with username - "+username);
            return "sign-up";
        }

        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isPresent()){
            model.addAttribute("registerMsg","User already exists with email - "+email);
            return "sign-up";
        }
        saveUser(username, email, password);
        return "redirect:/login";
    }

    @GetMapping("access-forbidden")
    public String getLoginPage() {
        return "access-forbidden";
    }

}
