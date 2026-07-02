package hoon.football.teammatch.repository;

import hoon.football.teammatch.domain.TeamMatchRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMatchRequestRepository extends JpaRepository<TeamMatchRequest, Long> {
}
