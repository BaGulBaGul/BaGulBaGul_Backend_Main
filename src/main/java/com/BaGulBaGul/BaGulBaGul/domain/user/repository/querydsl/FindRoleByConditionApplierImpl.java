package com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl;


import static org.springframework.util.ObjectUtils.isEmpty;

import com.BaGulBaGul.BaGulBaGul.domain.user.QPermission;
import com.BaGulBaGul.BaGulBaGul.domain.user.QRole;
import com.BaGulBaGul.BaGulBaGul.domain.user.QRolePermission;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SearchRoleRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindRoleByConditionApplierImpl implements FindRoleByConditionApplier {
    private static final String[] orderNames = {"name"};

    @Override
    public <T> JPAQuery<T> applyCondition(JPAQuery<T> query, SearchRoleRequest request, QRole role) {
        //역할 이름을 포함
        if(request.getRoleName() != null) {
            query.where(role.name.contains(request.getRoleName()));
        }
        //권한 요구사항을 모두 만족
        if(request.getPermissions() != null) {
            int typeNum = 0;
            for(PermissionType permissionType : request.getPermissions()) {
                QRolePermission rolePermission = QRolePermission.rolePermission;
                QPermission permission = new QPermission("permission" + typeNum++);
                query.join(role.rolePermissions, rolePermission);
                query.join(rolePermission.permission, permission);
                query.where(permission.name.eq(permissionType.name()));
            }
        }
        return query;
    }

    @Override
    public List<OrderSpecifier> getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if(!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                getOrderSpecifier(order).ifPresent(orderSpecifiers::add);
            }
        }
        return orderSpecifiers;
    }

    @Override
    public Optional<OrderSpecifier> getOrderSpecifier(Order order) {
        String propertyName = order.getProperty();
        //만약 지원하는 정렬이 있다면 OrderSpecifier을 반환
        if(Arrays.stream(orderNames).anyMatch(s -> s.equals(propertyName))){
            com.querydsl.core.types.Order dir = com.querydsl.core.types.Order.ASC;
            if(order.getDirection().isDescending()) {
                dir = com.querydsl.core.types.Order.DESC;
            }
            return Optional.of(new OrderSpecifier(
                    dir,
                    Expressions.path(Object.class, QRole.role, propertyName)
            ));
        }
        return Optional.empty();
    }
}
