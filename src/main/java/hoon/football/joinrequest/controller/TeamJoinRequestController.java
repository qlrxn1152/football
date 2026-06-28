package hoon.football.joinrequest.controller;

import hoon.football.joinrequest.service.TeamJoinRequestService;
import hoon.football.member.dto.MemberSessionDto;
import hoon.football.member.service.MemberService;
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


}
