package com.example.cliphelper.domain.user.service;

import com.example.cliphelper.domain.user.dto.UserModifyRequestDto;
import com.example.cliphelper.domain.user.dto.UserRequestDto;
import com.example.cliphelper.domain.user.dto.UserResponseDto;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
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

    /*
    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 userId를 가진 회원이 존재하지 않습니다."));

        // user 스크랩 개수
        // user 북마크 개수
        // user 팔로워
        // 알림받을 시간 목록
    }
     */
}
