package com.example.task4_user_auth.service;

import com.example.task4_user_auth.base_service.UserBaseService;
import com.example.task4_user_auth.entity.User;
import com.example.task4_user_auth.entity.UserStatus;
import com.example.task4_user_auth.payload.response.UserDto;
import com.example.task4_user_auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.task4_user_auth.entity.UserStatus.*;
import static com.example.task4_user_auth.utils.SessionManager.getSessionByUsername;
import static com.example.task4_user_auth.utils.SessionManager.removeSession;


@RequiredArgsConstructor
@Service
public class UserService implements UserBaseService {

    private final UserRepository userRepository;
//    private final SessionRegistry sessionRegistry;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<UserDto> getUserList() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user: userList){
            userDtoList.add(
                    new UserDto(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getRegisteredTime(),
                            user.getLastLoginTime(),
                            user.getStatus()
                    )
            );
        }
        return userDtoList;
    }

    @Override
    public void deleteAllById(Long[] userIds) {
        for (Long id: userIds) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public void blockAllById(Long[] userIds) {
        setAllUsersStatus(userIds, BLOCKED);
    }

    public void unblockAllById(Long[] userIds) {
        setAllUsersStatus(userIds, ACTIVE);
    }

    public void setAllUsersStatus(Long[] userIds, UserStatus status) {
        for (Long id: userIds) {
            User user = getUserById(id);
            user.setStatus(status);
            if (status == BLOCKED){
                blockUser(user.getUsername());
            }
            userRepository.save(user);
        }
    }

    private User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty())
            throw new RuntimeException("User not found with id - "+id);
        return optionalUser.get();
    }

    private void blockUser(String username){
        HttpSession session = getSessionByUsername(username);
        if(session != null){
            session.invalidate();
            removeSession(username);
        }
    }

    @Override
    public boolean checkUserInList(Long[] userIds) {
        User principal = getPrincipal();
        boolean userExists = false;
        for (Long id: userIds) {
            if (principal.getId().equals(id)) {
                userExists = true;
                break;
            }
        }
        return userExists;
    }


    private User getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User)authentication.getPrincipal();
    }
}
