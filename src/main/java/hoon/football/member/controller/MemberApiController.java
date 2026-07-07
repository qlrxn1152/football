package hoon.football.member.controller;

import hoon.football.member.dto.MemberListDto;
import hoon.football.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/members")
    public ResponseEntity<List<MemberListDto>> members() {
        List<MemberListDto> test = memberService.findAll()
                .stream()
                .map(member -> new MemberListDto(member.getId(), member.getUsername(), member.getRating()))
                .toList();

        return ResponseEntity.ok()
                .body(test);
    }
}
