package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserRole;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserRole.UserRoleId;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRoleRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.Role;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.RoleNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.RoleRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import java.util.Collection;
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

    private final PermissionService permissionService;

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
    public void addRole(AuthenticatedUserInfo requestUserAuthInfo, Long userId, String roleName) {
        checkRequestUserPermission(requestUserAuthInfo, userId, List.of(roleName));
        addRole(userId, roleName);
    }

    @Override
    @Transactional
    public void addRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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
    public void addRoles(AuthenticatedUserInfo requestUserAuthInfo, Long userId, Collection<String> roleNames) {
        checkRequestUserPermission(requestUserAuthInfo, userId, roleNames);
        addRoles(userId, roleNames);

    }

    @Override
    @Transactional
    public void addRoles(Long userId, Collection<String> roleNames) {
        Set<String> roleNamesSet = roleNames.stream().collect(Collectors.toSet());
        for(String roleName : roleNamesSet) {
            addRole(userId, roleName);
        }
    }

    @Override
    @Transactional
    public void deleteRole(AuthenticatedUserInfo requestUserAuthInfo, Long userId, String roleName) {
        checkRequestUserPermission(requestUserAuthInfo, userId, List.of(roleName));
        deleteRole(userId, roleName);
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
    public void deleteRoles(AuthenticatedUserInfo requestUserAuthInfo, Long userId, Collection<String> roleNames) {
        checkRequestUserPermission(requestUserAuthInfo, userId, roleNames);
        deleteRoles(userId, roleNames);
    }

    @Override
    @Transactional
    public void deleteRoles(Long userId, Collection<String> roleNames) {
        Set<String> roleNamesSet = roleNames.stream().collect(Collectors.toSet());
        for(String roleName : roleNamesSet) {
            deleteRole(userId, roleName);
        }
    }

    @Override
    @Transactional
    public void changeRole(AuthenticatedUserInfo requestUserAuthInfo, Long userId, Collection<String> newRoleNames) {
        checkRequestUserPermission(requestUserAuthInfo, userId, newRoleNames);
        //유저의 역할을 변경
        changeRole(userId, newRoleNames);
    }

    @Override
    @Transactional
    public void changeRole(Long userId, Collection<String> newRoleNames) {
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

    /**
     * 변경 요청자가 변경 대상자에 대해 작업을 수행할 권한이 있는지를 검사
     * MANAGE_USER권한 검사 + ADMIN 유저 보호
     * @param authenticatedUserInfo 인증된 변경 요청자
     * @param userId 변경 대상 유저 id
     * @param targetRoleNames 변경할 역할들
     */
    private void checkRequestUserPermission(AuthenticatedUserInfo authenticatedUserInfo, Long userId, Collection<String> targetRoleNames) {
        //MANAGE_USER 권한이 없다면 금지
        if(!permissionService.checkPermission(authenticatedUserInfo.getRoles(), PermissionType.MANAGE_USER)) {
            throw new GeneralException(ResponseCode.FORBIDDEN);
        }
        //만약 변경 요청자가 admin이 아니라면
        if(!authenticatedUserInfo.hasRole(GeneralRoleType.ADMIN)) {
            //변경 대상이 admin이라면 금지
            if(hasRole(userId, GeneralRoleType.ADMIN.name())) {
                throw new GeneralException(ResponseCode.FORBIDDEN);
            }
            //변경할 역할에 ADMIN이 있다면 금지
            if(targetRoleNames.contains(GeneralRoleType.ADMIN.name())) {
                throw new GeneralException(ResponseCode.FORBIDDEN);
            }
        }
    }
}
