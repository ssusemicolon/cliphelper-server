package com.example.cliphelper.util;

import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.user.entity.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserUtils {
    public static User newInstance() {
        String username = RandomStringUtils.random(6, true, true);
        String email = username + "@naver.com";

        return User.builder()
                .id(null)
                .email(email)
                .username(username)
                .picture(null)
                .enableNotifications(false)
                .build();
    }

    public static List<User> newInstanceList(int userCount) {
        List<User> userList = new ArrayList<>();
        Set<String> usernameSet = new HashSet<>();

        while (userList.size() < userCount) {
            final User user = newInstance();
            if (usernameSet.contains(user.getUsername())) {
                continue;
            }

            userList.add(user);
            usernameSet.add(user.getUsername());
        }

        return userList;
    }


    // 이렇게 of 메소드를 구현한다면, builder의 장점을 다 지워버리는 코드라고 생각한다.
    /*
    public static User of(String email, String username, String picture, boolean enableNotifications) {
        return User.builder()
                .id(null)
                .email(email)
                .username(username)
                .picture(picture)
                .enableNotifications(enableNotifications)
                .build();
    }
    */

}
