package me.jeehahn.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.jeehahn.springbootdeveloper.domain.User;
import me.jeehahn.springbootdeveloper.dto.AddUserRequest;
import me.jeehahn.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(AddUserRequest dto) {
        userRepository.save(User.builder()
            .email(dto.getEmail())
            .password(bCryptPasswordEncoder.encode(dto.getPassword()))
            .build());
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
