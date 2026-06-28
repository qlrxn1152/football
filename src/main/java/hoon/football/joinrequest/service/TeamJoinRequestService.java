package hoon.football.joinrequest.service;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.member.domain.Member;
import hoon.football.team.domain.Team;

import java.util.List;

public interface TeamJoinRequestService {

    TeamJoinRequest createRequest(Long memberId, Long teamId);

    List<TeamJoinRequest> findAllRequestsByTeamId (Long teamId);
}
