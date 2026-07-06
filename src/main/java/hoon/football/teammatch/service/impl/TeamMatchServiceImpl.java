package hoon.football.teammatch.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.domain.TeamRole;
import hoon.football.member.exception.exceptions.MemberNotFoundException;
import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.NotTeamLeaderException;
import hoon.football.team.exception.exceptions.NotTeamMemberException;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.repository.TeamRepository;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.domain.TeamMatchRequest;
import hoon.football.teammatch.domain.TeamMatchResult;
import hoon.football.teammatch.domain.TeamMatchStatus;
import hoon.football.teammatch.exception.exceptions.NotFoundTeamMatchRequestException;
import hoon.football.teammatch.exception.exceptions.TeamMatchAcceptToSelfTeamException;
import hoon.football.teammatch.exception.exceptions.NotFoundTeamMatchException;
import hoon.football.teammatch.repository.TeamMatchRepository;
import hoon.football.teammatch.repository.TeamMatchRequestRepository;
import hoon.football.teammatch.service.TeamMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TeamMatchServiceImpl implements TeamMatchService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMatchRepository teamMatchRepository;
    private final TeamMatchRequestRepository teamMatchRequestRepository;

    // 매칭 등록
    @Override
    public TeamMatch createTeamMatch(Long homeTeamId, Long loginMemberId) {
        Team homeTeam = teamRepository.findById(homeTeamId)
                .orElseThrow(() -> new TeamNotFoundException("팀을 조회하는데 실패했습니다."));
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버 조회하는데 실패했습니다."));
        validateMemberToTeam(loginMember, homeTeam);

        // 검증들 통과 -> 매칭 생성
        return createTeamMatch(homeTeam);
    }

    // 매칭 수락 요청 --> awayTeam 팀장이 신청 ( loginMember )
    @Override
    public TeamMatchRequest acceptTeamMatchRequest(Long matchId, Long awayTeamId, Long loginMemberId) {
        TeamMatch teamMatch = teamMatchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundTeamMatchException("팀매치 조회하는데 실패했습니다."));

        Team homeTeam = teamMatch.getHomeTeam();

        Team awayTeam = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new TeamNotFoundException("팀을 조회하는데 실패했습니다."));

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버 조회하는데 실패했습니다."));

        // 로그인한 사람이 팀에 속해있는지
        validateMemberToTeam(loginMember, awayTeam);
        validateAcceptTeamMatchRequest(matchId, awayTeamId, homeTeam, awayTeam);

        // 검증들 통괴 -> 매칭 요청 생성
        return createMatchAcceptRequest(teamMatch, homeTeam, awayTeam);
    }

    // 매칭 수락
    @Override
    public TeamMatch acceptTeamMatch(Long matchId, Long awayTeamId, Long loginMemberId) {
        // homeTeam 팀장이 수락 ... ( 요청 보낸사람 -> awayTeam 팀장 )
        TeamMatch teamMatch = teamMatchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundTeamMatchException("팀매치 조회하는데 실패했습니다."));
        Team homeTeam = teamMatch.getHomeTeam();
        Team awayTeam = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new TeamNotFoundException("팀을 조회하는데 실패했습니다."));
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버 조회하는데 실패했습니다."));

        TeamMatchRequest request = teamMatchRequestRepository.findByTeamMatchIdAndAwayTeamId(matchId, awayTeamId)
                .orElseThrow(() -> new NotFoundTeamMatchRequestException("팀 매칭 요청을 조회하지못했습니다."));

        request.matched(); // request -> status : MATCHED
        teamMatch.acceptMatch(awayTeam); // awayTeam 할당 , status = MATCHED 변경
        return teamMatch;
    }

    @Override
    public void rejectTeamMatchRequest(Long matchId, Long awayTeamId) {
        // TeamMatchREquest_id -> 매치요청 조회
        TeamMatchRequest request = teamMatchRequestRepository.findByTeamMatchIdAndAwayTeamId(matchId, awayTeamId)
                .orElseThrow(() -> new NotFoundTeamMatchRequestException("팀 매칭 요청을 조회하지못했습니다."));

        // TeamMatchRequest_id 를 통해서 TeamMatchRequest 조회 -> status = REJECTED 로 변경.
        request.rejected();
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
    public TeamMatch resultTeamMatch(Long matchId, Integer homeScore, Integer awayScore) {
        // 검증도 필요 ...
        // 팀장 입력맞음 ? 점수에 이상한값이 들어온건 아닌지 ?

        // 팀장이 점수를 입력하고, 결과입력 버튼을 누름 ->
        TeamMatch match = teamMatchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundTeamMatchException("매치 조회 실패했습니다."));

        new TeamMatchResult(match, homeScore, awayScore); // 생성자 호출 -> 점수 계산해서 반영.

        return match;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamMatch> findPendingMatch() {
        return teamMatchRepository.findMatchesByStatus(TeamMatchStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMatch findMatchById(Long id) {
        return teamMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundTeamMatchException("매치 조회에 실패했습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamMatch> findByTeamIdAndStatus(Long teamId, TeamMatchStatus status) {
        return teamMatchRepository.findByHomeTeamIdAndStatus(teamId, status);
    }


    // 비즈니스 로직
    private static void validateMemberToTeam(Member loginMember, Team team) {
        // 로그인한 사람이 팀에 속해있는지
        if (loginMember.getTeam() == null) {
            throw new NotTeamMemberException("팀에 안속해있는데요");
        }

        // team 에 속해있지 않는사람인지
        if (!loginMember.getTeam().equals(team)) {
            throw new NotTeamMemberException("다른팀 소속이잖아");
        }

        // 해당 팀 인원은 맞지만, 팀장이 아님
        if (loginMember.getTeam().equals(team) && loginMember.getTeamRole() != TeamRole.LEADER) {
            throw new NotTeamLeaderException("팀장아닌데요.");
        }


    }

    private @NonNull TeamMatchRequest createMatchAcceptRequest(TeamMatch teamMatch, Team homeTeam, Team awayTeam) {
        TeamMatchRequest matchRequest = new TeamMatchRequest(teamMatch, homeTeam, awayTeam);
        homeTeam.getTeamMatchRequests().add(matchRequest);
        teamMatchRequestRepository.save(matchRequest);
        return matchRequest;
    }

    private @NonNull TeamMatch createTeamMatch(Team homeTeam) {
        TeamMatch teamMatch = new TeamMatch(homeTeam);
        teamMatchRepository.save(teamMatch);
        return teamMatch;
    }

    private void validateAcceptTeamMatchRequest(Long matchId, Long awayTeamId, Team homeTeam, Team awayTeam) {
        // 검증에서, 이미 요청한 팀은 다시요청할수없게 해줘야함 즉, 2번이상 같은매치에 요청 ㄴㄴ !
        Optional<TeamMatchRequest> optTeamMatchRequest = teamMatchRequestRepository.findByTeamMatchIdAndAwayTeamId(matchId, awayTeamId);

        if (optTeamMatchRequest.isPresent()) {
            throw new NotFoundTeamMatchException("같은 매치에 요청을 보낼 수 없습니다.");
        }

        // 자기팀에는 요청불가
        if (homeTeam.getId().equals(awayTeam.getId())) {
            throw new TeamMatchAcceptToSelfTeamException("자신의 팀에는 매치요청을 보낼 수 없습니다.");
        }
    }



}
