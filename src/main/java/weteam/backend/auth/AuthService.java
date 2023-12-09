package weteam.backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weteam.backend.auth.domain.Auth;
import weteam.backend.auth.dto.AuthDto;
import weteam.backend.auth.mapper.AuthMapper;
import weteam.backend.auth.repository.AuthRepository;
import weteam.backend.config.message.ExceptionMessage;
import weteam.backend.member.MemberService;
import weteam.backend.member.domain.Member;
import weteam.backend.member.mapper.MemberMapper;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    public Optional<Auth> findById(Long id) {
        return authRepository.findById(id);
    }

    public void join(AuthDto.Join request) {
        if (authRepository.findByUid(request.getUid()).isPresent()) {
            throw new DuplicateKeyException(ExceptionMessage.DUPLICATE.getMessage());
        }
        Member member = memberService.create(MemberMapper.instance.extractMember(request));
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        authRepository.save(AuthMapper.instance.toEntity(request, hashedPassword, member));
    }

    public void verifyUid(String uid) {
        if (authRepository.findByUid(uid).isPresent()) {
            throw new DuplicateKeyException(ExceptionMessage.DUPLICATE.getMessage());
        }
    }

    public void verifyNickname(String nickname) {
        if (memberService.findByNickname(nickname).isPresent()) {
            throw new DuplicateKeyException(ExceptionMessage.DUPLICATE.getMessage());
        }
    }
}
