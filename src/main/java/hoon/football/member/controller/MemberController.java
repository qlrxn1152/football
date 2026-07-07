package hoon.football.member.controller;

import hoon.football.member.domain.Member;
import hoon.football.member.dto.*;
import hoon.football.member.exception.*;
import hoon.football.member.service.MemberService;
import hoon.football.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String saveMemberForm(@ModelAttribute MemberSaveDto memberSaveDto) {
        return "members/saveMember";
    }

    @PostMapping("/members/new")
    public String saveMember(@ModelAttribute MemberSaveDto memberSaveDto) {
        Member member = new Member(memberSaveDto.getUsername(), memberSaveDto.getPassword());
        memberService.save(member);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(name = "redirectURI", defaultValue = "/") String redirectURI, @ModelAttribute MemberLoginDto memberLoginDto, Model model) {
        model.addAttribute("redirectURI", redirectURI);
        log.info("[GET /login] redirectURI={}", redirectURI);
        return "members/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginDto memberLoginDto, HttpServletRequest request, @RequestParam(name = "redirectURI", defaultValue = "/") String redirectURI) {
        log.info("[POST /login] redirectURI={}", redirectURI);
        Member loginMember = memberService.login(memberLoginDto.getUsername(), memberLoginDto.getPassword());

        // 세션 전용 memberDto 저장
        request.getSession().setAttribute(SessionConst.LOGIN_MEMBER, new MemberSessionDto(loginMember.getId(), loginMember.getUsername()));

        return "redirect:" + redirectURI;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/";
        }

        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/members")
    public String membersForm(Model model) {
        List<MemberListDto> dtoMembers = membersToMemberListDto();

        // id, username, rating
        model.addAttribute("members", dtoMembers);
        return "members/list";
    }

    @GetMapping("/mypage")
    public String memberDetailForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) MemberSessionDto memberSessionDto, Model model) {
        // 검증 ? => 로그인한사람의 id, 접속하는 회원 페이지에 있는 멤버랑 같은지?
        Member member = memberService.findById(memberSessionDto.getLoginMemberId());

        // 마이페이지에 넘길 정보 -> 내팀정보, 내 점수, 내 이름 ...
        MemberDetailDto memberDetailDto;

        // 팀 있는경우 ...
        if (member.getTeam() != null) {
            memberDetailDto = new MemberDetailDto(member.getTeam().getId(), member.getTeam().getTeamName(), member.getRating(), member.getUsername());
        }
        // 팀 없는경우 ...
        else {
            memberDetailDto = new MemberDetailDto(member.getRating(), member.getUsername());
        }

        model.addAttribute("memberDetailDto", memberDetailDto);
        return "members/detail";
    }

    // 특정 회원 조회
    @GetMapping("/members/{memberId}")
    public String memberForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId);

        // 마이페이지에 넘길 정보 -> 내팀정보, 내 점수, 내 이름 ...
        MemberDetailDto memberDetailDto;

        // 팀 있는경우 ...
        if (member.getTeam() != null) {
            memberDetailDto = new MemberDetailDto(member.getTeam().getId(), member.getTeam().getTeamName(), member.getRating(), member.getUsername());
        }
        // 팀 없는경우 ...
        else {
            memberDetailDto = new MemberDetailDto(member.getRating(), member.getUsername());
        }

        model.addAttribute("memberDetailDto", memberDetailDto);
        return "members/detail";
    }


    // 비즈니스 로직
    private @NonNull List<MemberListDto> membersToMemberListDto() {
        return memberService.findAll().stream()
                .map(member -> new MemberListDto(member.getId(), member.getUsername(), member.getRating()))
                .toList();
    }
}
