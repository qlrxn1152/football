package hoon.football.teammatch.service;

import hoon.football.team.domain.Team;
import hoon.football.teammatch.domain.TeamMatch;

public interface TeamMatchService {

    TeamMatch createTeamMatch(Long homeTeamId);

    TeamMatch acceptTeamMatch(Long matchId, Long awayTeamId);

    TeamMatch expireTeamMatch(Long matchId);

}
