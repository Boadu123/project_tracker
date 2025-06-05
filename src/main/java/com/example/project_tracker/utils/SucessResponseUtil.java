package com.example.project_tracker.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class SucessResponseUtil {

    public static ResponseEntity<Map<String, Object>> sucessResponseUtil(HttpStatus status, Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("data", data);
        body.put("message", "success");
        body.put("status", status.value());
        return new ResponseEntity<>(body, status);
    }
}
