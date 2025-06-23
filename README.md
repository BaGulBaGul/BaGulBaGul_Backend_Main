# 내가 찾던 페스티벌, 바글바글에서 확인해보세요!

각종 이벤트(패스티벌, 지역행사, 파티)에 대한 정보를 공유하고 함께 참여하거나 직접 파티를 주최하는 커뮤니티입니다.
<img height='500' src='https://github.com/user-attachments/assets/4d52c12b-8df9-4bce-9f5a-06d1a989b3c1'>
<img height='500' src='https://github.com/user-attachments/assets/37f15d1b-c2d9-476e-8f10-a4f45ffbbed7'>
<img height='500' src='https://github.com/user-attachments/assets/3c8709f2-2f8a-4e2a-9b84-93db308aac50'>
<img height='500' src='https://github.com/user-attachments/assets/2a739b23-7668-4353-bbfd-49f062c34934'>
<img height='500' src='https://github.com/user-attachments/assets/b45326b3-d0cd-4f46-95e4-76170f3aeafa'>
<img height='500' src='https://github.com/user-attachments/assets/e592ffc6-5a0d-4ab3-9fae-4e208757f896'>
<img height='500' src='https://github.com/user-attachments/assets/6b1d1415-28ee-41dd-b88c-01a73988c5bc'>
<img height='500' src='https://github.com/user-attachments/assets/dcab0fda-b0f3-4154-80f4-ae4cdedd0458'>
<img height='500' src='https://github.com/user-attachments/assets/525a8b4e-4e45-465b-91db-cc510543a64f'>

