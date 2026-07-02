package hoon.football.teammatch.repository;

import hoon.football.teammatch.domain.TeamMatchRequest;
import hoon.football.teammatch.domain.TeamMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamMatchRequestRepository extends JpaRepository<TeamMatchRequest, Long> {

    List<TeamMatchRequest> findByHomeTeamId(Long homeTeamId);

    @Query("select tmr from TeamMatchRequest tmr join TeamMatch tm on tmr.teamMatch.id = tm.id where tm.homeTeam.id = :homeTeamId and tm.status = :status")
    List<TeamMatchRequest> findPendingMatchRequest(Long homeTeamId, TeamMatchStatus status);

    Optional<TeamMatchRequest> findByTeamMatchIdAndAwayTeamId(Long teamMatchId, Long awayTeamId);

}
