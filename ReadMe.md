# My SNS 프로젝트 
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

- 회원가입, 로그인, 글쓰기, 조회, 수정, 삭제, 알람, 좋아요, 댓글 기능
- [Swagger-ui](http://ec2-3-38-172-197.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/#/)
---
# 🥇 요구사항
#### 기술 스택
- 에디터 : Intellij Ultimate
- 개발 툴 : SpringBoot 2.7.5
- 자바 : JAVA 11
- 빌드 : Gradle 6.8
- 서버 : AWS EC2
- 배포 : Docker, gitlab
- 데이터베이스 : MySql 8.0
- 필수 라이브러리 : SpringBoot Web, MySQL, Spring Data JPA, Lombok, Spring Security

#### EndPoint
<img src="src/main/resources/images/endpoint.png" width="600" height="483"/>

# 🥈 기능
### 인프라
- [x] EC2 생성 및 도커 설치
- [x] gitlab 배포파일 및 ec2 크론탭 설정

### DataBase
- [x] Mysql 구축
- [x] DB 설계

### Swagger UI 
- [x] swagger 설정
- [x] Spring Security에 적용

### Spring Security + JWT 
- [x] 회원가입과 로그인
- [x] admin 권한 (Role 역할) 구현

### Spring Boot
- [x] 게시글 CRUD 구현
- [x] 댓글 CRUD 구현
- [x] 좋아요 누르기, 조회 기능 구현
- [x] 마이피드 기능 구현
- [x] 알람 기능 구현(`좋아요`, `댓글`)
- [x] Entity에 soft delete 적용
- [x] 예외 처리 `@exceptionHandler`
- [x] Jpa Auditing

### Test Code
- [x] controller 테스트 코드
    - user, post, like, comment, alarm
- [ ] service 테스트 코드 

### UI
- [ ] 화면ui 설정


#### 🥉 ERD Diagram
<img src="src/main/resources/images/erd_1.png" width="515" height="769"/>


