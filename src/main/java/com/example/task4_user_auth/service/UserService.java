package com.example.task4_user_auth.service;

import com.example.task4_user_auth.base_service.UserBaseService;
import com.example.task4_user_auth.entity.User;
import com.example.task4_user_auth.entity.UserStatus;
import com.example.task4_user_auth.payload.response.UserDto;
import com.example.task4_user_auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.task4_user_auth.entity.UserStatus.ACTIVE;
import static com.example.task4_user_auth.entity.UserStatus.BLOCKED;
import static com.example.task4_user_auth.utils.SessionManager.getSessionByUsername;
import static com.example.task4_user_auth.utils.SessionManager.removeSession;


@RequiredArgsConstructor
@Service
public class UserService implements UserBaseService {

    private final UserRepository userRepository;

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
                            user.getStatus())
            );
        }
        return userDtoList;
    }

    @Override
    public void deleteAllById(Long[] userIds) {
        for (Long id: userIds) {
            blockUser(id);
            userRepository.deleteById(id);
        }
    }

    @Override
    public void blockAllById(Long[] userIds) {
        for (Long id : userIds){
            setUserStatus(id, BLOCKED);
            blockUser(id);
        }
    }

    @Override
    public void unblockAllById(Long[] userIds) {
        for (Long id : userIds)
            setUserStatus(id, ACTIVE);
    }

    public void setUserStatus(Long id, UserStatus status) {
            User user = getUserById(id);
            user.setStatus(status);
            userRepository.save(user);
    }

    private User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty())
            throw new RuntimeException("User not found with id - "+id);
        return optionalUser.get();
    }

    private void blockUser(Long id){
        String username = getUserById(id).getUsername();
        HttpSession session = getSessionByUsername(username);
        if(session != null){
            session.invalidate();
            removeSession(username);
        }
    }

}
