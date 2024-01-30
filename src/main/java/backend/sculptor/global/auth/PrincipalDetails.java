package backend.sculptor.global.auth;

import backend.sculptor.domain.user.entity.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements OAuth2User {
    private Users users;
    private Map<String, Object> attributes;

    public PrincipalDetails(Users users) {
        this.users = users;
    }

    public PrincipalDetails(Users users, Map<String, Object> attributes) {
        this.users = users;
        this.attributes=attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return users.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getName() {
        return "name";
    }
}
