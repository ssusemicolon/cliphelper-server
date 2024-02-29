<div align="center">
  <img width="25%" height="25%" alt="클립헬퍼 로고" src="https://github.com/ssusemicolon/cliphelper-server/assets/67828333/df643927-dc26-4fcf-a62c-443f083e3e57">
  <h1>'클립헬퍼'</h1>
  <p align="center">
    스크랩 컨텐츠 관리를 도와주는 앱 서비스 '클립헬퍼'의 서버 github입니다.
    <br />
    <a href="https://github.com/ssusemicolon/cliphelper-app"><strong>프론트엔드(front end) 저장소</strong></a><br>
    <a href="https://docs.google.com/presentation/d/1VZTacaWLq6ec5V4QJh-Fo61E0H4_dyiu/edit?usp=share_link&ouid=102251499831159686917&rtpof=true&sd=true"><strong>클립헬퍼 소개 ppt 자료 구글 드라이브 링크</strong></a><br>
    <a href="https://drive.google.com/file/d/10hB0iPVWaGcmJJmxKYoImkHIUzc20dIG/view?usp=share_link"><strong>클립헬퍼 데모 영상</strong></a>
    <br />
  </p>
</div>

<ul>
  <li><a href="#API-명세">API 명세</a></li>
  <li><a href="#package structure">Package Structure</a></li>
  <li><a href="#erd">ERD</a></li>
</ul>

## API 명세

| URI                                             | HTTP METHOD | 기능                            |
|-------------------------------------------------|-------------|-------------------------------|
| /auth/signup                                    | POST        | 회원가입                          |
| /users                                          | GET         | 특정 userId를 가진 회원 조회           |
| /users/username                                 | PATCH       | 회원 닉네임 수정                     |
| /users/picture                                  | PATCH       | 회원 이미지 수정                     |
| /users                                          | DELETE      | 회원 삭제                         |
| /users/profile                                  | GET         | 회원 프로필 조회                     |
| /users/alarms                                   | GET         | 회원의 알람 희망 시간대 목록 조회           |
| /users/alarms/setting?status={status}           | PATCH       | 회원의 알람 기능 활성화/비활성화            |
| /users/deviceToken                              | POST        | 회원의 디바이스 토큰 저장                |
| /users/alarms?alarmTime={alarmTime}             | PATCH       | 회원 알람 희망 시간대 추가               |
| /users/alarms/{alarmId}                         | DELETE      | 회원 알람 희망 시간대 삭제               |
| /articles                                       | POST        | 새 스크랩 컨텐츠 등록                  |
| /articles/{articleId}                           | GET         | 특정 articleId를 가진 스크랩 컨텐츠 조회   |
| /articles                                       | GET         | 내 스크랩 컨텐츠 전체 조회               |
| /articles/{articleId}                           | PATCH       | 특정 articleId를 가진 스크랩 컨텐츠 수정   |
| /articles/{articleId}                           | DELETE      | 특정 articleId를 가진 스크랩 컨텐츠 삭제   |
| /articles/{articleId}/collections               | PATCH       | 특정 아티클이 속해 있는 컬렉션 편집          |
| /collections                                    | POST        | 새 컬렉션 등록                      |
| /collections/{collectionId}                     | GET         | 특정 collectionId를 가진 컬렉션 조회    |
| /collections                                    | GET         | 내 컬렉션 목록 조회                   |
| /collections/{collectionId}                     | PATCH       | 특정 collectionId를 가진 컬렉션 정보 수정 |
| /collections/{collectionId}/article/{articleId} | DELETE      | 특정 컬렉션의 아티클을 컬렉션에서 삭제         |
| /collections/{collectionId}                     | DELETE      | 특정 collectionId를 가진 컬렉션 삭제    |
| /bookmarks                                      | POST        | 특정 컬렉션 북마크하기                  |
| /bookmarks                                      | GET         | 내 북마크 컬렉션 목록 조회               |
| /bookmarks/{bookmarkId}                         | DELETE      | 특정 컬렉션 북마크 해제                 |
| /admin/users                                    | GET         | 전체 회원 조회                      |
| /admin/articles                                 | GET         | 모든 스크랩 컨텐츠 조회                 |
| /admin/collection                               | GET         | 모든 컬렉션 조회                     |


## ERD
[erdcloud 링크](https://www.erdcloud.com/d/9eS7mDZv8jc4S2G9C)

<img width="1488" alt="image" src="https://github.com/ssusemicolon/cliphelper-server/assets/67828333/55063713-b62c-4ccc-a8b3-6de0bc95ad22">



