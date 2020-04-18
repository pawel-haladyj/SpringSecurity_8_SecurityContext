package pl.haladyj.springsecurity8.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import pl.haladyj.springsecurity8.repository.OtpRepository;
import pl.haladyj.springsecurity8.security.authentication.OptAuthentication;

import java.util.List;

@Component
public class OtpAuthProvider implements AuthenticationProvider {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var otp = authentication.getCredentials().toString();

        var otpOptional = otpRepository.findOtpByUsername(username);

        if(otpOptional.isPresent()){
            return new OptAuthentication(username,otp, List.of(()->"read"));
        }
        throw new BadCredentialsException("Bad credentials");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OptAuthentication.class.equals(aClass);
    }
}
