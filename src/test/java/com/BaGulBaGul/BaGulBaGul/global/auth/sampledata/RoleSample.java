package com.BaGulBaGul.BaGulBaGul.global.auth.sampledata;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RoleRegisterRequest;

public class RoleSample {

    public static final String NORMAL_ROLENAME = "testRole";
    public static final String NORMAL2_ROLENAME = "testRole2";
    public static final String NORMAL3_ROLENAME = "testRole3";
    public static final String NORMAL4_ROLENAME = "testRole4";
    public static final String NORMAL5_ROLENAME = "testRole5";

    public static RoleRegisterRequest getNormalRoleRegisterRequest() {
        return RoleRegisterRequest.builder()
                .roleName(NORMAL_ROLENAME)
                .build();
    }

    public static RoleRegisterRequest getNormal2RoleRegisterRequest() {
        return RoleRegisterRequest.builder()
                .roleName(NORMAL2_ROLENAME)
                .build();
    }

    public static RoleRegisterRequest getNormal3RoleRegisterRequest() {
        return RoleRegisterRequest.builder()
                .roleName(NORMAL3_ROLENAME)
                .build();
    }

    public static RoleRegisterRequest getNormal4RoleRegisterRequest() {
        return RoleRegisterRequest.builder()
                .roleName(NORMAL4_ROLENAME)
                .build();
    }

    public static RoleRegisterRequest getNormal5RoleRegisterRequest() {
        return RoleRegisterRequest.builder()
                .roleName(NORMAL5_ROLENAME)
                .build();
    }
}
