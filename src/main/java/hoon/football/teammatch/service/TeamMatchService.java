package hoon.football.teammatch.service;

import hoon.football.team.domain.Team;
import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.domain.TeamMatchRequest;
import hoon.football.teammatch.domain.TeamMatchStatus;

import java.util.List;

public interface TeamMatchService {

    TeamMatch createTeamMatch(Long homeTeamId, Long loginMemberId);

    TeamMatchRequest acceptTeamMatchRequest(Long matchId, Long awayTeamId, Long loginMemberId);

    TeamMatch acceptTeamMatch(Long matchId, Long awayTeamId, Long loginMemberId);

    void rejectTeamMatchRequest(Long matchId, Long awayTeamId);

    TeamMatch expireTeamMatch(Long matchId);

    TeamMatch resultTeamMatch(Long matchId, Integer homeScore, Integer awayScore);

    List<TeamMatch> findPendingMatch();

    TeamMatch findMatchById(Long id);

    List<TeamMatch> findByTeamIdAndStatus(Long teamId, TeamMatchStatus status);

}
