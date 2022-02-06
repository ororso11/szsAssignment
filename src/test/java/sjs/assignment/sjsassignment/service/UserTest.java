package sjs.assignment.sjsassignment.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sjs.assignment.sjsassignment.dto.UserRequestDto;
import sjs.assignment.sjsassignment.jwt.JwtTokenProvider;
import sjs.assignment.sjsassignment.model.UserEntity;
import sjs.assignment.sjsassignment.service.AuthService;
import sjs.assignment.sjsassignment.service.UserService;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthService authService;

    @Test
    public void 유저정보조회_성공() throws Exception {

        // given
        String user = "ororso11";
        List<String> roles = Collections.singletonList("ROLE_USER");
        String name = "홍길동";

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("홍길동");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("860824-1655068");

        // when
        Long rtn = authService.save(userRequestDto);
        String token = jwtTokenProvider.createToken(user,roles,name);
        UserEntity userInfo = userService.selfInfoCheck(token);

        // then
        Assertions.assertNotNull(userInfo);
    }

    @Test
    public void 유저정보조회_실패() throws Exception {

        // given
        String user = "홍길동";
        List<String> roles = Collections.singletonList("ROLE_ADMIN");
        String name = "홍길동";

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("홍길동");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("860824-1655068");

        // when
        Long rtn = authService.save(userRequestDto);
        String token = jwtTokenProvider.createToken(user,roles,name);
        UserEntity userInfo = userService.selfInfoCheck(token);

        // then
        Assertions.assertNull(userInfo);
    }

}
