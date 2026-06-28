package hoon.football.team.service.impl;

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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TeamServiceImplTest {

    @Autowired MemberService memberService;
    @Autowired TeamService teamService;

    @Test
    @DisplayName(value = "팀생성_성공")
    void createTeam_success() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member);

        // when
        Team savedTeam = teamService.createTeam("teamA", savedMember.getId());

        // then
        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getLeaderMember()).isEqualTo(savedMember);
        assertThat(savedTeam.getTeamName()).isEqualTo("teamA");
        assertThat(savedTeam.getRating()).isEqualTo(1000);
    }

}