package pl.haladyj.springsecurity8.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import pl.haladyj.springsecurity8.security.authentication.TokenAuthentication;
import pl.haladyj.springsecurity8.security.manager.TokenManager;

@Component
public class TokenAuthProvider implements AuthenticationProvider {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var tokenhash = authentication.getName();
        System.out.println(tokenhash);
        var exists = tokenManager.contains(tokenhash);

        if (exists) {
            return new TokenAuthentication(tokenhash, null, null);
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TokenAuthentication.class.equals(aClass);
    }
}
