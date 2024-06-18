package me.jeehahn.springbootdeveloper;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    public void test() {
        memberRepository.save(new Member(1L, "A"));

        Optional<Member> member = memberRepository.findById(1L);
        List<Member> allMembers = memberRepository.findAll();

        memberRepository.deleteById(1L);
    }
}
