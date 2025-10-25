user-service/
├── src/main/java/com/demo/user/
│   ├── UserServiceApplication.java
│   ├── config/
│   │   ├── RedisConfig.java
│   │   └── SwaggerConfig.java
│   ├── controller/
│   │   └── UserController.java
│   ├── service/
│   │   ├── UserService.java
│   │   └── impl/
│   │       └── UserServiceImpl.java
│   ├── dto/
│   │   ├── UserDTO.java
│   │   ├── RegisterRequest.java
│   │   └── LoginRequest.java
│   ├── entity/
│   │   └── UserEntity.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   └── BusinessException.java
│   ├── util/
│   │   └── JwtUtil.java
│   └── response/
│       └── ApiResponse.java
├── src/main/resources/
│   └── application.yml
└── src/test/java/com/demo/user/
├── UserServiceTests.java
└── UserControllerTests.java
