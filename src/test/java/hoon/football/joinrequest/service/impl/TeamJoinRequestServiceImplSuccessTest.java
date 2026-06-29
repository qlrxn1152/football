package hoon.football.joinrequest.service.impl;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.joinrequest.domain.TeamJoinRequestStatus;
import hoon.football.joinrequest.repository.TeamJoinRequestRepository;
import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.service.TeamService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TeamJoinRequestServiceImplSuccessTest {

    @Autowired TeamJoinRequestService tjrService;
    @Autowired MemberService memberService;
    @Autowired TeamService teamService;

    @Test
    @DisplayName(value = "요청생성_성공")
    void createRequest_success() throws Exception {
        // given
        Member savedMember = memberService.save(new Member("userA", "1234"));
        Member savedMember2 = memberService.save(new Member("userB", "1234"));

        Team team = teamService.createTeam("teamA", savedMember.getId()); // userA 가 팀을 생성

        TeamJoinRequest request = tjrService.createRequest(savedMember2.getId(), team.getId()); // userB 가 userA 팀에 가입신청

        // when
        List<TeamJoinRequest> allRequests = tjrService.findAllRequestsByTeamId(team.getId());

        // then
        assertThat(allRequests).hasSize(1);

        // 가입신청을 넣은상태에서는, member 는 팀에 속해있지않음 ( 팀장의 수락이전 )
        assertThat(savedMember2.getTeam()).isNull();
        assertThat(request.getMember()).isEqualTo(savedMember2);
        assertThat(request.getStatus()).isEqualTo(TeamJoinRequestStatus.PENDING);
    }


    @Test
    @DisplayName(value = "해당 팀 요청 status 가 PENDING 인 요청들 모두 조회")
    void findAllRequest_pending() throws Exception {
        // given
        Member userA = memberService.save(new Member("userA", "1234"));
        Member userB = memberService.save(new Member("userB", "1234"));
        Member userC = memberService.save(new Member("userC", "1234"));

        Team team = teamService.createTeam("teamA", userA.getId()); // userA 가 팀을 생성
        // when
        TeamJoinRequest request = tjrService.createRequest(userB.getId(), team.getId()); // userB 가 teamA 에 가입신청
        TeamJoinRequest request2 = tjrService.createRequest(userC.getId(), team.getId()); // userC 가 teamA 에 가입신청
        List<TeamJoinRequest> requests = tjrService.findAllRequestsByTeamId(team.getId()); // teamA 에 가입신청대기중인 TeamJoinRequest 들을 가지고옴 ( status = PENDING )

        // then
        assertThat(requests).hasSize(2);
        assertThat(requests.get(0).getStatus()).isEqualTo(TeamJoinRequestStatus.PENDING);
        assertThat(requests).contains(request, request2);
    }

    // 테스트코드 확인할거 -> 1. 팀장만 가능한지 / 2. 수락하고나서 status = ACCEPTED 로 바뀌는지, / 3. 수락하고나서 팀에 멤버추가됐는지 / 4. 수락하고나서 멤버에 팀이 추가됐는지
    @Test
    @DisplayName(value = "가입신청 수락성공")
    void acceptRequest_success() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Team team = teamService.createTeam("teamA", memberA.getId()); // memberA -> teamA 팀 생성, memberA = leaderMember
        Member memberB = memberService.save(new Member("memberB", "1234"));
        TeamJoinRequest request = tjrService.createRequest(memberB.getId(), team.getId()); // memberB -> teamA 가입신청. ==> 팀에는 아직 소속 x

        Member memberC = memberService.save(new Member("memberC", "1234"));
        TeamJoinRequest memberCRequest = tjrService.createRequest(memberC.getId(), team.getId()); // memberC -> 가입신청 -> 현재 대기상태

        // when
        tjrService.acceptRequest(memberB.getId(), team.getId(), memberA.getId()); // memberA 가, memberB 가 신청한 요청을 수락.


        // then
        assertThat(request.getStatus()).isEqualTo(TeamJoinRequestStatus.ACCEPTED); // 수락 -> status = ACCEPTED
        assertThat(team.getMembers()).contains(memberA, memberB); // 수락 -> 팀에 멤버 추가
        assertThat(memberB.getTeam()).isEqualTo(team); // 수락 -> 멤버에 팀 추가
        assertThat(team.getMembers()).hasSize(2); // memberA, memberB

        assertThat(memberC.getTeam()).isNull();
        assertThat(team.getMembers()).doesNotContainSequence(memberC);
        assertThat(memberCRequest.getStatus()).isEqualTo(TeamJoinRequestStatus.PENDING);
    }

    // 테스트코드 확인할거 -> 1. 팀장만 가능한지 / 2. 수락하고나서 status = REJECTED 로 바뀌는지 / 3. 거절하고나서 팀에 멤버 추가 안되는거 맞는지 / 4. 거절하고나서 멤버에 팀이 추가 안되는거 맞는지
    @Test
    @DisplayName(value = "가입신청 거절성공")
    void rejectRequest_success() throws Exception {
        // given
        Member memberA = memberService.save(new Member("memberA", "1234"));
        Member memberB = memberService.save(new Member("memberB", "1234"));
        Member memberC = memberService.save(new Member("memberC", "1234"));

        Team team = teamService.createTeam("teamA", memberA.getId()); // memberA -> teamA 팀 생성, memberA = leaderMember

        TeamJoinRequest memberBRequest = tjrService.createRequest(memberB.getId(), team.getId()); // memberB -> teamA 가입신청. ==> 팀에는 아직 소속 x
        TeamJoinRequest memberCRequest = tjrService.createRequest(memberC.getId(), team.getId());
        tjrService.acceptRequest(memberB.getId(), team.getId(), memberA.getId()); // memberA -> memberB 가입신청 수락 -> memberB 의 Team = teamA

        // when
        tjrService.rejectRequest(memberC.getId(), team.getId(), memberA.getId()); // memberA -> memberC 가입신청 거절

        // then
        assertThat(memberCRequest.getStatus()).isEqualTo(TeamJoinRequestStatus.REJECTED);
        assertThat(memberC.getTeam()).isNull();
        assertThat(team.getMembers()).doesNotContainSequence(memberC);

        assertThat(team.getMembers()).hasSize(2); // memberA, memberB
    }

}