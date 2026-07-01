package hoon.football.teammatch.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.domain.TeamRole;
import hoon.football.member.exception.exceptions.MemberLoginException;
import hoon.football.member.exception.exceptions.MemberNotFoundException;
import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.NotTeamLeaderException;
import hoon.football.team.exception.exceptions.NotTeamMemberException;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.repository.TeamRepository;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.domain.TeamMatchStatus;
import hoon.football.teammatch.exception.exceptions.NotFoundTeamMatchException;
import hoon.football.teammatch.repository.TeamMatchRepository;
import hoon.football.teammatch.service.TeamMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TeamMatchServiceImpl implements TeamMatchService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMatchRepository teamMatchRepository;

    @Override
    public TeamMatch createTeamMatch(Long homeTeamId, Long loginMemberId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버 조회에 실패했습니다.")); // 멤버조회

        Team homeTeam = teamRepository.findById(homeTeamId)
                .orElseThrow(() -> new TeamNotFoundException("팀 조회에 실패했습니다.")); // 팀 조회 => 문제생길 가능성이 굉장히 적음.

        if (loginMember.getTeam() == null || !loginMember.getTeam().getId().equals(homeTeamId)){
            throw new NotTeamMemberException("해당 팀 멤버가 아닙니다.");
        } // 팀 멤버가 맞는지

        if (loginMember.getTeamRole() != TeamRole.LEADER) {
            throw new NotTeamLeaderException("팀 리더가 아닙니다.");
        } // 팀장이 맞는지 ==> 팀원이지만, 팀장이 아닌경우


        // 검증들 다 통과하면 매칭등록
        return teamMatchRepository.save(new TeamMatch(homeTeam));
    }

    // homeTeam 팀장 -> 매칭 등록 -> awayTeam 팀장 -> 매칭 수락 요청 -> homeTeam 팀장 -> 수락 (acceptTeamMatch ) / 거절
    @Override
    public TeamMatch acceptTeamMatch(Long matchId, Long awayTeamId, Long loginMemberId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버 조회에 실패했습니다.")); // => homeTeam 팀장이여야함.

        TeamMatch teamMatch = teamMatchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundTeamMatchException("매치 조회에 실패했습니다.")); // 등록되어져있는 매치조회

        Team homeTeam = teamMatch.getHomeTeam();

        if ( loginMember.getTeam() == null || !homeTeam.getLeaderMember().getId().equals(loginMember.getId()) ){
            throw new NotTeamLeaderException("팀장이 아닙니다.");
        }

        Team awayTeam = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new TeamNotFoundException("원정팀 조회에 실패했습니다.")); // 원정팀 조회

        // 검증들 다 통과하면 매칭수락 => 매치됨
        teamMatch.acceptMatch(awayTeam);

        return teamMatch;
    }


    @Override
    public TeamMatch expireTeamMatch(Long matchId) {
        TeamMatch teamMatch = teamMatchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundTeamMatchException("매치 조회에 실패했습니다."));

        LocalDateTime requestAt = teamMatch.getRequestAt();

        // 그럼 이거를 계속해서 체크?
        if (requestAt.isBefore(LocalDateTime.now().minusWeeks(1))) {
            teamMatch.expireMatch();
        }

        return teamMatch;
    }

    @Override
    public List<TeamMatch> findPendingMatch() {
        return teamMatchRepository.findMatchesByStatus(TeamMatchStatus.PENDING);
    }


}
