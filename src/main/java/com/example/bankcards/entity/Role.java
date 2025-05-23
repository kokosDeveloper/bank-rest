package com.example.bankcards.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.bankcards.entity.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER(
            Set.of(
                    SEE_POSSESS_CARDS,
                    REQUEST_BLOCK_CARD,
                    TRANSFER_MONEY
            )
    ),
    ADMIN(
            Set.of(
                    CREATE_CARD,
                    CHANGE_CARD_STATUS,
                    DELETE_CARD,
                    SEE_ALL_CARDS,
                    SEE_ALL_USERS,
                    DEPOSIT_MONEY
            )
    )
    ;
    @Getter
    private final Set<Permission> permissions;
    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
