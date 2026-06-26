package hoon.football.member.service;

import hoon.football.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByUsername(String username);

    List<Member> findAll();
}
