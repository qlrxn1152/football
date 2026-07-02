package hoon.football.teammatch.repository;

import hoon.football.teammatch.domain.TeamMatchRequest;
import hoon.football.teammatch.domain.TeamMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamMatchRequestRepository extends JpaRepository<TeamMatchRequest, Long> {

    List<TeamMatchRequest> findByHomeTeamId(Long homeTeamId);

}
