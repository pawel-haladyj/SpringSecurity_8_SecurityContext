package pl.haladyj.springsecurity8.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import pl.haladyj.springsecurity8.security.authentication.TokenAuthentication;
import pl.haladyj.springsecurity8.security.manager.TokenManager;

import java.util.UUID;

@Component
public class TokenAuthProvider implements AuthenticationProvider {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var token = authentication.getName();
        var tokenhash = token.substring(UUID.randomUUID().toString().length()-5,token.length()-1);

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
