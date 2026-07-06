package hoon.football.validator.team;

import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.member.exception.exceptions.MemberNotFoundException;
import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.*;
import hoon.football.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamValidator {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    /**
     * 팀 생성을 위한 검증로직들입니다.
     * @return 모든 검증들을 통과하면, 팀을 생성하고, 팀생성을 요청한 멤버 객체를 반환합니다.
     */
    public Member validateForCreateTeam(Long memberId, String teamName) {
        Member member = validateAlreadyJoinTeamAndReturnMember(memberId);
        validateDuplicateTeamName(teamName);

        return member;
    }

    /**
     * 팀 이름 변경을 위한 검증로직들입니다.
     * @return 모든 검증들을 통과하면, 팀 이름을 변경하고, 팀 객체를 반환합니다.
     */
    public Team validateForUpdateTeamName(Long teamId, String newTeamName, Long leaderMemberId) {
        Team team = findTeam(teamId);
        checkTeamLeader(leaderMemberId, team);
        validateDuplicateTeamName(newTeamName);

        return team;
    }




    // 팀 업데이트 검증
    private @NonNull Team findTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀 조회 실패")); // 팀 조회
        return team;
    }

    private static void checkTeamLeader(Long leaderMemberId, Team team) {
        if (!leaderMemberId.equals(team.getLeaderMember().getId())) {
            throw new NotTeamLeaderException("해당하는 팀 팀장이 아닙니다."); // 조회한 팀 팀장이 맞는지 확인.
        }
    }

    private void validateDuplicateTeamName(String teamName) {
        if (teamRepository.existsByTeamName(teamName)) {
            throw new TeamNameDuplicateException("팀 이름이 이미 존재합니다."); // 팀 이름이 이미 존재하는지 확인.
        }
    }



    // 팀 생성 검증

    /**
     * 파라미터로, memberId 를 받고, 이 값을 memberrepository 에서 해당 member 를 조회 -> 해당 member 가 가입한 팀이 존재하는지 확인하는 로직
     * @param memberId
     */
    private Member validateAlreadyJoinTeamAndReturnMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않습니다."));

        if (member.getTeam() != null) {
            throw new AlreadyJoinedTeamException("팀이 이미 존재합니다.");
        }
        return member;
    }




}
