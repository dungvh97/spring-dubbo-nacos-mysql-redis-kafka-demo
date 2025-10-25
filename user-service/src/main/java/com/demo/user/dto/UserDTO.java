package com.demo.user.dto;

import java.io.Serializable;

public record UserDTO(Long id, String username, String email, String nickname) implements Serializable {}
