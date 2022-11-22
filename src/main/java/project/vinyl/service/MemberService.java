package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.vinyl.dto.AddMemberDto;
import project.vinyl.entity.Member;
import project.vinyl.entity.TotalLedger;
import project.vinyl.repository.MemberRepository;
import project.vinyl.repository.TotalLedgerRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TotalLedgerRepository totalLedgerRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(AddMemberDto addMemberDto) {
        Member member = Member.createMember(addMemberDto);

        validateDuplicateMember(member.getLoginId());
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member);
        TotalLedger totalLedger = new TotalLedger(member);
        totalLedgerRepository.save(totalLedger);

        return member;
    }

    public void validateDuplicateMember(String loginId) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 ID입니다.");
        }
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

    public Member check(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
