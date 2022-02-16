package sjs.assignment.sjsassignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sjs.assignment.sjsassignment.model.UserEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    // 아이디
    @NotNull
    @Pattern(regexp="^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$", message = "시작은 영문으로만, '_'를 제외한 특수문자 안되며 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이내로 입력해주세요.")
    @JsonProperty("userId")
    private String userId;

    // 패스워드
    @NotNull
    @Pattern(regexp="[a-zA-Z1-9]{6,12}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
    private String password;

    // 이름
    @NotNull
    @Size(min = 2, max = 8, message = "이름을 2~8자 사이로 입력해주세요.")
    private String name;

    // 주민등록번호
    @NotNull
    @Pattern(regexp="\\d{6}\\-[1-4]\\d{6}", message = "주민번호는 앞 6자리 뒤 7자리를 입력해주세요.")
    @JsonProperty("regNo")
    private String regNo;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .regNo(regNo)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

}
