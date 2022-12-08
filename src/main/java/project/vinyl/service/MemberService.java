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
        if (checkEmail(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
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

    public boolean checkEmail(String email) {
        log.info("Service checkEmail");
        log.info("email: {}", email);
        log.info("memberRepository.existsByEmail(email): {}", memberRepository.existsByEmail(email));
        return memberRepository.existsByEmail(email);
    }

    public String getTmpPassword() {
        char[] charSet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String password = "";

        /* 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 조합 */
        int index = 0;
        for(int i = 0; i < 10; i++){
            index = (int) (charSet.length * Math.random());
            password += charSet[index];
        }

        log.info("임시 비밀번호 생성");
        log.info("password: {}", password);
        return password;
    }

    /** 임시 비밀번호로 업데이트 **/
    public void updatePassword(String tmpPassword, String memberEmail) {

        String encryptPassword = passwordEncoder.encode(tmpPassword);
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        member.updatePassword(encryptPassword);
        log.info("임시 비밀번호 업데이트");
    }
}
