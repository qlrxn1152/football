package hoon.football.validator.teammatch;

import hoon.football.member.domain.Member;
import hoon.football.member.domain.TeamRole;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.NotTeamLeaderException;
import hoon.football.team.exception.exceptions.NotTeamMemberException;
import hoon.football.teammatch.exception.exceptions.DuplicateTeamMatchRequestException;
import hoon.football.teammatch.exception.exceptions.TeamMatchAcceptToSelfTeamException;
import hoon.football.teammatch.repository.TeamMatchRepository;
import hoon.football.teammatch.repository.TeamMatchRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMatchValidator {

    private final TeamMatchRepository teamMatchRepository;
    private final TeamMatchRequestRepository teamMatchRequestRepository;

    public void validateForTeamMatchCreate(Member member, Team team) {
        // 팀 매치를 생성하는데, 필요한 검증들 --> 팀에 속해있나? 들어온 팀에 속해있는 멤버가맞나 ?  팀장이맞나?
        if ( member.getTeam() == null) {
            throw new NotTeamMemberException("팀에 안속해있는데요");
        }

        if (!member.getTeam().equals(team)) {
            throw new NotTeamMemberException("다른팀 소속이잖아");
        }

        if (member.getTeam().equals(team) && member.getTeamRole() != TeamRole.LEADER) {
            throw new NotTeamLeaderException("팀장아닌데요.");
        }
    }

    public void validateForAcceptTeamMatchRequest(Long matchId, Long awayTeamId, Long homeTeamId) {
        // 같은 매치에 중복요청
        if (teamMatchRequestRepository.existsByTeamMatchIdAndAwayTeamId(matchId, awayTeamId)) {
            throw new DuplicateTeamMatchRequestException("같은 매치에 중복요청을 보낼 수 없습니다.");
        }

        // 자신의 팀에 매칭요청
        if (homeTeamId.equals(awayTeamId)) {
            throw new TeamMatchAcceptToSelfTeamException("자신의 팀에는 매치요청을 보낼 수 없습니다.");
        }
    }


}
