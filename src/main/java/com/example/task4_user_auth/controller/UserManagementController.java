package com.example.task4_user_auth.controller;

import com.example.task4_user_auth.base_service.UserBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/management")
public class UserManagementController {

    private final UserBaseService userBaseService;

    @PostMapping("/delete")
    public void delete(
            @RequestBody Long[] userIds
    ){
        userBaseService.deleteAllById(userIds);
    }

    @PostMapping("/block")
    public void block(
            @RequestBody Long[] userIds
    )  {
        userBaseService.blockAllById(userIds);
    }

    @PostMapping("/unblock")
    public void unblock(
            @RequestBody Long[] userIds
    ){
        userBaseService.unblockAllById(userIds);
    }
}
