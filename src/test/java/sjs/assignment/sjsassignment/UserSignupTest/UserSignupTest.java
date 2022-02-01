package sjs.assignment.sjsassignment.UserSignupTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import sjs.assignment.sjsassignment.dto.UserRequestDto;
import sjs.assignment.sjsassignment.repository.UserRepository;

@SpringBootTest
public class UserSignupTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원가입_아이디_실패() {
    }

    @Test
    public void 회원가입_비밀번호_정상_등록() {

    }

    @Test
    public void 회원가입_패스워드_정상_등록() {

    }

    @Test
    public void 회원가입_주민등록번호_정상_등록() {

    }

}
