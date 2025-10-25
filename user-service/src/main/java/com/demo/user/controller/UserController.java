package com.demo.user.controller;

import com.demo.user.entity.User;
import com.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody Map<String, String> body) {
        User user = userService.register(body.get("username"), body.get("email"), body.get("password"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        User user = userService.login(body.get("username"), body.get("password"));
        return ResponseEntity.ok(Map.of(
                "token", user.getPassword(), // token stored in Redis
                "user", user.getUsername()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
        User user = userService.getUserFromSession(token);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired session"));
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
