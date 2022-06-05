package com.example.task4_user_auth.base_service;


import com.example.task4_user_auth.payload.response.UserDto;

import java.util.List;

public interface UserBaseService {
    List<UserDto> getUserList();
    void deleteAllById(Long[] id);
    void blockAllById(Long[] id);
    void unblockAllById(Long[] userIds);
}
