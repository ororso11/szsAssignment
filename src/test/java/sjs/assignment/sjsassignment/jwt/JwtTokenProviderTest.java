package sjs.assignment.sjsassignment.jwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void JWT_토큰_발급_성공() {

        // given
        String user = "ororso11";
        List<String> roles = Collections.singletonList("ROLE_USER");
        String name = "홍길동";

        // when
        String token = jwtTokenProvider.createToken(user,roles,name);

        // then
        Assertions.assertNotNull(token,"null 아닙니다.");
    }

    @Test
    public void JWT_토큰_발급_실패() {

        // given
        String user = "ororso11";
        List<String> roles = Collections.singletonList("ROLE_ADMIN");
        String name = "홍길동";

        // when
        String token = jwtTokenProvider.createToken(user,roles,name);

        // then
        Assertions.assertNull(token,"null 입니다.");
    }
}
