# ๐ง My SNS ํ๋ก์ ํธ 
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">

> **์ฃผ์๊ธฐ๋ฅ ๋ฐ ๊ฒฐ๊ณผ** 
    - ํ์๊ฐ์, ๋ก๊ทธ์ธ, ๊ธ์ฐ๊ธฐ, ์กฐํ, ์์ , ์ญ์ , ์๋, ์ข์์, ๋๊ธ  
    [Swagger-ui](http://ec2-3-38-172-197.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/#/)
---

## ๐ฅ ์๊ตฌ์ฌํญ
#### ๊ธฐ์  ์คํ
- ์๋ํฐ : Intellij Ultimate
- ๊ฐ๋ฐ ํด : SpringBoot 2.7.5
- ์๋ฐ : JAVA 11
- ๋น๋ : Gradle 6.8
- ์๋ฒ : AWS EC2
- ๋ฐฐํฌ : Docker, gitlab
- ๋ฐ์ดํฐ๋ฒ ์ด์ค : MySql 8.0
- ํ์ ๋ผ์ด๋ธ๋ฌ๋ฆฌ : SpringBoot Web, MySQL, Spring Data JPA, Lombok, Spring Security

#### EndPoint
<img src="src/main/resources/images/endpoint.png" width="600" height="483" align="center"/>
---

## ๐ฅ ๊ธฐ๋ฅ ๊ตฌํ
### Infra
- [x] EC2 ์์ฑ ๋ฐ ๋์ปค ์ค์น
- [x] gitlab ๋ฐฐํฌํ์ผ ๋ฐ ec2 ํฌ๋ก ํญ ์ค์ 

### DataBase
- [x] Mysql ๊ตฌ์ถ
- [x] DB ์ค๊ณ

### Swagger UI 
- [x] swagger ์ค์ 
- [x] Spring Security์ ์ ์ฉ `SecurityFilterChain`

### Spring Security + JWT 
- [x] ํ์๊ฐ์๊ณผ ๋ก๊ทธ์ธ
- [x] admin ๊ถํ ๊ตฌํ (UserRole`enum` > `ADMIN`, `USER`)

### Spring Boot
- [x] ๊ฒ์๊ธ CRUD ๊ตฌํ
- [x] ๋๊ธ CRUD ๊ตฌํ
- [x] ์ข์์ ๋๋ฅด๊ธฐ, ์กฐํ ๊ธฐ๋ฅ ๊ตฌํ
- [x] ๋ง์ดํผ๋ ๊ธฐ๋ฅ ๊ตฌํ
- [x] ์๋ ๊ธฐ๋ฅ ๊ตฌํ (AlarmType `enum` > `์ข์์`, `๋๊ธ`)
- [x] Entity์ soft delete ์ ์ฉ
- [x] ์์ธ ์ฒ๋ฆฌ `@exceptionHandler`
- [x] Jpa Auditing

### Test Code
- [x] controller ํ์คํธ ์ฝ๋
    - user, post, like, comment, alarm
- [ ] service ํ์คํธ ์ฝ๋ 

### UI
- [ ] ํ๋ฉดui ์ค์ 
---


## ๐ฅ๊ตฌ์กฐ 
#### ERD
<img src="src/main/resources/images/erd_1.png" width="515" height="769"/>

#### Architecture
- ๋ ์ด์ด๋ ์ํคํ์ฒ
    - ์ญํ ์ ๋ฐ๋ผ ๋๋ฆฝ๋ ๋ชจ๋๋ก ๋๋์ด์ ๊ตฌ์ฑํ๋ ํจํด์๋๋ค. 
    - ๊ฐ ๋ชจ๋์ด ์๋ก์ ์์กด๋์ ๋ฐ๋ผ ์ธต์ธตํ ์๋ฏ์ด ์ฐ๊ฒฐ๋์ด์ ์ ์ฒด์ ์์คํ์ ๊ตฌํํ๋ ๊ตฌ์กฐ
    - ํน์ง : ๋จ๋ฐฉํฅ ์์กด์ฑ. ๊ฐ๊ฐ์ ๋ ์ด์ด๋ ์ค์ง ์๊ธฐ๋ณด๋ค ํ์์ ์๋ ๋ ์ด์ด์๋ง ์์กด
<img src="src/main/resources/images/layerArchitecture.png" width="600" height="120"/>

## ๐ฌ ํ๊ณ 
#### ์ ๊ฒฝ ์ด ๋ถ๋ถ
Dto ์ค๊ณ๋ฅผ ํ  ๋ ํ๋์ ํด๋์ค๊ฐ ํ ๊ฐ์ง ๊ธฐ๋ฅ๋ง ๊ฐ๋ `๋จ์ผ ์ฑ์ ์์น`์ ์งํฌ ์ ์๋๋ก ๋ง๋ค์ด๋ดค๋ค.
- request dto, response dto
- service controller ์ฌ์ด์์ ๋ณํํ๋ dto
#### ๊ฐ์ ์ฌํญ
Controller ์กฐํ`Page` test code 
- ์ธ์ ํ service ๋ ์ด์ด๊ฐ ์๋ repository ๊ฐ์ฒด๋ฅผ ์ฌ์ฉํ๋๋ฐ ์ด ๋ถ๋ถ ๋ฆฌํฉํ ๋ง์ด ํ์ํ๋ค.
- Controller์์ ํ์ด์ง ์ฒ๋ฆฌ๋ ๋ก์ง์ ํ์คํธ ํ๊ธฐ ์ํด PageImpl์ ์ด์ฉํด ์ง์  Page<dto> ๊ฐ์ฒด๋ฅผ ๋ง๋ค์ด ์ฃผ์๋๋ฐ,์ด๋ null Pointer ์๋ฌ๊ฐ ๋ฐ์ํ๋ค.
- ๋๋ฌธ์ ์ด ์๋ฌ๋ฅผ ํผํ๊ธฐ ์ํด์ repository๊ฐ์ฒด๋ฅผ ํตํด ์ง์  saveํด์ฃผ์๋ค.

#### ์ถ๊ฐ์ฌํญ
Service test ์ฝ๋ ์์ฑํ๊ธฐ
- TestInfoFixture์ ์ฌ์ฉํด์ ์์ฑ

UI ์์ฑ ์์ผ๋ณด๊ธฐ
- ๋ฆฌ์กํธ ์ฌ์ฉํด์ ์์ฑ

 
#### ๋๋์ 
1์ฃผ์ฐจ์ ๋น๊ตํด ํค๋งค๋ ์๊ฐ์ด ์ค์๋ค. ๋ฏ์  ์๋ฌ๋ค์ ๋ง๋ฌ์ ๋ ๊ตฌ๊ธ๋งํ๋ ๋ฒ์ ์กฐ๊ธ์ด๋๋ง ํฐ๋ํ ๊ฒ ๊ฐ๋ค.๐

ํํธ ํ์คํธ ์ฝ๋ ์์ฑ์ ์ด๋ ค์์ ๋๊ผ๋๋ฐ, ์ถ๊ฐ์ ์ผ๋ก JUnit์ ๊ณต๋ถํ๋ฉด์ ์ต์ํด์ง ๋๊น์ง ๋ง๋ค์ด๋ณด๋ ๋ธ๋ ฅ์ด ํ์ํ  ๊ฒ  ๊ฐ๋ค.๐ค
