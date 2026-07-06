package hoon.football.validator.team;

import hoon.football.member.domain.Member;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.NotTeamLeaderException;
import hoon.football.team.service.TeamService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamValidator {

    private final MemberService memberService;
    private final TeamService teamService;

    public void checkTeamLeader(Long teamId, Long memberId) {
        Member member = memberService.findById(memberId);
        Team team = teamService.findById(teamId);

        if ( !team.getLeaderMember().getId().equals(member.getId()) ) {
            throw new NotTeamLeaderException("팀 리더가 아닙니다.");
        }

    }


}
