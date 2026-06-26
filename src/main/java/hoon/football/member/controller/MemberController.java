package hoon.football.member.controller;

import hoon.football.member.domain.Member;
import hoon.football.member.dto.MemberSaveDto;
import hoon.football.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/members")
    public String membersForm(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "members/list";
    }


}
