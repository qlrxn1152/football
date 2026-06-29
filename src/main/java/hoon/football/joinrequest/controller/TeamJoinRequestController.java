package hoon.football.joinrequest.controller;

import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.domain.Member;
import hoon.football.member.dto.MemberSessionDto;
import hoon.football.member.service.MemberService;
import hoon.football.team.domain.Team;
import hoon.football.team.service.TeamService;
import hoon.football.web.SessionConst;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TeamJoinRequestController {

    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamJoinRequestService teamJoinRequestService;

    private final EntityManager em;

    @PostMapping("/teams/{teamId}/join-requests")
    public String joinRequest(@PathVariable Long teamId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto loginMember, RedirectAttributes redirectAttributes) {
        teamJoinRequestService.createRequest(loginMember.getId(), teamId);
        redirectAttributes.addFlashAttribute("successMessage", "가입신청 성공.");
        return "redirect:/teams/" + teamId;
    }

    @PostMapping("/teams/{teamId}/join-requests/{memberId}/accept")
    public String acceptRequest(@PathVariable Long teamId, @PathVariable Long memberId, RedirectAttributes redirectAttributes) {
        // 해당 팀 팀장이 수락버튼 -> 요청 status = ACCEPTED -> 팀에 가입
        Team team = teamService.findById(teamId);
        Member member = memberService.findById(memberId);

        // TEAMJOINREQUEST 객체를 요청을 받아서 처리 ? 아니면, 직접 찾아서 처리? ==> status = ACCEPTED 로 바꿔줘야하니까 .. teamId, memberId 로 조회는 가능.

        // 팀에 가입 ?
        member.joinTeam(team);
        redirectAttributes.addFlashAttribute("successMessage", "승인완료");
        return "redirect:/teams/" + teamId;
    }

    @PostMapping("/teams/{teamId}/join-requests/{memberId}/reject")
    public String rejectRequest(@PathVariable Long teamId, @PathVariable Long memberId, RedirectAttributes redirectAttributes) {
        // 해당 팀 팀장이 거절버튼 -> 요청 status = REJECTED -> 가입신청 거절
        Team team = teamService.findById(teamId);
        Member member = memberService.findById(memberId);

        // TEAMJOINREQUEST 객체를 요청을 받아서 처리 ? 아니면, 직접 찾아서 처리? ==> status = REJECTED 로 바꿔줘야하니까 .. teamId, memberId 로 조회는 가능.





    }


}
