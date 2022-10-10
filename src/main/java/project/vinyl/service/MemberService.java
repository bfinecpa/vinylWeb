package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.vinyl.entity.Member;
import project.vinyl.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member join(Member member) {
        return memberRepository.save(member);
    }
}
