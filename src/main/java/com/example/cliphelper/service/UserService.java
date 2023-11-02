package com.example.cliphelper.service;

import com.example.cliphelper.dto.UserModifyRequestDto;
import com.example.cliphelper.dto.UserRequestDto;
import com.example.cliphelper.dto.UserResponseDto;
import com.example.cliphelper.entity.User;
import com.example.cliphelper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public Long createUser(UserRequestDto userRequestDto) {
        User user = userRequestDto.toEntity();
        Long userId = userRepository.save(user)
                .getId();

        return userId;
    }

    public List<UserResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User user : users) {
            userResponseDtos.add(UserResponseDto.of(user));
        }

        return userResponseDtos;
    }

    public UserResponseDto findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 userId를 가진 회원이 존재하지 않습니다."));

        return UserResponseDto.of(user);
    }

    /*
    public UserResponseDto findUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 회원이 존재하지 않습니다."));

        return UserResponseDto.of(user);
    }
    */

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public void modifyUser(Long userId, UserModifyRequestDto userModifyRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 userId를 가진 회원이 존재하지 않습니다."));

        user.changeInfo(
                userModifyRequestDto.getEmail(),
                userModifyRequestDto.getPassword(),
                userModifyRequestDto.getUsername());

        userRepository.save(user);
    }
}
