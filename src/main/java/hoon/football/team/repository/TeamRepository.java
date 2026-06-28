package hoon.football.team.repository;

import hoon.football.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByTeamName(String teamName);

    Optional<Team> findByLeaderMemberId(Long leaderMemberId);

    @Query("select t from Team t join fetch t.leaderMember where t.id = :teamId")
    Optional<Team> findByIdWithLeaderMember(Long teamId);

    boolean existsByTeamName(String teamName);
}
