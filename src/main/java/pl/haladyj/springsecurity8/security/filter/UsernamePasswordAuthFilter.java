package pl.haladyj.springsecurity8.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.haladyj.springsecurity8.entity.Otp;
import pl.haladyj.springsecurity8.entity.Token;
import pl.haladyj.springsecurity8.repository.OtpRepository;
import pl.haladyj.springsecurity8.security.authentication.OptAuthentication;
import pl.haladyj.springsecurity8.security.authentication.UsernamePasswordAuthentication;
import pl.haladyj.springsecurity8.security.manager.TokenManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private OtpRepository otpRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        var username = request.getHeader("username");
        var password = request.getHeader("password");
        var otp = request.getHeader("otp");

        if (otp == null) {
            Authentication authentication = new UsernamePasswordAuthentication(username,password);
            authentication = authenticationManager.authenticate(authentication);

            String otpHash = String.valueOf(new Random().nextInt(8999)+1000).toString();
            var otpOptional = otpRepository.findOtpByUsername(username);
            var otpEntity = new Otp();
            if(otpOptional.isPresent()){
                otpEntity.setId(otpOptional.get().getId());
            }
            otpEntity.setUsername(username);
            otpEntity.setOtp(otpHash);
            otpRepository.save(otpEntity);
        } else {
            Authentication authentication = new OptAuthentication(username,otp);
            authentication = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            var tokenHash = UUID.randomUUID().toString();
            var token = new Token(username,tokenHash);
            tokenManager.add(token);
            response.setHeader("Authorization", token.getTokenhash());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/login");
    }
}
