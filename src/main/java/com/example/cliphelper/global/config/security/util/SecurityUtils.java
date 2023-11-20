package com.example.cliphelper.global.config.security.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {

    public Long getCurrentUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(userId);
    }
}
