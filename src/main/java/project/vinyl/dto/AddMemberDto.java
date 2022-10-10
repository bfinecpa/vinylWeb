package project.vinyl.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AddMemberDto {
    @NotEmpty(message = "아이디를 입력하세요")
    private String loginId;
    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;
    @NotEmpty(message = "이름을 입력하세요")
    private String name;

    private String address;
    private String phoneNumber;
}
