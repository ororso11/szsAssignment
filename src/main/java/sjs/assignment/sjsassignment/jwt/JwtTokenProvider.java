package sjs.assignment.sjsassignment.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sjs.assignment.sjsassignment.security.CustomUserDetailService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final CustomUserDetailService customUserDetailService;

    // 키
    private static String secretKey = "ThisIsA_SecretKeyForJwtExample";

    // 토큰 유효시간 | 30min
    private long tokenValidTime = 30 * 60 * 1000L;

    // 의존성 주입 후, 초기화를 수행
    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT Token 생성.
    public String createToken(String user, List<String> roles, String name){
        Claims claims = Jwts.claims().setSubject(user); // claims 생성 및 payload 설정
        claims.put("roles", roles); // 권한 설정, key/ value 쌍으로 저장

        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) // 발행 유저 정보 저장
                // loadUserByUsername 전용 , 아이디를 다른곳에서 사용하기위함.
                .claim("name",name)
                .setIssuedAt(date) // 발행 시간 저장
                .setExpiration(new Date(date.getTime() + tokenValidTime)) // 토큰 유효 시간 저장
                .signWith(SignatureAlgorithm.HS256, secretKey) // 해싱 알고리즘 및 키 설정
                .compact(); // 생성
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(this.getUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 아이디 정보 추출
    public String getUserId(String token) {
        // bearer 공백처리
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.replace("Bearer","")).getBody().getSubject();
    }

    // 토큰에서 회원 이름 정보 추출
    public String getUserName(String token) {
        // bearer 공백처리
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.replace("Bearer","")).getBody().get("name",String.class);
    }

    // Request의 Header에서 token 값을 가져옵니다. "authorization" : "token'
    public String resolveToken(HttpServletRequest request) {
        if(request.getHeader("authorization") != null )
            return request.getHeader("authorization").substring(7);
        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
