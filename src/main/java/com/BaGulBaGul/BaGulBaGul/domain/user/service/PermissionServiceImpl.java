package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.Permission;
import com.BaGulBaGul.BaGulBaGul.domain.user.Role;
import com.BaGulBaGul.BaGulBaGul.domain.user.RolePermission;
import com.BaGulBaGul.BaGulBaGul.domain.user.RolePermission.RolePermissionId;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.RoleNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.PermissionRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.RolePermissionRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.RoleRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private static final String REDIS_TOPIC_PERMISSION_UPDATE = "permission_refresh";
    private static final String REDIS_MESSAGE_PERMISSION_REFRESH = "";

    private ConcurrentMap<String, Set<PermissionType>> rolePermissionCacheMap = new ConcurrentHashMap<>();

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisTemplate<String, String> redisTemplate;


    @PostConstruct
    private void init() {
        //권한 캐시를 채운다
        refreshPermission();
        //권한이 변경될 경우
        MessageListener permissionCacheUpdateListener = new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                refreshPermission();
            }
        };
        ChannelTopic channelTopic = new ChannelTopic(REDIS_TOPIC_PERMISSION_UPDATE);
        redisMessageListenerContainer.addMessageListener(permissionCacheUpdateListener,
                channelTopic);
    }

    @Override
    public boolean checkPermission(String roleName, PermissionType permissionType) {
        Set<PermissionType> permissionTypes = rolePermissionCacheMap.get(roleName);
        if(permissionTypes == null) {
            return false;
        }
        return permissionTypes.contains(permissionType);
    }

    @Override
    public boolean checkPermission(Collection<String> roleNames, PermissionType permissionType) {
        if(roleNames == null) {
            return false;
        }
        for(String roleName : roleNames) {
            Set<PermissionType> permissionTypes = rolePermissionCacheMap.get(roleName);
            if (permissionTypes == null) {
                continue;
            }
            if(permissionTypes.contains(permissionType)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Set<PermissionType> getPermissionsByRoles(Collection<String> roleNames) {
        Set<PermissionType> permissions = new HashSet<>();
        for(String roleName : roleNames) {
            Set<PermissionType> cachedPermissions = rolePermissionCacheMap.get(roleName);
            if (cachedPermissions != null) {
                permissions.addAll(cachedPermissions);
            }
        }
        return permissions;
    }


    @Override
    @Transactional
    public void addPermission(String roleName, PermissionType permissionType) {
        Role role = roleRepository.findById(roleName).orElseThrow(RoleNotFoundException::new);
        Permission permission = permissionRepository.getReferenceById(permissionType.name());
        role.getRolePermissions().add(
                RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .build()
        );
        publishRefreshEventAfterCommit();
    }

    @Override
    @Transactional
    public void addPermissions(String roleName, Collection<PermissionType> permissionTypes) {
        Role role = roleRepository.findById(roleName).orElseThrow(RoleNotFoundException::new);
        Set<RolePermission> rolePermissions = role.getRolePermissions();
        for(PermissionType permissionType : permissionTypes) {
            Permission permission = permissionRepository.getReferenceById(permissionType.name());
            rolePermissions.add(
                    RolePermission.builder()
                    .role(role)
                    .permission(permission)
                    .build()
            );
        }
        publishRefreshEventAfterCommit();
    }

    @Override
    @Transactional
    public void deletePermission(String roleName, PermissionType permissionType) {
        Role role = roleRepository.findById(roleName).orElseThrow(RoleNotFoundException::new);
        Permission permission = permissionRepository.getReferenceById(permissionType.name());
        Set<RolePermission> rolePermissions = role.getRolePermissions();
        rolePermissions.remove(
                RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .build()
        );
        publishRefreshEventAfterCommit();
    }

    @Override
    @Transactional
    public void deletePermissions(String roleName, Collection<PermissionType> permissionTypes) {
        Role role = roleRepository.findById(roleName).orElseThrow(RoleNotFoundException::new);
        Set<RolePermission> rolePermissions = role.getRolePermissions();
        for(PermissionType permissionType : permissionTypes) {
            Permission permission = permissionRepository.getReferenceById(permissionType.name());
            rolePermissions.remove(
                    RolePermission.builder()
                            .role(role)
                            .permission(permission)
                            .build()
            );
        }
        publishRefreshEventAfterCommit();
    }

    @Override
    @Transactional
    public void changePermission(String roleName, Collection<PermissionType> newPermissions) {
        Role role = roleRepository.findById(roleName).orElseThrow(RoleNotFoundException::new);
        Set<RolePermission> rolePermissions = role.getRolePermissions();
        rolePermissions.clear();
        for(PermissionType permissionType : newPermissions) {
            Permission permission = permissionRepository.getReferenceById(permissionType.name());
            RolePermission rolePermission = rolePermissionRepository
                    .findById(new RolePermissionId(roleName, permissionType.name()))
                    .orElse(RolePermission.builder()
                            .role(role)
                            .permission(permission)
                            .build());
            rolePermissions.add(rolePermission);
        }
        publishRefreshEventAfterCommit();
    }

    private void savePermission(String roleName, PermissionType permissionType) {
        Role role = roleRepository.getReferenceById(roleName);
        Permission permission = permissionRepository.getReferenceById(permissionType.name());
        rolePermissionRepository.save(
                RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .build()
        );
    }

    /**
     * 현재 트랜젝션 이후에 permission이 변경되었음을 알리는 이벤트를 발행
     */
    private void publishRefreshEventAfterCommit() {
        //redis pub/sub를 통해 이벤트 발행
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        publishRefreshEvent();
                    }
                }
        );
    }

    /**
     * permission이 변경되었음을 알리는 이벤트를 발행
     */
    private void publishRefreshEvent() {
        //redis pub/sub를 통해 이벤트 발행
        redisTemplate.convertAndSend(REDIS_TOPIC_PERMISSION_UPDATE, REDIS_MESSAGE_PERMISSION_REFRESH);
    }

    /**
     * 모든 permission 캐시를 db로부터 다시 로드
     * 조회 -> 업데이트 사이에 스레드 경합으로 이전 버전으로 업데이트될 수 있으므로 전체 synchronize
     * 역할이 갖는 권한을 업데이트할 일은 거의 없으므로 단순히 전부 재조회 + 동기화로 구현
     */
    private synchronized void refreshPermission() {
        //새로운 역할, 권한을 db에서 가져옴
        List<RolePermission> rolePermissions = rolePermissionRepository.findAll();
        Map<String, Set<PermissionType>> newRolePermissions = new HashMap<>();
        for(RolePermission rolePermission : rolePermissions) {
            String roleName = rolePermission.getRole().getName();
            PermissionType permissionType = PermissionType.valueOf(rolePermission.getPermission().getName());

            Set<PermissionType> values = newRolePermissions.get(roleName);
            if(values == null) {
                values = new HashSet<>();
                newRolePermissions.put(roleName, values);
            }
            values.add(permissionType);
        }
        //갱신
        //기존에 있던 역할 중 권한이 삭제된 역할을 제거
        for (String roleName : rolePermissionCacheMap.keySet()) {
            if(newRolePermissions.containsKey(roleName)) {
                continue;
            }
            rolePermissionCacheMap.remove(roleName);
        }
        //변경된 권한을 새로운 권한 set으로 변경
        for (String roleName : newRolePermissions.keySet()) {
            rolePermissionCacheMap.put(roleName, newRolePermissions.get(roleName)); 
        }
    }
}
