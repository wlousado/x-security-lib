package br.dev.xcode.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverser implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return Mono.just(new CustomAuthenticationToken(authorities, jwt));
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = new ArrayList<>();
        if (realmAccess != null && realmAccess.get("roles") instanceof List<?> roleList) {
            for (Object role : roleList) {
                if (role instanceof String) {
                    roles.add((String) role);
                }
            }
        }
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private static class CustomAuthenticationToken extends AbstractAuthenticationToken {
        private final Jwt jwt;

        public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Jwt jwt) {
            super(authorities);
            this.jwt = jwt;
            super.setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return jwt.getTokenValue();
        }

        @Override
        public Object getPrincipal() {
            return jwt.getSubject();
        }
    }
}
