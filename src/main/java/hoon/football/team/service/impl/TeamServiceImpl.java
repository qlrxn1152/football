package hoon.football.team.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.TeamCreateException;
import hoon.football.team.exception.exceptions.TeamNameDuplicateException;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.repository.TeamRepository;
import hoon.football.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Override
    public Team createTeam(String teamName, Long leaderMemberId) {
        Member leaderMember = findMemberByTeamLeaderMember(leaderMemberId); // 멤버조회
        validateAlreadyJoinedTeam(leaderMember); // 멤버가 가입한 팀이 이미 존재하는지 확인
        validateDuplicateTeamName(teamName); // 팀이름 중복확인

        // 문제 없이 통과한 경우 실행
        Team savedTeam = teamRepository.save(new Team(teamName, leaderMember));
        leaderMember.createTeamAsLeaderMember(savedTeam);

        return savedTeam;
    }

    @Override
    @Transactional(readOnly = true)
    public Team findById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("ID로, 팀을 조회하지 못했습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Team findByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName)
                .orElseThrow(() -> new TeamNotFoundException("TeamName으로, 팀을 조회하지 못했습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Team findByLeaderMemberId(Long leaderMemberId) {
        return teamRepository.findByLeaderMemberId(leaderMemberId)
                .orElseThrow(() -> new TeamNotFoundException("leaderMemberId로, 팀을 조회하지 못했습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Team findDetailByTeamId(Long teamId) {
        return teamRepository.findByIdWithLeaderMember(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀을 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findAll() {
        return teamRepository.findAll();
    }



    // 비즈니스 로직 //

    /**
     * 파라미터로, teamName 을 받고 이를 repository 에서 같은 teamName 을 가진 팀이 존재하는지 검증하는 로직.
     * @return teamName 이 이미 존재하면 -> true / 존재하지않는다면 -> false
     * @param teamName
     */
    private void validateDuplicateTeamName(String teamName) {
        if (teamRepository.existsByTeamName(teamName)) {
            throw new TeamNameDuplicateException("팀 이름이 이미 존재합니다.");
        }
    }


    /**
     * 파라미터로, leaderMemberId 를 받고, 이 값을 memberRepository 에서 해당 member 를 조회하는 로직. ( 팀장이 누구인지 확인가능 )
     * @param leaderMemberId
     * @return 해당 PK 값을 가진 Member 가 존재하면, Member 리턴 / 존재하지않는다면 -> 예외발생
     */
    private @NonNull Member findMemberByTeamLeaderMember(Long leaderMemberId) {
        return memberRepository.findById(leaderMemberId)
                .orElseThrow(() -> new TeamCreateException("팀장을 찾을 수 없습니다."));
    }

    /**
     * 파라미터로, leaderMemberId 를 받고, 이 값을 memberrepository 에서 해당 member 를 조회 -> 해당 member 가 가입한 팀이 존재하는지 확인하는 로직
     * @param leaderMember
     */
    private static void validateAlreadyJoinedTeam(Member leaderMember) {
        Team memberTeam = leaderMember.getTeam();
        if (memberTeam != null) {
            throw new AlreadyJoinedTeamException("팀이 이미 존재합니다.");
        }
    }



}
