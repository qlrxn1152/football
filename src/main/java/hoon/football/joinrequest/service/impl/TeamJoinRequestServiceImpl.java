package hoon.football.joinrequest.service.impl;

import hoon.football.joinrequest.domain.TeamJoinRequest;
import hoon.football.joinrequest.domain.TeamJoinRequestStatus;
import hoon.football.joinrequest.exception.exceptions.DuplicateTeamJoinRequestException;
import hoon.football.joinrequest.repository.TeamJoinRequestRepository;
import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.domain.Member;
import hoon.football.member.exception.exceptions.AlreadyJoinedTeamException;
import hoon.football.member.exception.exceptions.MemberNotFoundException;
import hoon.football.member.repository.MemberRepository;
import hoon.football.team.domain.Team;
import hoon.football.team.exception.exceptions.TeamNotFoundException;
import hoon.football.team.repository.TeamRepository;
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

    @Override
    public TeamJoinRequest createRequest(Long memberId, Long teamId) {
        Member member = findMember(memberId);
        Team team = findTeam(teamId);
        validateCreateRequest(teamId, member);

        // 멤버가 팀에, 가입신청을 넣음
        TeamJoinRequest request = new TeamJoinRequest(member, team); // -> 요청생성
        return joinRepository.save(request); // -> 요청저장
    }


    @Override
    @Transactional(readOnly = true)
    public List<TeamJoinRequest> findAllRequestsByTeamId(Long teamId) {
        return joinRepository.findAllByTeam_IdAndStatus(teamId, TeamJoinRequestStatus.PENDING);
    }



    // 비즈니스 로직

    private @NonNull Team findTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("ID로, 팀을 조회하지 못했습니다."));
    }

    private @NonNull Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[ID(pk)] 조회실패"));
    }

    private void validateCreateRequest(Long teamId, Member member) {
        // 다른팀에 이미 가입되어져있는지 상태인지? 이미 신청한팀이 자신의 팀인경우 ?
        if (member.getTeam() != null) { // 팀이 이미 있는경우
            if (member.getTeam().getId().equals(teamId)) { // 자신의 팀에 가입신청한 경우
                throw new AlreadyJoinedTeamException("자신의 팀에는 가입신청을 할 수 없습니다.");
            }
            throw new AlreadyJoinedTeamException("팀이 이미 존재합니다.");
        }

        // 이미 신청한 팀에 또 신청하는건 아닌지?
        if (joinRepository.existsByMember_IdAndTeam_IdAndStatus(member.getId(), teamId, TeamJoinRequestStatus.PENDING)) {
            throw new DuplicateTeamJoinRequestException("이미 신청한 팀입니다.");
        }

    }


}
