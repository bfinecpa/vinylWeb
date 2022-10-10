package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.vinyl.entity.Member;
import project.vinyl.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
