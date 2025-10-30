package com.demo.user.controller;

import com.demo.common.api.product.ProductDTO;
import com.demo.user.dto.LoginRequest;
import com.demo.user.dto.RegisterRequest;
import com.demo.user.dto.UserDTO;
import com.demo.user.entity.User;
import com.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import com.demo.user.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserDTO> register(@RequestBody RegisterRequest request) {
        return ApiResponse.success(userService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {
        return ApiResponse.success(userService.login(request));
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

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUser(@PathVariable("id") Long id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO dto) {
        userService.updateUser(id, dto);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }

//    @PostMapping("/favorite-products")
//    public ApiResponse<List<ProductDTO>> getUserFavorites(@RequestBody UserFavoriteRequest req) {
//        return ApiResponse.success(userService.getFavoriteProducts(req.getUserId()));
//    }
}
