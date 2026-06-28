package hoon.football.joinrequest.service.impl;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.joinrequest.domain.TeamJoinRequestStats;
import hoon.football.joinrequest.repository.TeamJoinRequestRepository;
import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.domain.Member;
import hoon.football.team.domain.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TeamJoinRequestServiceImpl implements TeamJoinRequestService {

    private final TeamJoinRequestRepository joinRepository;

    @Override
    public TeamJoinRequest createRequest(Member member, Team team) {
        // 멤버가 팀에, 가입신청을 넣음
        TeamJoinRequest request = new TeamJoinRequest(member, team); // -> 요청생성
        return joinRepository.save(request); // -> 요청저장
    }

    @Override
    public List<TeamJoinRequest> findAllRequestsByTeamId(Long teamId) {
        return joinRepository.findAllByTeam_IdAndStatus(teamId, TeamJoinRequestStats.PENDING);
    }


}
