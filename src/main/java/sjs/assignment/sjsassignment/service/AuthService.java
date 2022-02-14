package sjs.assignment.sjsassignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sjs.assignment.sjsassignment.jwt.JwtTokenProvider;
import sjs.assignment.sjsassignment.dto.UserRequestDto;
import sjs.assignment.sjsassignment.model.UserEntity;
import sjs.assignment.sjsassignment.repository.UserRepository;
import sjs.assignment.sjsassignment.utils.AES256Cipher;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private AES256Cipher encryption;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long save(UserRequestDto userRequestDto) throws Exception {
        encryption = AES256Cipher.getInstance();
        /** 가입 가능한 유저 정보 토대로 회원가입 진행  */
        if( userRequestDto.getName().equals("홍길동") || userRequestDto.getName().equals("김둘리") || userRequestDto.getName().equals("마징가")
                || userRequestDto.getName().equals("배지터") || userRequestDto.getName().equals("손오공") ) {
            if( userRequestDto.getUserId().equals( userRepository.findByUserId( userRequestDto.getUserId()) ) ) {
                throw new IllegalArgumentException("아이디가 존재합니다.");
            } else {
                userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
                userRequestDto.setRegNo(encryption.AES_Encode(userRequestDto.getRegNo()));
            }
        } else {
            throw new IllegalArgumentException("가입 불가능한 아이디입니다.");
        }
        return userRepository.save(userRequestDto.toEntity()).getId();
    }


    @Transactional
    public String checkLogin(Map<String,String> userInfo) {
        UserEntity user = userRepository.findByUserId(userInfo.get("userId"));
        if( !passwordEncoder.matches(userInfo.get("password") ,user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(user.getUserId(), user.getRoles(), user.getName() );
    }

}
