package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserRole;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserRole.UserRoleId;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRoleRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.RoleNotFoundException;
import com.BaGulBaGul.BaGulBaGul.global.auth.repository.RoleRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public List<String> getAllRole(Long userId) {
        User user = userRepository.findUserWithRoles(userId).orElseThrow(UserNotFoundException::new);
        return user.getUserRoles().stream()
                .map(UserRole::getRole)
                .map(role -> role.getName())
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasRole(Long userId, String roleName) {
        return userRoleRepository.existsById(new UserRoleId(userId, roleName));
    }

    @Override
    @Transactional
    public void addRole(Long userId, String roleName) {
//        User user = userRepository.getReferenceById(userId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
//        Role role = roleRepository.getReferenceById(roleName);
        Role role = roleRepository.findById(roleName).orElseThrow(RoleNotFoundException::new);
        UserRole userRole = userRoleRepository.save(
                UserRole.builder()
                        .user(user)
                        .role(role)
                        .build()
        );
        //이미 컬렉션이 초기화되어 있다면 동기화
        if (Hibernate.isInitialized(user.getUserRoles())) {
            user.getUserRoles().add(userRole);
        }
    }

    @Override
    @Transactional
    public void addRoles(Long userId, List<String> roleNames) {
        for(String roleName : roleNames) {
            addRole(userId, roleName);
        }
    }

    @Override
    @Transactional
    public void deleteRole(Long userId, String roleName) {
        UserRole userRole = userRoleRepository.getReferenceById(new UserRoleId(userId, roleName));
        userRoleRepository.delete(userRole);
        //이미 컬렉션이 초기화되어 있다면 동기화
        User user = userRepository.getReferenceById(userId);
        if (Hibernate.isInitialized(user.getUserRoles())) {
            user.getUserRoles().remove(userRole);
        }
    }

    @Override
    @Transactional
    public void deleteRoles(Long userId, List<String> roleNames) {
        for(String roleName : roleNames) {
            deleteRole(userId, roleName);
        }
    }

    @Override
    @Transactional
    public void changeRole(Long userId, List<String> newRoleNames) {
        User user = userRepository.getReferenceById(userId);

        Set<String> existingRoles = getAllRole(userId).stream().collect(Collectors.toSet());
        Set<String> newRoles = newRoleNames.stream().collect(Collectors.toSet());
        List<UserRole> toDelete = existingRoles.stream()
                .filter(x -> !newRoles.contains(x))
                .map(roleName -> new UserRoleId(userId, roleName))
                .map(userRoleRepository::getReferenceById)
                .collect(Collectors.toList());
        List<String> toAddRoleNames = newRoles.stream()
                .filter(x -> !existingRoles.contains(x))
                .collect(Collectors.toList());
        //삭제
//        userRoleRepository.deleteAllByIdInBatch(toDelete);
        userRoleRepository.deleteAll(toDelete);
        user.getUserRoles().removeAll(toDelete);
        //추가
        for(String roleName : toAddRoleNames) {
            Role role = roleRepository.getReferenceById(roleName);
            UserRole userRole = userRoleRepository.save(
                    UserRole.builder()
                            .user(user)
                            .role(role)
                            .build()
            );
            user.getUserRoles().add(userRole);
        }
    }
}
