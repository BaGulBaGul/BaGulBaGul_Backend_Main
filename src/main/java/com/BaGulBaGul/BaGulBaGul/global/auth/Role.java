package com.BaGulBaGul.BaGulBaGul.global.auth;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id @Column(name = "role_name")
    String name;

    @Builder.Default
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<RolePermission> rolePermissions = new HashSet<>();

    public Set<Permission> getPermissions() {
        return rolePermissions.stream().map(RolePermission::getPermission).collect(Collectors.toSet());
    }
}
