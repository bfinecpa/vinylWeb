package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.vinyl.dto.AddMemberDto;
import project.vinyl.dto.PostFormDto;
import project.vinyl.entity.Member;
import project.vinyl.entity.Post;
import project.vinyl.entity.TotalLedger;
import project.vinyl.repository.MemberRepository;
import project.vinyl.repository.TotalLedgerRepository;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final TotalLedgerService totalLedgerService;
    private final PasswordEncoder passwordEncoder;

    public Member join(AddMemberDto addMemberDto) {
        Member member = Member.createMember(addMemberDto);

        validateDuplicateMember(member.getLoginId());
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member);

        totalLedgerService.makeNewTotalLedger(member);
        return member;
    }

    public void validateDuplicateMember(String loginId) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 ID입니다.");
        }
    }

    @Transactional
    public Member modify(AddMemberDto addMemberDto) {

        Member member = memberRepository.findByLoginId(addMemberDto.getLoginId()).get();
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setName(addMemberDto.getName());
        member.setPhoneNumber(addMemberDto.getPhoneNumber());

        log.info("member.name={}", member.getName());
        log.info("addDto.name={}", addMemberDto.getName());

        return member;

    }
    public void delete(Member member) {
        memberRepository.delete(member);
    }

    public Member checkLoginId(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> passwordEncoder.matches(password, m.getPassword()))
                .orElse(null);
    }

    public Member findById(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);
    }

    public boolean checkEmail(String memberEmail) {
        return true;
    }
}
