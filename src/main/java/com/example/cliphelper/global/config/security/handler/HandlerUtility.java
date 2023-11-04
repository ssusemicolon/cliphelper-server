package com.example.cliphelper.global.config.security.handler;

import com.example.cliphelper.global.error.ErrorCode;
import com.example.cliphelper.global.error.ErrorResponse;
import com.example.cliphelper.global.result.ResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HandlerUtility {
    public static void writeResponse(HttpServletRequest request, HttpServletResponse response, ErrorCode errorCode)
            throws IOException, ServletException {
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, ErrorResponse.of(errorCode));
            os.flush();
        }
    }

    public static void writeResponse(HttpServletRequest request, HttpServletResponse response, ErrorCode errorCode,
            List<ErrorResponse.FieldError> errorList) throws IOException, ServletException {
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, ErrorResponse.of(errorCode, errorList));
            os.flush();
        }
    }

    public static void writeResponse(HttpServletRequest request, HttpServletResponse response,
            ResultResponse resultResponse) throws IOException, ServletException {
        response.setStatus(resultResponse.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, resultResponse);
            os.flush();
        }
    }
}
