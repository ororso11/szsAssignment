package sjs.assignment.sjsassignment.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "TB_USER")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    // 아이디
    private String userId;
    // 패스워드
    private String password;
    // 이름
    private String name;
    // 주민등록번호
    private String regNo;

    @Builder
    public UserEntity(String userId,String password, String name, String regNo) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.regNo = regNo;

    }

}
