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

}