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
import hoon.football.teammatch.domain.TeamMatchRequest;
import hoon.football.teammatch.domain.TeamMatchStatus;
import hoon.football.teammatch.exception.exceptions.NotFoundTeamMatchException;
import hoon.football.teammatch.repository.TeamMatchRepository;
import hoon.football.teammatch.repository.TeamMatchRequestRepository;
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
    private final TeamMatchRequestRepository teamMatchRequestRepository;


    @Override
    public TeamMatch createTeamMatch(Long homeTeamId, Long loginMemberId) {
        return null;
    }

    @Override
    public TeamMatch acceptTeamMatch(Long matchId, Long awayTeamId, Long loginMemberId) {
        return null;
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

    @Override
    public TeamMatch findMatchById(Long id) {
        return teamMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundTeamMatchException("매치 조회에 실패했습니다."));
    }


}
