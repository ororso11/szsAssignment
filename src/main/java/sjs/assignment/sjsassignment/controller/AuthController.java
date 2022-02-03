package sjs.assignment.sjsassignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sjs.assignment.sjsassignment.dto.UserRequestDto;
import sjs.assignment.sjsassignment.model.UserEntity;
import sjs.assignment.sjsassignment.service.UserService;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/auth/szs")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원 등록", description = "회원을 등록한다.")
    @PostMapping("/signup")
    public ResponseEntity<Long> memberSignup( @Valid @RequestBody UserRequestDto userRequestDto ) throws Exception {
        return new ResponseEntity<Long>(userService.save(userRequestDto), HttpStatus.OK);
    }
    @Operation(summary = "로그인", description = "로그인을 한다.")
    @PostMapping("/login")
    public String login(@RequestBody Map<String,String> userInfo) {
        return userService.checkLogin(userInfo);
    }


}
