package project.vinyl.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AddMemberDto {
    @NotEmpty(message = "아이디를 입력하세요")
    @Pattern(regexp = "^[a-z0-9]{6,16}$", message = "6~16자 영문 소문자와 숫자만 사용하여 설정하세요.")
    private String loginId;
    @NotEmpty(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}", message = "8~16자 영문 대소문자와 숫자로 설정하세요.")
    private String password;
    @NotEmpty(message = "이름을 입력하세요")
    private String name;

    private String zipcode;

    private String streetAdr;

    private String detailAdr;

    private String phoneNumber;
}
