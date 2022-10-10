package project.vinyl.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class LoginFormDto {
    @NotNull(message = "아이디를 입력하세요")
    private String loginId;
    @NotNull(message = "비밀번호를 입력하세요")
    private String password;
}
