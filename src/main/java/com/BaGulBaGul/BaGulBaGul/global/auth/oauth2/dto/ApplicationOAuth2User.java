package com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto;

import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant.OAuth2Provider;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class ApplicationOAuth2User implements OAuth2User {

    private String socialLoginId;
    private OAuth2Provider oAuthProvider;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    protected ApplicationOAuth2User(String oauthId, OAuth2Provider oAuthProvider, Map<String, Object> attributes) {
        this.socialLoginId = oAuthProvider + "_" + oauthId;
        this.oAuthProvider = oAuthProvider;
        this.attributes = attributes;
        this.authorities = AuthorityUtils.NO_AUTHORITIES;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.socialLoginId;
    }
}
