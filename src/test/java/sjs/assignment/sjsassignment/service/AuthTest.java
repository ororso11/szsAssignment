package sjs.assignment.sjsassignment.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sjs.assignment.sjsassignment.dto.UserRequestDto;
import sjs.assignment.sjsassignment.service.AuthService;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthTest {

    @Autowired
    private AuthService authService;

    @Test
    public void 회원가입_성공() throws Exception {

        // given
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("홍길동");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("860824-1655068");

        // when
        Long rts =  authService.save(userRequestDto);

        // then
        Assertions.assertNotNull(rts);
    }

    @Test
    public void 회원가입_실패() throws Exception {

        // given
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("최영재");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("123456-1234567");

        // when
        Long rts =  authService.save(userRequestDto);

        // then
        Assertions.assertNotNull(rts);
    }

    @Test
    public void 로그인_성공() throws Exception {

        // given
        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("userId","ororso11");
        userInfo.put("password","dudwo135");

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("홍길동");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("860824-1655068");

        // when
        authService.save(userRequestDto);
        String rts =  authService.checkLogin(userInfo);

        // then
        Assertions.assertNotNull(rts);
    }

    @Test
    public void 로그인_실패() throws Exception {

        // given
        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("userId","ororso11");
        userInfo.put("password","dudwo135123");

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("홍길동");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("860824-1655068");

        // when
        authService.save(userRequestDto);
        String rts =  authService.checkLogin(userInfo);

        // then
        Assertions.assertNotNull(rts);
    }

}
