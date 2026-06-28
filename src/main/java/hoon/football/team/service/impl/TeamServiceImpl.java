package hoon.football.team.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.TeamCreateException;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.repository.TeamRepository;
import hoon.football.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Override
    public Team createTeam(String teamName, Long leaderMemberId) {
        Member leaderMember = memberRepository.findById(leaderMemberId)
                .orElseThrow(() -> new TeamCreateException("팀장을 찾을 수 없습니다."));

        Team team = new Team(teamName, leaderMember);
        return teamRepository.save(team);
    }

    @Override
    public Team findById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("ID로, 팀을 조회하지 못했습니다."));
    }

    @Override
    public Team findByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName)
                .orElseThrow(() -> new TeamNotFoundException("TeamName으로, 팀을 조회하지 못했습니다."));
    }

    @Override
    public Team findByLeaderMemberId(Long leaderMemberId) {
        return teamRepository.findByLeaderMemberId(leaderMemberId)
                .orElseThrow(() -> new TeamCreateException("leaderMemberId로, 팀을 조회하지 못했습니다."));
    }

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }


}
