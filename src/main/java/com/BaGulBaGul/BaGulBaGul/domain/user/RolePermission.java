package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.user.RolePermission.RolePermissionId;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@IdClass(RolePermissionId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RolePermission {

    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RolePermissionId implements Serializable {
        private String role;
        private String permission;
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name")
    Role role;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_name")
    Permission permission;

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof RolePermission)) {
            return false;
        }
        RolePermission target = (RolePermission) o;
        return Objects.equals(this.role.getName(), target.role.getName()) &&
                Objects.equals(this.permission.getName(), target.permission.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.role.getName(), this.permission.getName());
    }
}

