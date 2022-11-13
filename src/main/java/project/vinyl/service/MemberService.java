package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.vinyl.entity.Member;
import project.vinyl.entity.TotalLedger;
import project.vinyl.repository.MemberRepository;
import project.vinyl.repository.TotalLedgerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TotalLedgerRepository totalLedgerRepository;

    public Member join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        TotalLedger totalLedger = new TotalLedger(member);
        totalLedgerRepository.save(totalLedger);
        return member;
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findByLoginId(member.getLoginId());
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
