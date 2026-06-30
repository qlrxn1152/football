package hoon.football.teammatch.repository;

import hoon.football.teammatch.domain.TeamMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMatchRepository extends JpaRepository<TeamMatch, Long> {
}
