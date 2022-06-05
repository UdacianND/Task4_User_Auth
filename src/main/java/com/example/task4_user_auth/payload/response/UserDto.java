package com.example.task4_user_auth.payload.response;

import com.example.task4_user_auth.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Date registeredTime;
    private Date lastLoginTime;
    private UserStatus status;
}
