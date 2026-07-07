# 미니프로젝트 1 

# FootMatch 



## 1차 MVP - 완료

---

## 2차 MVP - 진행중.

## 1. 2차 MVP 에서 구현할 기능들.
- 팀 상세페이지 위주로 구현
  ㄴ 해당 팀이 참여했던 매치들을 확인할 수 있다.
  ㄴ 해당팀이 진행했던 매치 ( 이미 종료된 경기 ) 상세페이지에서, 매치 결과를 확인할 수 있다.
  ㄴ 홈팀으로 참여한 매치만이아닌, 어웨이팀으로도 참여한 매치들을 확인할 수 있다.

---

## 2. 매칭 대기중인 매치 / 진행중인 매치 / 완료된 매치 구분
- 팀 상세 페이지의 매치탭에서, 매치를 상태별로 확인할 수 있다.

- 대기중인 매치
  ㄴ `TeamMatchStatus` = `PENDING`
  ㄴ 상대팀이 아직 확정안된상태 ( `awayTeam` 존재 x )

- 진행중인 매치
  ㄴ `TeamMatchStatus` = `MATCHED`
  ㄴ 상대팀이 확정됐고 ( `awayTeam` 존재 ) 경기결과를 아직 입력하지 않은상태.

- 완료된 매치
  ㄴ `TeamMatchStatus` = `COMPLETED`
  ㄴ 경기결과를 입력해, 매치가 종료된 상태.

---

## 3. 완료된 매치 상세 페이지

- 완료된 매치를 클릭하면 매치 상세페이지로 이동한다.

- 매치 상세페이지에서는 다음 정보들을 확인할 수 있다.
  ㄴ 홈팀
  ㄴ 어웨이팀
  ㄴ 홈팀 점수
  ㄴ 어웨이팀 점수

완료된 매치에는, 결과를 수정하지 못한다.

추후에, 확인할 수 있는 정보들은 늘어날 수 있다.

---

## 4. 2차 MVP 정책

- 팀 매치 조회
  ㄴ 홈팀으로 참여한 매치들을 조회할 수 있다. ( 자신의팀이 매치를 등록한 경우 )
  ㄴ 어웨이팀으로 참여한 매치들을 조회할 수 있다. ( 자신의팀이 이미 등록되어져있는 매치에 매치요청을 보낸경우 )
  ㄴ 특정 팀 상세페이지에서는, 해당팀의 매치들만 확인할 수 있다.
  ㄴ 매칭 대기중인 매치 / 진행중인 매치 / 완료된 매치 는 각각 구분해서 확인할 수 있다.

- 매칭 대기중인 매치
  ㄴ 아직, 홈팀만 존재하고 어웨이팀은 없는상태.
  ㄴ `TeamMatchStatus` = `PENDING`

- 진행중인 매치
  ㄴ 매치가 성공적으로 잡혀, 홈팀과 어웨이팀이 존재하는 상태.
  ㄴ `TeamMatchStatus` = `MATCHED`
  ㄴ 이 경우에만 매치 결과를 입력할 수 있다.

- 완료된 매치
  ㄴ 매치가 성공적으로 종료된상태.
  ㄴ `TeamMatchStatus` = `COMPLETED`
  ㄴ 완료된 매치의 경기결과를 확인할 수  있다.
  ㄴ 매치가 완료되고나면 ( = 경기결과를 입력하면 ), `rating` 변경사항을 반영한다.

---

## 5. 우선순위

- 해당 팀이 참여했던 매치들 조회

- 매치상태에 따른 매치들 구분 ( `PENDING`, `MATCHED`, `COMPLETED` )

- 완료된 매치 상세페이지 구현

---

## 6. 정리

- 팀 상세페이지에서, 팀이 참여한 매치 기록들을 조회할 수 있고, 완료된 경기 결과를 확인할 수 있게 하는게 2차 MVP 의 완료기준이다.


--- 

## 7. 현재 팀 상세페이지 HTML

- 정보확인

![](https://velog.velcdn.com/images/qlrxn1152/post/209cc9f4-33a7-4a3e-b1be-5158b88875dd/image.png)

---

- 팀원확인

![](https://velog.velcdn.com/images/qlrxn1152/post/9b74524e-c3d3-4af9-bae5-e2a2c148168a/image.png)

---

- 매치확인

![](https://velog.velcdn.com/images/qlrxn1152/post/05fceccd-ca53-47a0-92fb-489d14d8bb1b/image.png)

--- 

## GITHUB
[GITHUB](https://github.com/qlrxn1152/football)






---

## 해당 프로젝트 용어사전

- 점수 : `rating`
- PK : `테이블명_id`
- 검증 메서드 이름 : `validateForXxx` ( Xxx : 무엇을 검증하는지 )
  - ㄴ 예시 : 팀 리더인지 체크 => `validateForTeamLeader`
- 홈팀 아이디 : `home_team_id`
- 어웨이팀 아이디 : `away_team_id`
- 상태 : `테이블명_status`
  - ㄴ 예시 : 매치 상태 => `team_match_status`
  - ㄴ 예시 : 가입신청 상태 => `team_request_join_status`
  - ㄴ 예시 : 매치 요청 상태 => `team_match_request_status`


