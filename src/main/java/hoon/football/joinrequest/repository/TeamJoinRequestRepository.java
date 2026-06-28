package hoon.football.joinrequest.repository;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.joinrequest.domain.TeamJoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamJoinRequestRepository extends JpaRepository<TeamJoinRequest, Long> {

    List<TeamJoinRequest> findAllByTeam_IdAndStatus(Long teamId, TeamJoinRequestStatus stats);

    boolean existsByMember_IdAndTeam_IdAndStatus(Long memberId, Long teamId, TeamJoinRequestStatus stats);
}
