package sjs.assignment.sjsassignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sjs.assignment.sjsassignment.service.ScrapService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/szs")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService apiService;

    @Operation(summary = "유저 정보 스크랩", description = "유저 정보 스크랩을 합니다.")
    @PostMapping("/scrap")
    public ResponseEntity<String> userScrap( HttpServletRequest request, @RequestBody  Map<String,String> parma ) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return apiService.getUserScrap(token,parma);
    }

    @Operation(summary = "유저 환급액 반환  ", description = "유저의 환급액을 계산합니다.   .")
    @GetMapping("/refund")
    public Map<String,String> userRefund( HttpServletRequest request ) throws ParseException {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return apiService.getUserRefund(token);
    }

}

