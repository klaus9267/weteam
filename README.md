<h2>🎈 Weteam</h2>
<div align="center">
  <p><img src = "https://github.com/user-attachments/assets/96c3be44-1f1e-46be-ae19-297df24167da" ></p>
  <p><b>We are Team!</b></p>
  <p>  프로젝트 일정 관리에 편의성을 제공하는 앱 프로젝트입니다 :)</p>
</div>

## 🎈 기술 스택
### 언어 & 프레임워크
<div style="display: flex; align-items: center; gap: 8px">
  <img src="https://img.shields.io/badge/Java 17-007396?style=flat-square&logo=java&logoColor=white"/>  
  <img src="https://img.shields.io/badge/Spring Boot 3.0-%236DB33F?style=flat-square&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat-square&logo=&logoColor=white"/>
</div>

### 데이터베이스 & 인프라
<div style="display: flex; align-items: center; gap: 8px">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/>
  <img src="https://img.shields.io/badge/Termux-000000?style=flat-square&logo=iTerm2&logoColor=white"/>
</div>

### 소셜 로그인
<div style="display: flex; align-items: center; gap: 8px">
  <img src="https://img.shields.io/badge/Firebase-DD2C00?style=flat-square&logo=Firebase&logoColor=white"/>
</div>

## 🎈 ERD

![erd](https://github.com/klaus9267/weteam/assets/90795904/00b603d8-ed88-4951-bc5e-6a7fb73c7878)

## 🎈 Architecture
### 이전 전
<div align="center">
  
  ![weteam drawio](https://github.com/user-attachments/assets/8d60d79e-db32-4be7-b5c2-0b7104caa08d)
</div>


### 이전 후
<div align="center">
  
  ![제목 없는 다이어그램 drawio](https://github.com/user-attachments/assets/8b7346cb-85a0-4dea-b630-288f01053db0)  
</div>

## 🎈 API 목록

| Domain | URL | Http Method | Description |
|--------|-----|-------------|-------------|
| **ALARM** ||||
|| /api/alarms | GET | 알람 조회 |
|| /api/alarms | PATCH | 알람 전체 읽음 처리 |
|| /api/alarms/{alarmId} | PATCH | 알람 단건 읽음 처리 |
| **COMMON** ||||
|| /api/common | GET | 딥링크용 url 반환 API |
| **FCM** ||||
|| /api/fcm | GET | 디바이스 토큰 조회 |
|| /api/fcm/{token} | PATCH | 디바이스 설정 및 변경 |
| **MEETING** ||||
|| /api/meetings | GET | 약속 목록 조회 |
|| /api/meetings | POST | 약속 생성 |
|| /api/meetings/{meetingId} | GET | 약속 상세 조회 |
|| /api/meetings/{meetingId} | PATCH | 약속 수정 |
|| /api/meetings/{meetingId} | DELETE | 약속 삭제 |
| **MEETING USER** ||||
|| /api/meeting-users/{meetingId} | GET | 약속 초대용 hashedId 조회 |
|| /api/meeting-users/{meetingId}/time | PATCH | 시간 수정 |
|| /api/meeting-users/{meetingId}/quit | PATCH | 약속 탈퇴 |
|| /api/meeting-users/{meetingId}/displayed | PATCH | 약속 목록 조회 시 제외 처리 |
|| /api/meeting-users/{hashedId} | PATCH | 약속 초대 수락 |
| **PROFILE** ||||
|| /api/profiles/{imageIdx} | PATCH | 프로필 사진 변경 |
|| /api/profiles/{imageIdx} | POST | 프로필 사진 생성 |
| **PROJECT** ||||
|| /api/projects | GET | 팀플 목록 조회 |
|| /api/projects | POST | 팀플 생성 |
|| /api/projects/{projectId} | GET | 팀플 단건 조회 |
|| /api/projects/{projectId} | PATCH | 팀플 수정 |
|| /api/projects/{projectId} | DELETE | 팀플 삭제 |
|| /api/projects/{projectId}/{userId} | PATCH | 호스트 넘기기 |
|| /api/projects/{projectId}/done | PATCH | 팀플 진행 상황 변경 |
| **PROJECT USER** ||||
|| /api/project-users | DELETE | 팀원 강퇴 |
|| /api/project-users | PATCH | 담당 역할 변경 |
|| /api/project-users/{projectId} | GET | 팀원 목록 조회 |
|| /api/project-users/{projectId} | DELETE | 팀플 탈퇴 |
|| /api/project-users/{projectId}/invite | GET | 팀원 초대용 hashedId 조회 |
|| /api/project-users/{hashedProjectId} | PATCH | 초대 수락 |
| **USER** ||||
|| /api/users | GET | 내 정보 조회 |
|| /api/users | DELETE | 사용자 탈퇴 |
|| /api/users/{id} | GET | 다른 사용자 조회 |
|| /api/users/{id} | DELETE | 사용자 강제 삭제(개발용) |
|| /api/users | PATCH | 사용자 정보 변경 |
|| /api/users/push | PATCH | 푸시 알람 수신 활성화/비활성화 |
|| /api/users/logout | PATCH | 로그아웃 |
