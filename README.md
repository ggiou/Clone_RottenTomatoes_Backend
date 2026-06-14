# 🍅 Clone Rotten Tomatoes Backend

영화 평점 사이트 Rotten Tomatoes를 클론한 백엔드 프로젝트입니다.

영화 정보, 예고편, 평점, 리뷰 기능을 제공하며,
영화 데이터 수집을 위해 Selenium 기반 크롤링 기능을 구현하였습니다.

<img width="1710" height="1107" alt="main" src="https://github.com/user-attachments/assets/5fd55230-80cc-4ff1-a465-c485ff2f8e3f" />

<br>

## 🛠 Tech Stack

### Backend

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)
![Spring Data JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?style=for-the-badge)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-0769AD?style=for-the-badge)
![Selenium](https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle)

### DevOps

![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions)

<br>

## 📌 Project Overview

Rotten Tomatoes의 핵심 기능을 구현한 영화 정보 서비스입니다.

사용자는 영화 정보를 조회하고,
평점 및 리뷰를 작성할 수 있으며,
예고편 및 영화 데이터를 확인할 수 있습니다.

영화 및 예고편 정보는 Selenium 기반 크롤러를 통해 수집됩니다.

<br>

## 📂 Architecture

```text
Client
   │
   ▼
Spring Boot API
   │
   ├── Movie Domain
   ├── Review Domain
   ├── Rating Domain
   ├── User Domain
   │
   ▼
MySQL

   ▲
   │
Selenium Crawling
(Youtube / Movie Data)
````

<br>

## 🗄 ERD

<img width="3060" height="762" alt="Rottentomatoes Clone Erd" src="https://github.com/user-attachments/assets/c007cfdb-e484-4138-94be-6500115ffcd6" />

ERD 상세 설계

* Movie
* MovieTrailer
* Review
* User
* Rating

등의 엔티티 관계를 기반으로 설계되었습니다.

<br>

## 🎨 Figma

서비스 화면 설계

<img width="7951" height="10987" alt="로튼토마토" src="https://github.com/user-attachments/assets/6e2033fb-bf51-45f9-92c5-693ddfbecd57" />

<br>

## ✨ Features

### 👤 Member

* 일반 로그인
* 인증 코드 로그인
* Google OAuth 로그인
* 회원 등록 여부 확인
* 회원 정보 조회
* 로그아웃

---

### 🎬 Movie

* 영화 상세 조회
* 추천 영화 목록 조회
* 영화 목록 조회 (정렬 지원)
* 장르별 영화 조회
* 영화 검색
* 영화 정보 저장
* 영화 예고편 조회

---

### ⭐ Likes

* 영화 좋아요 등록
* 영화 좋아요 취소
* 좋아요 여부 확인

---

### 📌 Saved

* 영화 저장하기 등록
* 영화 저장하기 취소
* 저장 여부 확인

---

### 📝 Review

* 리뷰 작성
* 영화별 리뷰 전체 조회
* 리뷰 목록 조회
* 리뷰 상세 조회
* 리뷰 수정
* 리뷰 삭제

---

### 🙋 My Page

* 회원 정보 조회
* 좋아요한 영화 목록 조회
* 저장한 영화 목록 조회
* 작성한 리뷰 목록 조회

---

### 🕷 Crawling

* Selenium 기반 영화 데이터 수집
* YouTube 예고편 자동 수집
* 영화 상세 정보 크롤링


<br>

## 📁 Package Structure

```text
com.clone.rottentomato

├── common
│
├── config
│
├── crawling
│
├── domain
│   ├── movie
│   ├── review
│   ├── rating
│   └── user
│
├── security
│
└── util
```

<br>
