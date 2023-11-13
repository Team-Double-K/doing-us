
## 팀명

- double-k(더블 케이)

## 프로젝트 이름
- doingUs(두잉어스)
  
  <img src="https://github.com/Team-Double-K/doing-us/assets/126179088/e5d539bc-bc35-47ee-a55e-3af7070d6ba7" alt="image" width="80px">
  <img src="https://github.com/Team-Double-K/doing-us/assets/126179088/9db52fd7-b3e3-4ee2-8770-d6da25074ef7" alt="image" width="80px">
  <img src="https://github.com/Team-Double-K/doing-us/assets/126179088/3249e4ae-a998-4965-ae90-0c840e6a8c22" alt="image" width="80px">
  <img src="https://github.com/Team-Double-K/doing-us/assets/126179088/4af9900b-8e9d-4c2a-b05a-d06a1b712262" alt="image" width="80px">
  <img src="https://github.com/Team-Double-K/doing-us/assets/126179088/f32d1516-9a1d-41fb-b6f9-e50a67e3040e" alt="image" width="80px">
  <img src="https://github.com/Team-Double-K/doing-us/assets/126179088/f83f9bff-26ce-4bbc-8454-e81e539b151a" alt="image" width="80px">

  *위 사진은 "로그인, 회원가입, 메인페이지, 모임찾기, 모임생성, 내가 현재 속한 모임" 순서로 UI가 배치되어있습니다.*

- 시연영상 url(youtube)

  [https://www.youtube.com/watch?v=pzGHxS9zHoY](https://youtu.be/MuZipRTFFbM?si=6F4ejZWnf4BUEFOf)
## 목차

- [1. 제출 타입 및 주체](#1-제출-타입-및-주제)
- [2. 프로젝트 한 줄 설명](#2-프로젝트-한-줄-설명)
- [3. 프로젝트에서 활용된 기술](#3-프로젝트에서-활용된-기술)
  - [3-1. restful 구조 적용](#3-1-restful-구조-적용)
  - [3-2. retrofit을 이용한 의도적인 백엔드와의 연결](#3-2-retrofit을-이용한-의도적인-백엔드와의-연결)
  - [3-3. KakaoMap API 연결 및 지도 상에 마커 표시로 사용자에 대한 가독성 향상](#3-3-kakaomap-api-연결-및-지도-상에-마커-표시로-사용자에-대한-가독성-향상)

#

## 1. 제출 타입 및 주제

- 타입

  - C 타입 (포스트 코로나 시대에 혼란을 해결하는 SW 개발)

- 주제
  - 모임 생성 및 활동을 함께 할 내 주변 친구를 찾을 수 있는 서비스

## 2. 프로젝트 한 줄 설명

- 단절 문제 회복을 위해 모임 데이터를 적재 제공하여 사용자들 간 소통 촉진

## 3. 프로젝트에서 활용된 기술

### 3-1. restful 구조 적용

- API Endpoints

  | Endpoint                   | Method | Description                                                     |
  | -------------------------- | ------ | --------------------------------------------------------------- |
  | /auth/join                 | POST   | 유저 회원가입                                                   |
  | /auth/login                | POST   | 유저 로그인                                                     |
  | /auth/logout               | GET    | 유저 로그아웃                                                   |
  | /user                      | GET    | 유저 전체 리스트                                                |
  | /user/:userId              | GET    | 유저 1명의 상세 정보 리스트                                     |
  | /user/:userId/room         | GET    | 유저가 속한 모든 room 정보 리스트                               |
  | /user/:userId/room/:roomId | GET    | 유저가 속한 room 1개에 대한 상세 정보                           |
  | /user/:userId/room         | POST   | 방 만들기                                                       |
  | /user/:userId/room/:roomId | POST   | 방 참가하기                                                     |
  | /user/:userId/room/:roomId | DELETE | 방 삭제 및 나가기(사용자가 default User, Owner인지에 따라 구분) |
  | /user/:userId/room/:roomId | PUT    | 방에 대한 정보를 수정(Owner만)                                  |
  | /room                      | GET    | 모든 room의 정보를 얻음                                         |

  - 인증이 필요한 부분은 retrofit을 통해 header의 authorization부분을 통해 토큰을 넘겨주며 인증된 유저인지를 구분합니다.
  - crypto모듈과 JWT 모듈을 통해서 사용자에 대한 중요한 정보는 모두 암호화되어 저장되게 됩니다.

  - URL를 restful하게 설계하여, URL내에 가독성을 부여합니다.

  - room에 대해 CRUD 형태를 지원하여, 좀 더 유기적으로 사용자가 앱에 접근할 수 있습니다.

  - 또한 모든 오류 기저에 대해서 응답할 수 있게 처리해서 오류로 인해 서버가 꺼지는 경우를 최소화했습니다. (추가적으로 AWS올리고 PM2를 통해 백그라운드로 돌아가게 해서, 24시간 서버가 실행 될 수 있는 구조를 만들었습니다.)

### 3-2. retrofit을 이용한 의도적인 백엔드와의 연결

- 모바일에서 백엔드와 연결하기 위해 retrofit을 활용하였습니다. 인증이 필요한 부분에서는 header 파트에 sharedPreference를 통해 유지시킨 토큰을 백엔드로 넘겨서 보안성을 강화하였습니다.

- retrofit을 통한 웹기반 백엔드와의 통신으로 여러 백그라운드의 프로그램간의 이식성을 향상 시켰습니다.

- retrofit을 이용하여 백엔드를 의도적으로 둠으로써, 데이터에 대한 모든 처리는 백엔드가 처리하고, 프론트에서의 리소스 사용량을 최소화 하였습니다.

- 기본적으로 프론트는 지도상에 마커를 띄우고, 지도를 뿌리는 등의 작업 리소스 소모가 꽤나 많이 들어가는 작업입니다. 그렇기에 백과 프론트를 분리하여 모든 정보에 대한 처리는 백으로 인가하였습니다.

### 3-3. KakaoMap API 연결 및 지도 상에 마커 표시로 사용자에 대한 가독성 향상

- 일반적으로 주어진 정보로만 사용자가 식별하기에는 어려움이 존재합니다. 그래서 저희 어플리케이션에서는 KakaoMap API에 모임에 대한 정보를 마커로 표시합니다.

- 각 마커를 누르면 간단한 개요가 보이게 됩니다.

- 해당 개요를 누르게 되면, 해당 마커에 위치한 모임에 대한 상세정보와 참가를 할 것인지에 대한 유무를 사용자에게 묻숩니다.

- 사용자의 GPS 권한을 활용하여, 현재 내 위치를 spot으로 삼아 내 주변에 있는 모임에 대해 한 눈에 알아 볼 수 있도록 처리하였습니다.

- Kakao 맵 API를 활용하여 모임을 생성 시 원하는 위치를 검색하여 방을 만들 수 있도록 처리하였습니다. 사용자가 좀 더 간편하게 검색을 통해 모임을 하는 곳에 위치를 선별할 수 있도록 처리하였습니다.
