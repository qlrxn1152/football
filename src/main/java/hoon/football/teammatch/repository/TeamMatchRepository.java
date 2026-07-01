package hoon.football.teammatch.repository;

import hoon.football.teammatch.domain.TeamMatch;
import hoon.football.teammatch.domain.TeamMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TeamMatchRepository extends JpaRepository<TeamMatch, Long> {

    List<TeamMatch> findMatchesByStatus(TeamMatchStatus status);

}
