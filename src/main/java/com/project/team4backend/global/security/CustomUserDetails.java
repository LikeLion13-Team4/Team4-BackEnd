package com.project.team4backend.global.security;

import com.project.team4backend.domain.auth.entity.enums.IsTempPassword;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final String role;
    private final IsTempPassword isTempPassword;

    // 해당 User 의 권한을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
    
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Account 가 만료되었는지?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Account 가 잠겨있는지?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Credential 만료되지 않았는지?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 활성화가 되어있는지?
    @Override
    public boolean isEnabled() {
        // User Entity 에서 Status 가져온 후 true? false? 검사
        return true;
    }
}




