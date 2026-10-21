# mind-share-api

전문가와 학습자가 관심 분야(과학, 수학, 예술)별로 글과 답글을 주고받는 지식 공유 게시판의 백엔드 API입니다.

## 주요 기능
- 전문가, 학습자 유형별 회원 가입과 관심 카테고리 선택
- 이메일 또는 휴대폰 번호 로그인, Access Token, Refresh Token 기반 인증 (Refresh Token은 Redis에 저장)
- 카테고리별 게시글, 답글 작성과 검색, 페이징, 조회수 (쿠키로 중복 조회 방지)

## 기술 스택
Java 17, Spring Boot 3.2, Spring Security, JWT, JPA, QueryDSL, MySQL, Redis (Redisson), Gradle

## 모듈 구조
| 모듈 | 역할 |
| --- | --- |
| app-platform-api | API 진입점, 컨트롤러, 서비스 |
| domain-user | 회원, 회원 유형, 관심 카테고리 도메인 |
| domain-board | 게시글, 답글 도메인 |
| domain-system | 카테고리 도메인 |
| client-core | JWT 발급과 검증, Security 핸들러, 공통 응답과 예외 처리, JPA, Redis 설정 |
| system-core | 공통 예외 정의, 유틸리티 |

## 설계 포인트
- 도메인별로 모듈을 나눠 의존 방향을 API → 도메인 → 코어로 고정
- 동적 검색 조건은 QueryDSL로 처리
- 토큰 재발급 정보를 Redis에 두어 만료와 무효화를 단순화

---
### 프로젝트 실행 방법
  1. **개발 환경**
     * `JDK 17`
     * `spring boot 3.2.0`
  2. **DB 세팅**
     * `mysql`
       * port: `3306`
       * database
         * name: `mindshare`
         * username: `root`
         * password: `1234`
     * `redis`
       * port: `6379`
       * password: `1234`
  3. **QueryDSL QFile 생성**
     * ~/mind-share-api에서 아래 커맨드 실행
       * `./gradlew clean build`
  4. **기타**
     * 게시글 상세 API의 조회수 중복 체크를 `cookie`로 하고 있어 쿠키 허용 해야함
     * refresh token을 request header의 `X-Refresh-Token`에 담아 넘겨줘야 토큰 재발급 가능함
     * 날짜/시간 format
       * ~Date: `yyyy-MM-dd`
         * `ex) "birthDate": "2000-01-01"`
       * ~Datetime: `yyyy-MM-dd'T'HH:mm:ss.SSS'Z'`
         * `ex) "alarmAgreedDatetime": "2024-11-02T03:48:26.707Z"`

<br>

### API 엔드포인트 목록 및 간단한 설명

> AUTH
- POST `/v1/auth/join` 
    - 회원가입 API
        - userType(enum): {전문가,학습자}
        - category(seed data): {과학,수학,예술}
  - 로그인에 필요한 토큰 response 
  - 유저별로 관심 분야 설정할 수 있음(nullable)


- POST `/v1/auth/login`
    - 로그인 API
    - 회원가입시 입력한 이메일주소 또는 핸드폰번호로 로그인
      - loginType(enum): {PHONE,EMAIL}
    - 로그인에 필요한 토큰 response


- POST `/v1/auth/duplicate-check/email`
    - 이메일 중복체크 API
    - 회원가입 또는 사용자 정보 수정 전 호출 필요


- POST `/v1/auth/duplicate-check/phone`
    - 핸드폰 번호 중복체크 API
    - 회원가입 또는 사용자 정보 수정 전 호출 필요


- POST `/v1/auth/reissue-token`
    - 토큰 재발급 API
    - `EXPIRED_ACCESS_TOKEN` 에러 발생 시 호출 필요
    - 헤더의 `X-Refresh-Token`에 담긴 토큰과 `redis`에 저장된 사용자의 토큰이 같아야만 재발급
        - 없거나 다르면 `EXPIRED_REFRESH_TOKEN` 에러 발생 -> 재로그인 필요


> ME
- GET `/v1/users/me`
    - 사용자 정보 상세 API
    - access token으로 유저 특정하여 정보 조회


- PATCH `/v1/users/me`
    - 사용자 정보 수정 API
    - access token으로 유저 특정하여 정보 수정
    - 변경된 정보만 request로 전송 
        - 들어오지 않은 필드값은 변경 X


> ARTICLE
- POST `/v1/board/articles`
    - 게시글 작성 API
    - 분야별 게시글 작성
        - category(seed data): {과학,수학,예술}
    - parentId에 articleId가 있으면 답글 작성
        - 원글과 동일 분야여야 함


- GET `/v1/board/articles/{articleId}`
    - 게시글 조회 API
    - `viewedArticles`cookie에 articleId를 넣어 조회수 무제한 증가 차단 
    - 답글이 있는 경우 상세에 같이 보여짐


- PATCH `/v1/board/articles/{articleId}`
    - 게시글 수정 API
    - 본인 작성 글만 수정 가능
    - 제목/내용만 수정


- DELETE `/v1/board/articles/{articleId}`
    - 게시글 삭제 API
    - 본인 작성 글만 삭제 가능
    - 원글 삭제시 답글도 함께 삭제


- GET `/v1/board/articles`
    - 게시글 목록 API
    - 목록에는 원글만 표시
    - `category` query string으로 분야별 조회 가능
    - `pagination` 구현
        - `page`: 조회할 페이지(1부터 시작)
        - `size`: 페이지 당 게시글 개수
        - `sort`: 정렬기준과 방향
            - `ex)title,desc`


- GET `/v1/board/articles/search`
    - 게시글 검색 API
    - 목록에는 원글만 표시
    - `QueryDSL` 사용 및 `pagination` 구현
    - query string으로 검색값 전달
        - equal 검색
          - `category, hasChildren` 
        - like 검색
          - `authorName:like, keyword:like, title:like, content:like` 
        - 기간 검색
          - `createdDateGte, createdDateLte` 

<br>

### 고민한 점이나 아쉬운 점

**고민한 점**
1. 게시글 원글-답글 삭제 시 답글 처리
    1. 원글 삭제를 soft delete로 구현하고 답글은 살려둘까 고민하다가 삭제된 원글의 답글 확인은 사용자 정보 상세에 본인 작성글/답글 목록 노출까지 고려해야 해서 범위가 커지므로 보류
   2. 결국 원글 삭제시 답글도 함께 삭제하기로 함
2. 게시글 상세 API에서 답글 접근 처리
   1. 답글 pk로 게시글 상세 API를 요청했을 때, exception을 날리거나 원글 상세 API로 redirect할지 고민
   2. 게시글 목록에는 원글만 노출되므로 답글 pk를 취득하여 답글의 상세 API를 호출하는 일은 적을거라 판단하여 별도로 제한하지 않음
3. 사용자 정보 수정 시 userType 수정
   1. 타입에 따른 권한이 명백할 경우 수정을 막아야겠지만 현재는 그렇지 않으므로 열어 놓음

<br>

**개선할 점**
1. 핵심 기능을 우선 구현하고, 아래 부가 기능은 다음 단계로 남겨 둠
   1. userType에 따른 권한 분리
       1. ex) 답글은 전문가만 작성
   2. 게시글 목록 API에 Redis 캐시 적용
   3. 게시글 첨부파일 기능
2. 회원가입 SMS, 이메일 인증은 외부 발송 서비스 연동 비용과 계정 관리 문제로 제외함

---