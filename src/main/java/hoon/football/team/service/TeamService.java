package hoon.football.team.service;

import hoon.football.member.domain.Member;
import hoon.football.team.domain.Team;

public interface TeamService {

    Team createTeam(String teamName, Long leaderMemberId);
}
