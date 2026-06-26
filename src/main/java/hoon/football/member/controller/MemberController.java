package hoon.football.member.controller;

import hoon.football.member.domain.Member;
import hoon.football.member.dto.MemberLoginDto;
import hoon.football.member.dto.MemberSaveDto;
import hoon.football.member.exception.*;
import hoon.football.member.service.MemberService;
import hoon.football.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String loginForm(@ModelAttribute MemberLoginDto memberLoginDto) {
        return "members/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginDto memberLoginDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        try {
            Member loginMember = memberService.login(memberLoginDto.getUsername(), memberLoginDto.getPassword());

            request.getSession().setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
            return "redirect:/";

        } catch (MemberLoginException e1) {
            redirectAttributes.addFlashAttribute("errorMessage", e1.getMessage());
            return "redirect:/login";
        } catch (MemberUsernameLengthException e2) {
            redirectAttributes.addFlashAttribute("errorMessage", e2.getMessage());
            return "redirect:/login";
        } catch (MemberPasswordLengthException e3) {
            redirectAttributes.addFlashAttribute("errorMessage", e3.getMessage());
            return "redirect:/login";
        } catch (DuplicateUsernameException e4) {
            redirectAttributes.addFlashAttribute("errorMessage", e4.getMessage());
            return "redirect:/login";
        } catch (MemberNotFoundException e5) {
            redirectAttributes.addFlashAttribute("errorMessage", e5.getMessage());
            return "redirect:/login";
        } catch (Exception e) {
            log.error("로그인 처리 중 알수없는 에러발생", e);
            redirectAttributes.addFlashAttribute("errorMessage", "로그인중 알수없는 에러발생");
            return "redirect:/login";
        }
    }

    @GetMapping("/members")
    public String membersForm(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "members/list";
    }


}
