package hoon.football.teammatch.service.impl;

import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
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

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TeamMatchServiceImpl implements TeamMatchService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMatchRepository teamMatchRepository;

    @Override
    public TeamMatch createTeamMatch(Long homeTeamId) {
        // homeTeam 으로 들어온 team 객체랑, 로그인한 멤버의 team 이랑 같은지 확인도 해줘야하고, 팀장인지도 검증해줘야함.
        Team homeTeam = teamRepository.findById(homeTeamId)
                .orElseThrow(() -> new TeamNotFoundException("팀 조회에 실패했습니다."));

        return teamMatchRepository.save(new TeamMatch(homeTeam));
    }

    @Override
    public TeamMatch acceptTeamMatch(Long matchId, Long awayTeamId) {
        TeamMatch teamMatch = teamMatchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundTeamMatchException("매치 조회에 실패했습니다."));

        Team awayTeam = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new TeamNotFoundException("팀 조회에 실패했습니다."));

        teamMatch.acceptMatch(awayTeam);

        return teamMatch;
    }


    @Override
    public TeamMatch expireTeamMatch(Long matchId) {
        LocalDateTime now = LocalDateTime.now();
        TeamMatch teamMatch = teamMatchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundTeamMatchException("매치 조회에 실패했습니다."));

        LocalDateTime requestAt = teamMatch.getRequestAt();

        // 그럼 이거를 계속해서 체크?
        if (requestAt.isBefore(LocalDateTime.now().minusWeeks(1))) {
            teamMatch.expireMatch();
        }

        return teamMatch;
    }


}
