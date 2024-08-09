package org.example.skitschbook.users;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.skitschbook.global.dto.StatusResponse;
import org.example.skitschbook.users.dto.UserNicknameRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/update/nickname")
    public ResponseEntity<StatusResponse> updateNickname(@RequestBody UserNicknameRequest nicknameRequest) {
        return userService.changeNickname(nicknameRequest);
    }
}
