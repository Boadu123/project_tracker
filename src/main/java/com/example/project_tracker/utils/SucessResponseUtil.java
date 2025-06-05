package com.example.project_tracker.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class SucessResponseUtil {

    public static ResponseEntity<Map<String, Object>> sucessResponseUtil(HttpStatus status, Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("statusCode", status.value());
        body.put("status", "success");
        body.put("data", data);
        return new ResponseEntity<>(body, status);
    }
}
