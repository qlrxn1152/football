package hoon.football.joinrequest.repository;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.joinrequest.domain.TeamJoinRequestStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamJoinRequestRepository extends JpaRepository<TeamJoinRequest, Long> {

    List<TeamJoinRequest> findAllByTeam_IdAndStatus(Long teamId, TeamJoinRequestStats stats);

}
