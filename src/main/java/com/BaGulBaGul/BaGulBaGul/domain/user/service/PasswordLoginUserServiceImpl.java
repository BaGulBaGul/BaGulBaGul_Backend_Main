package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.PasswordLoginUserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.DuplicatePasswordLoginUserException;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.PasswordLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordLoginUserServiceImpl implements PasswordLoginUserService {

    private final PasswordEncoder passwordEncoder;

    private final PasswordLoginUserRepository passwordLoginUserRepository;

    private final EntityManager entityManager;

    @Override
    public PasswordLoginUser findPasswordLoginUser(String loginId, String loginPassword) {
        PasswordLoginUser passwordLoginUser = passwordLoginUserRepository.findById(loginId)
                .orElseThrow(UserNotFoundException::new);
        //비밀번호가 일치하지 않으면 예외
        if(!passwordEncoder.matches(loginPassword, passwordLoginUser.getEncodedLoginPassword())) {
            throw new UserNotFoundException();
        }
        return passwordLoginUser;
    }

    @Override
    @Transactional
    public PasswordLoginUser findPasswordLoginUser(String loginId, String loginPassword, String roleName) {
        PasswordLoginUser passwordLoginUser = findPasswordLoginUser(loginId, loginPassword);
        Set<String> roleNames = passwordLoginUser.getUser().getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        //역할을 포함하지 않으면 예외
        if(!roleNames.contains(roleName)){
            throw new UserNotFoundException();
        }
        return passwordLoginUser;
    }

    @Override
    @Transactional
    public PasswordLoginUser registerPasswordLoginUser(
            PasswordLoginUserRegisterRequest passwordLoginUserRegisterRequest,
            User user
    ) {
        String loginId = passwordLoginUserRegisterRequest.getLoginId();
        String loginPassword = passwordLoginUserRegisterRequest.getLoginPassword();
        String encodedLoginPassword = passwordEncoder.encode(loginPassword);

        PasswordLoginUser passwordLoginUser = PasswordLoginUser.builder()
                .user(user)
                .loginId(loginId)
                .encodedLoginPassword(encodedLoginPassword)
                .build();
        try {
            entityManager.persist(passwordLoginUser);
            entityManager.flush();
        } catch (EntityExistsException e) {
            throw new DuplicatePasswordLoginUserException();
        }
        user.setPasswordLoginUser(passwordLoginUser);

        return passwordLoginUser;
    }

    @Override
    @Transactional
    public void deRegisterPasswordLoginUser(String loginId) {
        passwordLoginUserRepository.deleteById(loginId);
    }
}
