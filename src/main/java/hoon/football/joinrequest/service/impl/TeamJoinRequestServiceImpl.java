package hoon.football.joinrequest.service.impl;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.joinrequest.domain.TeamJoinRequestStatus;
import hoon.football.joinrequest.exception.exceptions.NotFoundTeamJoinRequestException;
import hoon.football.joinrequest.exception.exceptions.DuplicateTeamJoinRequestException;
import hoon.football.joinrequest.repository.TeamJoinRequestRepository;
import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.member.exception.exceptions.MemberNotFoundException;
import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.NotTeamLeaderException;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.repository.TeamRepository;
import hoon.football.validator.teamjoinrequest.TeamJoinRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TeamJoinRequestServiceImpl implements TeamJoinRequestService {

    private final TeamJoinRequestRepository joinRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamJoinRequestValidator teamJoinRequestValidator;

    @Override
    public TeamJoinRequest createRequest(Long memberId, Long teamId) {
        Member member = findMember(memberId);
        Team team = findTeam(teamId);
        teamJoinRequestValidator.validateForCreateTeamJoinRequest(memberId, teamId);

        // 멤버가 팀에, 가입신청을 넣음
        TeamJoinRequest request = new TeamJoinRequest(member, team); // -> 요청생성
        return joinRepository.save(request); // -> 요청저장
    }

    @Override
    public TeamJoinRequest findPendingrequestByTeam_IdAndMember_idAndStatus(Long teamId, Long memberId) {
        return joinRepository.findByTeam_IdAndMember_idAndStatus(teamId, memberId, TeamJoinRequestStatus.PENDING)
                .orElseThrow(() -> new NotFoundTeamJoinRequestException("요청을 조회하지 못했습니다."));
    }


    @Override
    @Transactional(readOnly = true)
    public List<TeamJoinRequest> findAllRequestsByTeamId(Long teamId) {
        return joinRepository.findAllByTeam_IdAndStatus(teamId, TeamJoinRequestStatus.PENDING);
    }

    @Override
    public void acceptRequest(Long memberId, Long teamId, Long loginMemberId) {
        Member member = findMember(memberId);
        Team team = findTeam(teamId);

        teamJoinRequestValidator.validateForCheckTeamLeader(team, loginMemberId);
        findRequestAndAcceptRequest(memberId, teamId, member, team);
    }

    @Override
    public void rejectRequest(Long memberId, Long teamId, Long loginMemberId) {
        Team team = findTeam(teamId);

        teamJoinRequestValidator.validateForCheckTeamLeader(team, loginMemberId);
        findRequestAndRejectRequest(memberId, teamId);
    }

    // 비즈니스 로직
    private @NonNull Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[ID(pk)] 조회실패"));
    }

    private @NonNull Team findTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("ID로, 팀을 조회하지 못했습니다."));
    }

    private void findRequestAndAcceptRequest(Long memberId, Long teamId, Member member, Team team) {
        findPendingrequestByTeam_IdAndMember_idAndStatus(teamId, memberId)
                .accept(member, team); // status = ACCEPTED -> Team , Member 서로 양방향 매핑 ..
        // accept -> member.joinTeam(team) 실행 -> Team , Member 서로 양방향 매핑 ..
    }

    private void findRequestAndRejectRequest(Long memberId, Long teamId) {
        findPendingrequestByTeam_IdAndMember_idAndStatus(teamId, memberId)
                .reject();
    }


}