자세한 동작이나 모든 화면 구성을 보려면 [프론트 레포지토리](https://github.com/BaGulBaGul/BaGulBaGul_Front)나 [피그마](https://www.figma.com/design/qcBGuCu4VG9foDGuZDNYl3/%ED%8E%98%EC%8A%A4%ED%8B%B0%EB%B2%8C-%EC%9B%B9%EC%82%AC%EC%9D%B4%ED%8A%B8?node-id=0-1&p=f&t=W7qpsVdEEuPeL4xI-0)를 참고해주세요.


<br><br>

# 관련 레포지토리 링크

프론트
<br>
https://github.com/BaGulBaGul/BaGulBaGul_Front
<br><br>
백엔드 실시간 SSE 알람 서버
<br>
https://github.com/BaGulBaGul/BaGulBaGul_Backend_Alarm
<br><br>
설정 서버(환경변수 등)
<br>
https://github.com/BaGulBaGul/BaGulBaGul_Config
<br><br>
배포 관련 코드의 레포지토리
<br>
https://github.com/BaGulBaGul/BaGulBaGul_Deployment
<br>

# erd 다이어그램 링크
https://www.erdcloud.com/d/L3pTvMqaeKJdMw2Xz

# 디자인 figma 링크
https://www.figma.com/design/qcBGuCu4VG9foDGuZDNYl3/%ED%8E%98%EC%8A%A4%ED%8B%B0%EB%B2%8C-%EC%9B%B9%EC%82%AC%EC%9D%B4%ED%8A%B8?node-id=0-1&p=f&t=W7qpsVdEEuPeL4xI-0

<br><br>

# 사용 기술
- 웹 프레임워크
  - Spring Boot
- DB
  - Spring Data
  - mysql
  - redis
- ORM
  - JPA, QueryDSL
- 배치
  - Spring Batch
- 인증
  - Spring Security
  - OAUTH, JWT, COOKIE
- 실시간 알람
  - Spring WebFlux
  - SSE 
  - redis pub/sub
- 배포
  - github actions
  - aws
  - docker
- 기타
  - 테스트
    - JUnit5
    - docker + testcontainers
  - DB 스키마 마이그레이션
    - flyway
  - api 문서화
    - swagger
  - 설정 레포지토리 암호화
    - git-crypt(https://github.com/AGWA/git-crypt)
  
<br>

# 기술적 특징

### 공통 도메인 분리
- event(패스티벌, 지역행사, 파티)와 recruitment(모집글)에서 게시글 기능이 공통으로 사용되었고 코드 중복이 있었습니다.
따라서 게시글 정보를 post(게시글) 도메인으로 분리해서 코드를 재사용하고 유지보수성을 높였습니다.
- 게시글 서비스에서 게시글 이미지 서비스, 댓글 서비스, 검색 서비스, 알람 서비스 등 다양한 서비스와 연결됩니다.
- 또한 공지사항 등의 게시글 기능이 필요한 새로운 기능으로 확장이 쉬워졌습니다.
<br>

### 다양한 검색 기준
- 유저는 제목, 작성자, 카테고리, 태그, 인원수, 장소 등 다양한 조건으로 이벤트와 모집글의 상세 검색이 가능합니다.
- QueryDSL을 이용해 복잡한 동적 쿼리를 작성했습니다. 
또한 게시글에 대한 QueryDSL 쿼리 생성 코드를 분리해서 이벤트와 모집글의 쿼리 생성에 재사용하고 코드 중복을 제거했습니다.
<br>

### 랭킹 서비스
- 최근 1주일간의 조회수, 검색어, 태그 랭킹을 제공합니다
- redis의 sorted set을 이용해 실시간으로 조회수 증가가 반영됩니다
- 매일 배치 작업으로 1주일 전의 조회수를 빼주는 방식으로 최근 1주일간의 조회수만 반영합니다
<br>

### 인증 
- 카카오 oauth를 이용해 인증합니다.
- 회원가입하지 않은 유저라면 회원가입 페이지로 이동 후 추가정보를 작성하고 회원가입 합니다.
- 회원가입한 유저라면 user id를 담은 access token, refresh token을 jwt로 발급합니다.
- jwt를 쿠키에 https only, secure로 저장합니다. 알람서버 등의 다른 서버와 jwt를 공유합니다.
- 요청을 받으면 OncePerRequestFilter가 쿠키에서 jwt의 user id를 추출해서 security context에 저장합니다.
<br>

### 이미지 업로드
- 유저가 업로드 서비스에 이미지를 업로드하면 aws s3에 업로드되고 임시 자원인 TempResource로 등록됩니다.
- 클라이언트에서 게시글, 유저 api 등에 이미지를 등록하면 게시글 이미지 서비스, 유저 이미지 서비스 등에서 영구 자원인 Resource로 전환합니다.
- 영구 자원은 등록한 이미지 서비스에서 관리하고, 기간이 만료된 임시 자원은 배치 작업이 주기적으로 삭제합니다.
- TransactionSynchronizationManager를 이용해 다양한 트랜젝션 실패에서도 db와 s3의 자원 정보가 일치하도록 했습니다.  
<br>

### 알람
- 댓글, 답글이 달리거나 좋아요를 받는 등 다양한 상황에서 유저에게 알람을 보냅니다. 유저는 마이페이지에서 알람을 확인 가능합니다.
<br>

### 실시간 알람
|![bagulbagul_alarm drawio](https://github.com/user-attachments/assets/1f3fd483-a1b9-4093-8f6f-721b5d2a6e82)|
|:------:|
|전체 흐름|

|![alarm_inner drawio](https://github.com/user-attachments/assets/a7cc780b-63d1-4141-aa47-9e9e9d2ff101)|
|:------:|
|실시간 알람 서버 구조|
- 유저는 [실시간 알람 서버](https://github.com/BaGulBaGul/BaGulBaGul_Backend_Alarm)에 알람을 구독합니다.
- 실시간 알람 서버는 구독 요청을 받으면 클라이언트와 SSE 연결을 생성하고 redis pub/sub 에서 해당 유저의 토픽을 구독합니다.
  - 실시간 알람 서버는 netty와 WebFlux를 이용해 비동기적으로 동작하며, 고정 세션을 이용해 scale out 상황에서도 유저와의 SSE 연결을 유지합니다.
- 메인 서버는 알람 생성 시 redis pub/sub 에서 해당 user id 토픽에 새로운 알람 정보를 담은 메세지를 발행합니다
- 실시간 알람 서버는 redis pub/sub에서 메세지를 받아서 구독한 유저에게 SSE 메세지를 보냅니다.
- 유저는 실시간으로 알람 내용 팝업을 브라우저에서 보게 됩니다.
- redis pub/sub에서 메세지는 즉시 사용되고 구독자가 없으면 버려지므로 실시간 알람에 적합합니다.

### 어플리케이션 이벤트를 활용
- 알람 발행이나 게시글의 좋아요 개수, 조회수 등의 통계 작업이나 랭킹 업데이트 등의 작업을 위해 어플리케이션 이벤트를 적극적으로 활용했습니다.<br>
이벤트 생성, 이벤트 좋아요 추가, 댓글 추가, 조건검색 등의 다양한 상황에서 어플리케이션 이벤트를 발행합니다.<br>
이로써 핵심 로직이 다른 부가 기능에 직접적인 의존성을 갖지 않고 관심사를 분리해 자유룝게 확장 가능하도록 했습니다.
- 예를 들면 이벤트 상세 조회 시 QueryPostDetailByUserApplicationEvent를 상속받은 QueryEventDetailByUserApplicationEvent가 발행됩니다.<br> 
ListenerForRanking과 PostStatisticsService가 이 어플리케이션 이벤트를 받아서 실시간 조회수 랭킹과 게시글 조회수 통계를 업데이트합니다.
<br>

### 응답 구조 설계
- 자체적인 api 응답 구조와 응답 코드를 정의해서 다양한 상황에서도 클라이언트가 일관된 처리가 가능하도록 했습니다.
- ResponseEntityExceptionHandler를 상속받는 controller advice를 정의해서 스프링의 기본 예외 처리를 이용하면서 필요한 예외 처리만 재정의하는 방식으로 확장했습니다. 
또한 예외의 경우에도 직접 정의한 응답 구조에 응답 코드를 담아 보내도록 했습니다.
<br>

### 테스트
- testcontainers를 이용해 mysql, redis를 docker container로 실행해서 서로 다른 환경에서도 테스트의 일관성을 확보했습니다.
<br>

### 배포  

| ![bagulbagul_aws drawio](https://github.com/user-attachments/assets/e9305697-9c90-40ff-bddb-98f7ae3903c8) |
|:-------------------------------:|
| 전체 구조 |

| ![bagulbagul_cicd drawio](https://github.com/user-attachments/assets/6c68f12a-62aa-4e2a-ad2c-a7788a2c858e) |
|:-------------------------------:|
| CI/CD 흐름|

- github actions를 이용해 main과 develop 브랜치에 pull request가 발생하면 코드를 자동으로 테스트합니다.
- 테스트, 배포에 사용될 환경변수는 git-crypt로 암호화되어 [설정 레포지토리](https://github.com/BaGulBaGul/BaGulBaGul_Config)에서 관리됩니다. 
- 배포 관련 모든 코드는 [배포 레포지토리](https://github.com/BaGulBaGul/BaGulBaGul_Deployment) 에 있습니다.
- main에 push되면 자동으로 빌드-테스트-배포 파이프라인을 수행하고 블루-그린 배포를 합니다.(위의 'CI/CD 흐름' 이미지)<br>
  1\. 개발자가 main 브랜치에 push합니다.<br>
  2\. 설정 레포지토리를 clone하고 복호화해서 환경변수를 추출하는 docker 이미지 clone-decrypt_config-repository를 docker hub에서 가져옵니다.<br>
  3\. clone-decrypt_config-repository를 이용해 테스트에 필요한 환경변수를 추출합니다.<br>
  4 ~ 5\. 테스트를 수행하고 통과한다면 docker 이미지를 빌드해서 docker hub에 업로드합니다<br>
  6\. green 환경에 인스턴스를 증가시키도록 요청합니다<br>
  7\. 새로 생성된 인스턴스는 Deployment 레포지토리를 clone해서 시작 스크립트를 받아옵니다.<br>
  8 ~ 10\. 환경변수 추출과 어플리케이션 실행을 위한 docker 이미지를 docker hub에서 받아오고 실행합니다.<br>
  11\. green이 정상 요청을 받을 수 있을 때까지 대기합니다.<br>
  12\. green의 상태가 healthy라면 트래픽을 blue에서 green으로 전환합니다.<br>
  13\. blue의 인스턴스를 정리합니다.<br>
  14 ~ 15\. green과 blue의 이름을 서로 바꿉니다(태그를 이용)<br>

