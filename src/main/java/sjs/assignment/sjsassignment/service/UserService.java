package sjs.assignment.sjsassignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sjs.assignment.sjsassignment.config.JwtTokenProvider;
import sjs.assignment.sjsassignment.model.UserEntity;
import sjs.assignment.sjsassignment.repository.UserRepository;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Optional<UserEntity> selfInfoCheck(String token) {
        return userRepository.findByUserId(jwtTokenProvider.getUserId(token));
    }
}
