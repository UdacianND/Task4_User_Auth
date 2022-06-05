package com.example.task4_user_auth.base_service;


import com.example.task4_user_auth.entity.User;
import com.example.task4_user_auth.payload.response.UserDto;

import java.util.List;

public interface UserBaseService {
    User getUserByUsername(String username);
    List<UserDto> getUserList();
    void deleteAllById(Long[] id);
    void blockAllById(Long[] id);
    void unblockAllById(Long[] userIds);
    boolean checkUserInList(Long[] userIds);
}
