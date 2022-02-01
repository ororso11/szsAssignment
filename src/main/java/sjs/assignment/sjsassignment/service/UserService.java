package sjs.assignment.sjsassignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sjs.assignment.sjsassignment.config.JwtTokenProvider;
import sjs.assignment.sjsassignment.dto.UserRequestDto;
import sjs.assignment.sjsassignment.model.UserEntity;
import sjs.assignment.sjsassignment.repository.UserRepository;
import sjs.assignment.sjsassignment.utils.AES256Cipher;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AES256Cipher encryption = AES256Cipher.getInstance();

    @Transactional
    public Long save(UserRequestDto userRequestDto) throws Exception {
        if( userRequestDto.getUserId().equals( userRepository.findByUserId( userRequestDto.getUserId()) ) ) {
            throw new IllegalArgumentException("아이디가 존재합니다.");
        } else {
            userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
            userRequestDto.setRegNo(encryption.AES_Encode(userRequestDto.getRegNo()));
        }
        return userRepository.save(userRequestDto.toEntity()).getId();
    }

    @Transactional
    public Optional<UserEntity> selfInfoCheck(String token) {
        return userRepository.findByUserId(jwtTokenProvider.getUserPk(token));
    }

    @Transactional
    public String checkLogin(Map<String,String> userInfo) {
        UserEntity user = userRepository.findByUserId(userInfo.get("userId"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지않은 아이디입니다. 회원가입 진행하세요,")  );
        if( !passwordEncoder.matches(userInfo.get("password") ,user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(user.getUserId());
    }


}
