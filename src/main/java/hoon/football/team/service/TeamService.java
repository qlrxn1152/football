package hoon.football.team.service;

import hoon.football.member.domain.Member;
import hoon.football.team.domain.Team;

import java.util.List;

public interface TeamService {

    Team createTeam(String teamName, Long leaderMemberId);

    Team findById(Long id);

    Team findByTeamName(String teamName);

    Team findByLeaderMemberId(Long leaderMemberId);

    Team findDetailByTeamId(Long teamId);

    List<Team> findAll();

}
