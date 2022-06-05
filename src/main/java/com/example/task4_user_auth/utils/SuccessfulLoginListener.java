package com.example.task4_user_auth.utils;

import com.example.task4_user_auth.entity.User;
import com.example.task4_user_auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;

import static com.example.task4_user_auth.utils.SessionManager.newSession;

@Component
@RequiredArgsConstructor
public class SuccessfulLoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
            User user = (User) event.getAuthentication().getPrincipal();
            user.setLastLoginTime(Date.valueOf(LocalDate.now()));
            HttpSession userSession = getUserSession();
            newSession(user.getUsername(), userSession);
            userRepository.save(user);
    }

    public  HttpSession getUserSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}
