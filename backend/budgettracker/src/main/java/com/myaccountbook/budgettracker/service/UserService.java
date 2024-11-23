package com.myaccountbook.budgettracker.service;

import com.myaccountbook.budgettracker.dto.LoginRequest;
import com.myaccountbook.budgettracker.entity.User;
import com.myaccountbook.budgettracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.orElse(null); //인증 성공
        }
        return null; // 인증 실패
    }

    public void registerUser(LoginRequest signupRequest) {
        //사용자 이름 중복확인
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(signupRequest.getPassword());

        userRepository.save(user);
    }
}
