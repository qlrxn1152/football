package hoon.football.team.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.service.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TeamServiceImplSuccessTest {

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

        assertThat(savedMember.getTeam()).isEqualTo(savedTeam);
    }

    @Test
    @DisplayName(value = "팀조회 성공 ( PK 인, ID 값으로 조회 )")
    void findById_success() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member);
        Team savedTeam = teamService.createTeam("teamA", savedMember.getId());

        // when
        Team findTeam = teamService.findById(savedTeam.getId());

        // then
        assertThat(findTeam).isSameAs(savedTeam);
        assertThat(findTeam.getLeaderMember()).isEqualTo(savedMember);
        assertThat(findTeam.getTeamName()).isEqualTo("teamA");
        assertThat(findTeam.getRating()).isEqualTo(1000);
    }

    @Test
    @DisplayName(value = "팀조회 성공 ( TeamName 으로 조회 )")
    void findByTeamName_success() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member);
        Team savedTeam = teamService.createTeam("teamA", savedMember.getId());

        // when
        Team findTeam = teamService.findByTeamName("teamA");

        // then
        assertThat(findTeam).isSameAs(savedTeam);
        assertThat(findTeam.getLeaderMember()).isEqualTo(savedMember);
        assertThat(findTeam.getTeamName()).isEqualTo("teamA");
        assertThat(findTeam.getRating()).isEqualTo(1000);
    }

    @Test
    @DisplayName(value = "팀조회 성공 ( LeaderMemberId 로 조회 )")
    void findByLeaderMemberId_success() throws Exception {
        // given
        Member member = new Member("userA", "1234");
        Member savedMember = memberService.save(member); // 해쉬화된 Member 객체로 저장.
        Team savedTeam = teamService.createTeam("teamA", savedMember.getId());

        // when
        Team findTeam = teamService.findByLeaderMemberId(savedTeam.getId());

        // then
        assertThat(findTeam).isSameAs(savedTeam);
        assertThat(findTeam.getLeaderMember()).isEqualTo(savedMember);
        assertThat(findTeam.getTeamName()).isEqualTo("teamA");
        assertThat(findTeam.getRating()).isEqualTo(1000);

    }



}