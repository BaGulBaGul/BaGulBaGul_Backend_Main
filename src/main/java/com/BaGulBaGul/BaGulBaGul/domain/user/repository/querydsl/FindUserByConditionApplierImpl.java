package com.BaGulBaGul.BaGulBaGul.domain.user.repository.querydsl;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.BaGulBaGul.BaGulBaGul.domain.user.QRole;
import com.BaGulBaGul.BaGulBaGul.domain.user.QUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.QUserRole;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.UserSubType;
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
public class FindUserByConditionApplierImpl implements FindUserByConditionApplier {

    private static final String[] orderNames = {"id", "createdAt"};

    @Override
    public <T> JPAQuery<T> applyCondition(JPAQuery<T> query, UserSearchRequest userSearchRequest, QUser user) {
        //유저명이 일치
        if(userSearchRequest.getUserName() != null) {
            query.where(user.nickname.eq(userSearchRequest.getUserName()));
        }
        //가입일이 탐색 시작일 이후
        if(userSearchRequest.getJoinDateSearchStart() != null) {
            query.where(user.createdAt.after(userSearchRequest.getJoinDateSearchStart()));
        }
        //가입일이 탐색 종료일 이전
        if(userSearchRequest.getJoinDateSearchEnd() != null) {
            query.where(user.createdAt.before(userSearchRequest.getJoinDateSearchEnd()));
        }
        //서브 타입 조건
        if(userSearchRequest.getSubTypes() != null) {
            for(UserSubType userSubType : userSearchRequest.getSubTypes()) {
                if(userSubType == UserSubType.SOCIAL_LOGIN_USER) {
                    query.join(user.socialLoginUser);
                } else if(userSubType == UserSubType.PASSWORD_LOGIN_USER) {
                    query.join(user.passwordLoginUser);
                } else if(userSubType == UserSubType.ADMIN_MANAGE_PASSWORD_LOGIN_USER) {
                    query.join(user.passwordLoginUser.adminManagePasswordLoginUser);
                } else if(userSubType == UserSubType.ADMIN_MANAGE_EVENT_HOST_USER) {
                    query.join(user.adminManageEventHostUser);
                }
            }
        }
        //역할 조건
        if(userSearchRequest.getRoles() != null) {
            //모든 역할을 and로 처리
            int roleNum = 1;
            for(String roleName : userSearchRequest.getRoles()) {
                QRole role = new QRole("Role" + roleNum++);
                QUserRole userRole = QUserRole.userRole;
                query.join(user.userRoles, userRole);
                query.join(userRole.role, role);
                query.where(role.name.eq(roleName));
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
                    Expressions.path(Object.class, QUser.user, propertyName)
            ));
        }
        return Optional.empty();
    }
}
