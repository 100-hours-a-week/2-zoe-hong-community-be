# 2-ktb-community-be
KTB 6-7주차 과제: 기능 정의서를 기반으로 사용자 요구사항에 맞춘 커뮤니티 관리 시스템 구현



## 1. 프로젝트 소개
Spring Boot 기반의 커뮤니티 웹 서비스 백엔드입니다.
주요 기능: 회원가입, 로그인, 게시글, 댓글



## 2. 기술 스택
- Java 21
- Spring Boot 3.4.3
- Spring Security
- Spring Data JPA
- MySQL
- Gradle
<!-- - Swagger (SpringDoc OpenAPI) -->



## 3. DB 구조
![Image](https://github.com/user-attachments/assets/210f1de4-fee3-4d0f-8565-b7df5898d4fc)



## 4. 프로젝트 구조

```
2-ktb-community-be
├── README.md
└── backend
    ├── build.gradle
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── com.community.backend
    │   │   │       ├── BackendApplication.java
    │   │   │       ├── common
    │   │   │       │   └── exception
    │   │   │       │       ├── CustomException.java
    │   │   │       │       └── GlobalExceptionHandler.java
    │   │   │       ├── config
    │   │   │       │   ├── JpaConfig.java
    │   │   │       │   ├── SecurityConfig.java
    │   │   │       │   ├── SwaggerConfig.java
    │   │   │       │   └── WebConfig.java
    │   │   │       ├── controller
    │   │   │       │   ├── AuthController.java
    │   │   │       │   ├── CommentController.java
    │   │   │       │   ├── PostController.java
    │   │   │       │   └── UserController.java
    │   │   │       ├── domain
    │   │   │       │   ├── Comment.java
    │   │   │       │   ├── Liked.java
    │   │   │       │   ├── LikedId.java
    │   │   │       │   ├── Post.java
    │   │   │       │   ├── User.java
    │   │   │       │   └── enums
    │   │   │       │       ├── UserRole.java
    │   │   │       │       └── UserState.java
    │   │   │       ├── dto
    │   │   │       │   ├── CommentDTO.java
    │   │   │       │   ├── CommentRequest.java
    │   │   │       │   ├── PasswordRequest.java
    │   │   │       │   ├── PostCardDTO.java
    │   │   │       │   ├── PostDTO.java
    │   │   │       │   ├── PostRequest.java
    │   │   │       │   ├── ProfileRequest.java
    │   │   │       │   ├── ProfileResponse.java
    │   │   │       │   ├── ResponseDTO.java
    │   │   │       │   ├── UserDTO.java
    │   │   │       │   ├── UserJoinRequest.java
    │   │   │       │   ├── UserLoginRequest.java
    │   │   │       │   └── UserSessionDTO.java
    │   │   │       ├── repository
    │   │   │       │   ├── CommentRepository.java
    │   │   │       │   ├── LikedRepository.java
    │   │   │       │   ├── PostRepository.java
    │   │   │       │   └── UserRepository.java
    │   │   │       ├── service
    │   │   │       │   ├── CommentService.java
    │   │   │       │   ├── CommentServiceImpl.java
    │   │   │       │   ├── PostService.java
    │   │   │       │   ├── PostServiceImpl.java
    │   │   │       │   ├── UserService.java
    │   │   │       │   └── UserServiceImpl.java
    │   │   │       └── util
    │   │   │           ├── EmailValidator.java
    │   │   │           ├── ImageHandler.java
    │   │   │           ├── NicknameValidator.java
    │   │   │           └── PasswordValidator.java
    │   │   └── resources
    │   └── test
    │       └── java
    │           └── com.community.backend
    │               ├── BackendApplicationTests.java
    │               ├── RepositoryTest
    │               │   ├── CommentRepositoryTest.java
    │               │   ├── LikedRepositoryTest.java
    │               │   ├── PostRepositoryTest.java
    │               │   └── UserRepositoryTest.java
    │               └── ServiceTest
    │                   ├── CommentServiceTest.java
    │                   ├── MockFileGenerator.java
    │                   ├── PostServiceTest.java
    │                   └── UserServiceTest.java
    └── uploads
        └── images/
```



## 5. 실행 방법

```bash
# 1. Git 클론
git clone https://github.com/chulsu0012/2-ktb-community-be.git

# 2. MySQL 실행 및 DB 생성
create database community;

# 3. 환경 변수 및 설정
spring.application.name=backend

spring.datasource.url=jdbc:mysql://localhost:3306/community?serverTimezone=UTC&useSSL=false
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# 4. 실행
./gradlew bootRun
```



## 6. 주요 API 엔드포인트
| 메서드   | URL                                               | 설명              |
|----------|---------------------------------------------------|-------------------|
| `POST`   | `/auth/login`                                     | 로그인 요청        |
| `POST`   | `/users`                                          | 회원가입 요청      |
| `GET`    | `/posts`                                          | 전체 게시글 목록 조회 |
| `POST`   | `/posts`                                          | 게시글 생성        |
| `GET`    | `/posts/{postId}`                                 | 게시글 단건 조회     |
| `PUT`    | `/posts/{postId}/edit`                            | 게시글 수정        |
| `DELETE` | `/posts/{postId}`                                 | 게시글 삭제        |
| `GET`    | `/posts/{postId}/comments`                        | 댓글 목록 조회      |
| `POST`   | `/posts/{postId}/comments`                        | 댓글 생성 요청      |
| `PUT`    | `/posts/{postId}/comments/{commentId}`            | 댓글 수정 요청      |
| `DELETE` | `/posts/{postId}/comments/{commentId}`            | 댓글 삭제 요청      |
| `GET`    | `/posts/{postId}/like`                            | 좋아요 여부 확인     |
| `POST`   | `/posts/{postId}/like`                            | 좋아요 요청         |
| `GET`    | `/users/self/info`                                | 내 정보 조회        |
| `PATCH`  | `/users/self/info`                                | 내 정보 수정        |
| `PATCH`  | `/users/self/password`                            | 비밀번호 변경       |
| `DELETE` | `/users/self`                                     | 회원 탈퇴 요청      |
| `POST`   | `/auth/logout`                                    | 로그아웃 요청       |




## 7. 주요 기능
- 사용자 인증
  - 회원가입 및 로그인
  - 세션 기반 인증 처리
- 회원정보 기능
  - 프로필 사진 및 닉네임 변경
  - 비밀번호 변경
- 게시글 기능
  - 게시글 작성, 조회, 수정, 삭제
  - 게시글 좋아요 / 좋아요 취소
- 댓글 기능
  - 댓글 작성, 조회, 수정, 삭제