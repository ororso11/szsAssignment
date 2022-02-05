package sjs.assignment.sjsassignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjs.assignment.sjsassignment.dto.UserRequestDto;
import sjs.assignment.sjsassignment.model.UserEntity;
import sjs.assignment.sjsassignment.service.ApiService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/szs")
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @Operation(summary = "유저 정보 스크랩", description = "유저 정보 스크랩을 합니다.")
    @PostMapping("/scrap")
    public String userScrap(HttpServletRequest request,  @RequestBody UserRequestDto userRequestDto) {

        String token = request.getHeader("Authorization");
        token.replace("Bearer","");
        return apiService.getScrap(token,userRequestDto);
    }

}
