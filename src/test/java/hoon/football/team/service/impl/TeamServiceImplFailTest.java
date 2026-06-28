package hoon.football.team.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.TeamCreateException;
import hoon.football.team.exception.exceptions.TeamNameDuplicateException;
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
class TeamServiceImplFailTest {

    @Autowired MemberService memberService;
    @Autowired TeamService teamService;

    @Test
    @DisplayName(value = "팀조회 실패 ( 팀장멤버 존재 X )")
    void createTeam_fail_failFindMember() throws Exception {
        // when && then
        assertThatThrownBy(() -> teamService.createTeam("teamA", 124124L))
                .isInstanceOf(TeamCreateException.class);
    }

    @Test
    @DisplayName(value = "팀조회 실패 ( 팀 이름 중복 )")
    void createTeam_fail_duplicateTeamName() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member);
        Team savedTeam = teamService.createTeam("teamA", savedMember.getId());

        // when && then
        assertThatThrownBy(() -> teamService.createTeam("teamA", savedMember.getId()))
                .isInstanceOf(TeamNameDuplicateException.class);
    }


}