package hoon.football.validator.teamjoinrequest;

import hoon.football.joinrequest.domain.TeamJoinRequestStatus;
import hoon.football.joinrequest.exception.exceptions.DuplicateTeamJoinRequestException;
import hoon.football.joinrequest.repository.TeamJoinRequestRepository;
import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.member.exception.exceptions.MemberNotFoundException;
import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.NotTeamLeaderException;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamJoinRequestValidator {

    private final TeamJoinRequestRepository teamJoinRequestRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    public void validateForCreateTeamJoinRequest(Long memberId, Long teamId) {
        // 이미 팀에 가입된 상태아닌가요?
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버를 조회하는데 실패했습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀을 조회하는데 실패했습니다."));

        // 팀에 속해있지 않는지 부터 검증 -> 팀이 있음 -> 자신의 팀에 가입신청했는지를 검증
        if (member.getTeam() != null) {
            if (team.equals(member.getTeam())) {
                throw new AlreadyJoinedTeamException("자신의 팀에는 가입신청을 할 수 없습니다.");
            }
            throw new AlreadyJoinedTeamException("팀이 이미 존재합니다.");
        }

        // 이미 가입신청을 넣은팀에 또 신청을 넣은건 아닌가요?
        if (teamJoinRequestRepository.existsByMember_IdAndTeam_IdAndStatus(memberId, teamId, TeamJoinRequestStatus.PENDING)) {
            throw new DuplicateTeamJoinRequestException("이미 신청한 팀입니다.");
        }
    }

    public void validateForCheckTeamLeader(Team team, Long loginMemberId) {
        // 로그인한 사람이 팀 리더가 맞나요?

        if (!loginMemberId.equals(team.getLeaderMember().getId())) {
            throw new NotTeamLeaderException("해당 팀 팀장이 아닙니다.");
        }





    }
}
