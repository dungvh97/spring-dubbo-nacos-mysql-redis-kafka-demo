package com.demo.user;

import com.demo.user.dto.LoginRequest;
import com.demo.user.dto.RegisterRequest;
import com.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

	@Autowired
	private UserService userService;

	@Test
	public void testRegisterAndLogin() {
		String username = "john_" + UUID.randomUUID();
		RegisterRequest reg = new RegisterRequest(username, "123456", username + "@test.com");
		var userDTO = userService.register(reg);

		assertNotNull(userDTO.id());

		LoginRequest login = new LoginRequest(username, "123456");
		String token = userService.login(login);
		assertNotNull(token);
	}
}