package sjs.assignment.sjsassignment.dto;

import lombok.Getter;
import sjs.assignment.sjsassignment.model.UserEntity;

@Getter
public class UserResponseDto {
    // 아이디
    private String userId;
    // 패스워드
    private String password;
    // 이름
    private String name;
    // 주민등록번호
    private String regNo;

    public UserResponseDto(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.password = userEntity.getPassword();
        this.name = userEntity.getName();
        this.regNo = userEntity.getRegNo();
    }

}
