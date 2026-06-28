package hoon.football.joinrequest.service.impl;

import hoon.football.joinrequest.domain.TeamJoinRequest;
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
        Team team = teamService.createTeam("teamA", savedMember.getId());
        TeamJoinRequest request = tjrService.createRequest(savedMember, team);
        TeamJoinRequest request2 = tjrService.createRequest(savedMember2, team);

        // when
        List<TeamJoinRequest> allRequests = tjrService.findAllRequestsByTeamId(team.getId());

        // then
        assertThat(allRequests).hasSize(2);
        assertThat(allRequests).contains(request, request2);

        assertThat(request.getMember()).isEqualTo(savedMember);
        assertThat(request.getTeam()).isEqualTo(team);

        assertThat(request2.getMember()).isEqualTo(savedMember2);
        assertThat(request2.getTeam()).isEqualTo(team);

    }

}