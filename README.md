# 2-ktb-community-be
KTB 6-7주차 과제: 기능 정의서를 기반으로 사용자 요구사항에 맞춘 커뮤니티 관리 시스템 구현

<br/>

## 1. 프로젝트 소개
Spring Boot 기반의 커뮤니티 웹 서비스 백엔드입니다.<br/>
주요 기능: 회원가입, 로그인, 게시글, 댓글

<br/>

## 2. 기술 스택
- Java 21
- Spring Boot 3.4.3
- Spring Security
- Spring Data JPA
- Spring Validation
- MySQL
- Gradle
- Lombok
- JWT 0.12.6
- Swagger (SpringDoc OpenAPI 2.8.6)
- Redis

<br/>

## 3. 주요 기능
- **사용자 인증**
  - 회원가입 및 로그인
  - 세션 기반 인증 처리
- **회원정보 기능**
  - 프로필 사진 및 닉네임 변경
  - 비밀번호 변경
- **게시글 기능**
  - 게시글 작성, 조회, 수정, 삭제
  - 게시글 좋아요 / 좋아요 취소
- **댓글 기능**
  - 댓글 작성, 조회, 수정, 삭제

<br/>

## 4. DB 구조
![Image](https://github.com/user-attachments/assets/210f1de4-fee3-4d0f-8565-b7df5898d4fc)

<br/>

## 5. 프로젝트 구조

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

<br/>

## 6. 실행 방법

```bash
# 1. Git 클론
git clone https://github.com/chulsu0012/2-ktb-community-be.git

# 2. MySQL 실행 및 DB 생성(community)

# 3. 환경 변수 및 설정
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# 4. 실행
./gradlew bootRun
```

<br/>

## 6. API 문서
Swagger UI를 통해 전체 API 명세서를 확인할 수 있습니다.
- [Swagger Document](http://localhost:8080/swagger-ui/index.html)

### 인증
| 메서드   | URL                                               | 설명              |
|----------|---------------------------------------------------|-------------------|
| `POST`   | `/auth/login`                                     | 로그인 요청        |
| `POST`   | `/auth/logout`                                    | 로그아웃          |

---

### 회원정보
| 메서드   | URL                                               | 설명              |
|----------|---------------------------------------------------|-------------------|
| `POST`   | `/users`                                          | 회원가입 요청      |
| `GET`    | `/users/self/info`                                | 내 정보 조회        |
| `PATCH`  | `/users/self/info`                                | 내 정보 수정        |
| `PATCH`  | `/users/self/password`                            | 비밀번호 변경       |
| `DELETE` | `/users/self`                                     | 회원 탈퇴         |

---

### 게시글
| 메서드   | URL                                               | 설명              |
|----------|---------------------------------------------------|-------------------|
| `GET`    | `/posts`                                          | 전체 게시글 목록 조회 |
| `POST`   | `/posts`                                          | 게시글 생성        |
| `GET`    | `/posts/{postId}`                                 | 게시글 단건 조회     |
| `GET`    | `/posts/{postId}/edit`                            | 수정 대상 게시글 불러오기 |
| `PUT`    | `/posts/{postId}/edit`                            | 게시글 수정        |
| `DELETE` | `/posts/{postId}`                                 | 게시글 삭제        |

---

### 댓글
| 메서드   | URL                                               | 설명              |
|----------|---------------------------------------------------|-------------------|
| `GET`    | `/posts/{postId}/comments`                        | 댓글 목록 조회      |
| `POST`   | `/posts/{postId}/comments`                        | 댓글 생성          |
| `PUT`    | `/posts/{postId}/comments/{commentId}`            | 댓글 수정          |
| `DELETE` | `/posts/{postId}/comments/{commentId}`            | 댓글 삭제          |
| `GET`    | `/posts/{postId}/like`                            | 좋아요 여부 확인     |
| `POST`   | `/posts/{postId}/like`                            | 좋아요 / 좋아요 취소  |

<br/>

## 7. API 연동
- 기본적으로 더미데이터(`data.js`)를 기반으로 동작합니다.
- 백엔드(Spring Boot 서버)가 로컬에서 실행 중일 경우, 실제 API와 연동하여 동작합니다.
  - API 엔드포인트는 `config.js`에서 설정할 수 있습니다.

<br/>

## 8. 이슈 및 해결 방법
### 1. 세션 인증
- **문제**: FE에서 요청을 보내도 BE에서 세션 정보를 읽지 못함
- **원인**
  - 도메인을 `localhost` 또는 `127.0.0.1`으로 통일하지 않으면 쿠키 공유 불가
  - FE와 BE의 포트가 다르기 때문에 오리진이 달라짐 → 쿠키 미포함
- **해결**
  - FE: fetch 요청에 `credentials: 'include'` 추가
  - BE: `CorsRegistry` 설정에서 `allowedOrigins`에 FE 주소 명시 + `allowCredentials(true)`
- **기타**
  - 현재 프로젝트에서는 JWT 방식을 도입하면서 더 이상 사용하지 않는 방식
---

### 2. 파일 업로드 (이미지 등 바이너리 데이터 처리)
- **문제**: 이미지와 같은 바이너리 데이터를 전송할 때 BE에서 값을 받지 못함
- **원인**: 일반 JSON 요청으로는 바이너리 데이터 전송 불가
- **해결**
  - FE: `FormData` 객체로 전송
  - BE: `@ModelAttribute`, `MultipartFile` 활용
  - 파일 저장 시 `UUID` 기반 파일명으로 지정하여 충돌 방지
  - FE에는 이미지 경로만 응답하고, 프론트에서 백엔드 주소 붙여서 렌더링

---

### 3. 이미지 필드 처리 (수정 시 null 가능성)
- **문제**: 게시글/회원정보 수정 시 이미지가 없을 경우 null로 전송되어 오류 발생
- **원인**: 기존 이미지를 유지해야 하는데, `MultipartFile`이 null이면 BE에서 처리 누락 가능성 존재
- **해결**
  - FE: 새 이미지가 있는 경우에만 `FormData`에 포함
  - BE: `MultipartFile`이 null일 경우 기존 이미지 유지하도록 조건문 처리

---

### 4. API 응답 형식 통일
- **문제**: FE에서 API 응답 구조가 매번 달라 예외 처리가 어려움
- **원인**: 컨트롤러별 응답 포맷이 일관되지 않음
- **해결**
  - 공통 응답 포맷 설계
    - 성공: `{ success: true, data: ... }`
    - 실패: `{ success: false, message: "오류 메시지" }`
  - 커스텀 예외 클래스 + 전역 예외 처리기 구성하여 일관된 오류 응답 제공

---

### 5. 게시글 조회 vs 수정 API 분리
- **문제**: 게시글 수정할 때도 조회수(view count)가 올라감
- **원인**: 조회와 수정을 동일한 API로 처리 → 조회수 증가 로직도 같이 실행됨
- **해결**
  - 게시글 조회용 API와 수정용 API를 분리
  - 조회용 API에만 조회수 증가 쿼리(`@Modifying` + JPQL) 적용

---

### 6. Swagger(SpringDoc) 호환 오류

- **문제**  
  Swagger UI 접속 시 500 에러 발생 (`NoSuchMethodError`)

- **원인**
  - `springdoc-openapi-starter-webmvc-ui:2.2.0`은 Spring Framework 6.0.x와 호환되는 의존성을 사용함
  - Spring Boot 3.4.3은 Spring Framework 6.2.x를 사용하기 때문에 호환 오류 발생

- **해결 방법**
  **① Spring Boot 버전 다운그레이드**
  - Spring Boot 버전을 3.1.x로 낮추면 Swagger 2.2.0 사용 가능
  ```groovy
  plugins {
      id 'java'
      id 'org.springframework.boot' version '3.1.9'
      id 'io.spring.dependency-management' version '1.1.3'
  }

  dependencies {
      implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'
  }
  ```
  
  **② Swagger 최신 버전 사용**
  - Maven Central에서 SwaggerDoc 최신 버전(예: 2.8.6)을 확인하고 사용
  - Spring Boot 3.4.x에서도 정상 동작함
  ```groovy
  plugins {
    id 'java'
    id 'org.springframework.boot' version '3.4.3'
    id 'io.spring.dependency-management' version '1.1.7'
  }
  
  dependencies {
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6'
  }
  ```
  
- **결론**
  - Swagger 호환 오류는 Spring Boot 3.4.x와 Swagger 버전 불일치에서 비롯된 문제였음
  - Swagger 최신 버전(2.8.6) 사용으로 문제 해결됨
  - 프로젝트 초기 의존성 호환성 확인의 중요성을 경험함

<br/>
<br/>

## 고도화
### 1. Redis + JWT 도입

- **JWT 기반 인증/인가 도입** -> 기존 세션 방식보다 RESTful한 구조를 적용
- **Access Token + Refresh Token** 구조로 인증 유지 및 자동 갱신 처리
- **Redis**를 활용하여 Refresh Token을 저장하고 관리
  - 로그아웃 시 토큰 무효화
  - 토큰 재발급 시 유효성 검증 가능
- Redis에 저장된 Refresh Token은 `refresh_token:{userId}` 형식으로 저장, 만료시간(TTL)을 `Duration.ofDays(1)`로 함께 설정

#### 흐름 요약
1. 로그인 시 Access Token, Refresh Token을 생성한다.
2. Access Token은 클라이언트 localStorage에 저장한다.
3. Refresh Token은 Redis에 저장한다.
4. Access Token 만료 시, Refresh Token으로 `/auth/refresh`를 요청한다.
5. 서버는 Redis에 저장된 토큰과 비교하여 Access Token을 재발급한다.

#### JWT Claim 구성
- **subject**: 사용자의 닉네임(`user.nickname()`)
- **id**: 사용자의 고유 ID(`user.id`)

#### 도입 효과
- stateless: 서버 확장성 향상
- 보안성 개선: Refresh Token 검증 및 무효화 가능
- 클라이언트 UX 향상: 토큰 자동 갱신으로 인증 지속

