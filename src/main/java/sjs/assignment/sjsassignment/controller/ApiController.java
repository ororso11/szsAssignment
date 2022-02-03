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
@RequestMapping("/api/szs")
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;

    @Operation(summary = "자기정보확인", description = "자기정보 확인 한다.")
    @PostMapping("/me")
    public Optional<UserEntity> selfInfo(@RequestParam String token) {
        return userService.selfInfoCheck(token);
    }

    @PostMapping("/hello")
    public String hello(){
        return "hello";
    }
}
