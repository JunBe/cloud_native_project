package com.myaccountbook.budgettracker.controller;

import com.myaccountbook.budgettracker.dto.LoginRequest;
import com.myaccountbook.budgettracker.entity.User;
import com.myaccountbook.budgettracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            //세션에 사용자 정보 저장
            session.setAttribute("user", user);
            System.out.println("세션 생성됨. 세션 ID: " + session.getId());
            System.out.println(session.getAttribute("user"));
            return ResponseEntity.ok("로그인 성공");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/status")
    public ResponseEntity<?> getLoginStatus(HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println("user 세션 : " + user);
        if (user != null) {
            return ResponseEntity.ok(user.getUsername());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 되지 않았습니다.");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody LoginRequest signupRequest) {
        try {
            userService.registerUser(signupRequest);
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
