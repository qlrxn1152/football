package hoon.football.member.service.impl;

import hoon.football.member.domain.Member;
import hoon.football.member.repository.MemberRepository;
import hoon.football.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;

    @Override
    public Member save(Member member) {
        return null;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        return List.of();
    }
}
