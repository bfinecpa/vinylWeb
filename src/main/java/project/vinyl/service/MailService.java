package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import project.vinyl.dto.MailDto;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    private static final String title = "HiVinyl 임시 비밀번호입니다.";
    private static final String message = "안녕하세요." + "\n"
            + "HiVinyl 임시 비밀번호 안내 메일입니다." + "\n"
            + "회원님의 임시 비밀번호입니다." + "\n"
            + "로그인 후 반드시 비밀번호를 변경해주세요."+"\n\n";
    private static final String fromAddress = "alisonts137@gmail.com";

    /** 이메일 생성 **/
    public MailDto createMail(String tmpPassword, String memberEmail) {

        MailDto mailDto = MailDto.builder()
                .toAddress(memberEmail)
                .title(title)
                .message(message + tmpPassword)
                .fromAddress(fromAddress)
                .build();

        log.info("메일 생성 완료");
        return mailDto;
    }

    /** 이메일 전송 **/
    public void sendMail(MailDto mailDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailDto.getToAddress());
        mailMessage.setSubject(mailDto.getTitle());
        mailMessage.setText(mailDto.getMessage());
        mailMessage.setFrom(mailDto.getFromAddress());
        mailMessage.setReplyTo(mailDto.getFromAddress());

        mailSender.send(mailMessage);

        log.info("메일 전송 완료");
    }
}
