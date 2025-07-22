package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.user.UserRole.UserRoleId;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
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
@Builder
@Entity
@IdClass(UserRoleId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRole {
    @EqualsAndHashCode
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRoleId implements Serializable {
        private Long user;
        private String role;
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name")
    Role role;

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof UserRole)) {
            return false;
        }
        UserRole target = (UserRole) o;
        return Objects.equals(this.user.getId(), target.user.getId()) &&
                Objects.equals(this.role.getName(), target.role.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.user.getId(), this.role.getName());
    }
}
