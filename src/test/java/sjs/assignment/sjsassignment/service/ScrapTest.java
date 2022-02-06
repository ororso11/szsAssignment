package sjs.assignment.sjsassignment.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sjs.assignment.sjsassignment.dto.UserRequestDto;
import sjs.assignment.sjsassignment.jwt.JwtTokenProvider;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScrapTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthService authService;

    @Autowired
    private ScrapService scrapService;

    @Test
    public void 유저스크랩조회_성공() throws Exception {

        // given
        String user = "ororso11";
        List<String> roles = Collections.singletonList("ROLE_USER");
        String name = "홍길동";

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("홍길동");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("860824-1655068");

        Map<String,String> userInfo = new LinkedHashMap<>();
        userInfo.put("name","홍길동");
        userInfo.put("regNo", "860824-1655068");

        // when
        Long rtn = authService.save(userRequestDto);
        String token = jwtTokenProvider.createToken(user,roles,name);
        String res = String.valueOf(scrapService.getUserScrap(token,userInfo));

        // then
        Assertions.assertNotNull(res);
    }

    @Test
    public void 유저스크랩조회_실패() throws Exception {

        // given
        String user = "ororso11";
        List<String> roles = Collections.singletonList("ROLE_USER");
        String name = "홍길동";

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("홍길동");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("860824-1655068");

        Map<String,String> userInfo = new LinkedHashMap<>();
        userInfo.put("name","홍길동11");
        userInfo.put("regNo", "860824-1655068");

        // when
        Long rtn = authService.save(userRequestDto);
        String token = jwtTokenProvider.createToken(user,roles,name);
        String res = String.valueOf(scrapService.getUserScrap(token,userInfo));

        // then
        Assertions.assertNull(res);
    }

    @Test
    public void 유저스크랩_환급액_성공() throws Exception {

        // given
        String user = "ororso11";
        List<String> roles = Collections.singletonList("ROLE_USER");
        String name = "홍길동";

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("홍길동");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("860824-1655068");

        Map<String,String> userInfo = new LinkedHashMap<>();
        userInfo.put("name","홍길동");
        userInfo.put("regNo", "860824-1655068");

        // when
        Long rtn = authService.save(userRequestDto);
        String token = jwtTokenProvider.createToken(user,roles,name);
        scrapService.getUserScrap(token,userInfo);
        String res = String.valueOf(scrapService.getUserRefund(token));

        // then
        Assertions.assertNotNull(res);
    }

    @Test
    public void 유저스크랩_환급액_실패() throws Exception {

        // given
        String user = "ororso11";
        List<String> roles = Collections.singletonList("ROLE_USER");
        String name = "홍길동";

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName("홍길동");
        userRequestDto.setPassword("dudwo135");
        userRequestDto.setUserId("ororso11");
        userRequestDto.setRegNo("860824-1655068");

        Map<String,String> userInfo = new LinkedHashMap<>();
        userInfo.put("name","홍길동123");
        userInfo.put("regNo", "860824-1655068");

        // when
        Long rtn = authService.save(userRequestDto);
        String token = jwtTokenProvider.createToken(user,roles,name);
        scrapService.getUserScrap(token,userInfo);
        String res = String.valueOf(scrapService.getUserRefund(token));

        // then
        Assertions.assertNotNull(res);
    }

}
