package hoon.football.team.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.member.exception.exceptions.MemberNotFoundException;
import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.*;
import hoon.football.team.repository.TeamRepository;
import hoon.football.team.service.TeamService;
import hoon.football.validator.member.MemberValidator;
import hoon.football.validator.team.TeamValidator;
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
    private final TeamValidator teamValidator;

    @Override
    public Team createTeam(String teamName, Long memberId) {
        Member member = teamValidator.validateForCreateTeam(memberId, teamName);

        // 문제 없이 통과한 경우 실행 -> 팀 생성.
        Team savedTeam = teamRepository.save(new Team(teamName, member));
        member.createTeamAsLeaderMember(savedTeam);

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
    public Team findDetailTeamByTeamId(Long teamId) {
        return teamRepository.findByIdWithLeaderMember(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀을 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findAll() {
        return teamRepository.findAll();
    }


    @Override
    public void updateTeamName(Long teamId, String newTeamName, Long leaderMemberId) {
        Team team = teamValidator.validateForUpdateTeamName(teamId, newTeamName, leaderMemberId);
        team.changeTeamName(newTeamName); // 변경
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTeamName(String teamName) {
        return teamRepository.existsByTeamName(teamName);
    }





}
