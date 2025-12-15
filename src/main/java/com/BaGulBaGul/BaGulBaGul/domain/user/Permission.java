package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Permission {
    //hibernate에서 enum은 복합키가 불가능하기 때문에 String으로
    @Id @Column(name = "permission_name")
    String name;

    public PermissionType getPermissionType() {
        return PermissionType.valueOf(name);
    }
}
