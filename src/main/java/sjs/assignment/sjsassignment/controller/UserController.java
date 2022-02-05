package sjs.assignment.sjsassignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sjs.assignment.sjsassignment.dto.UserRequestDto;
import sjs.assignment.sjsassignment.model.UserEntity;
import sjs.assignment.sjsassignment.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@RestController
@RequestMapping("/user/szs")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "자기정보확인", description = "자기정보 확인합니다.")
    @PostMapping("/me")
    public Optional<UserEntity> selfInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return userService.selfInfoCheck(token);
    }
}
