package pl.haladyj.springsecurity8.security.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.haladyj.springsecurity8.entity.Token;
import pl.haladyj.springsecurity8.repository.TokenRepository;

@Component
public class TokenManager {
    @Autowired
    private TokenRepository tokenRepository;

    public void add(Token token) {
        tokenRepository.save(token);
    }

    public boolean contains(String tokenhash) {
        var repoToken = tokenRepository.findTokenByTokenhash(tokenhash);
        return repoToken.isPresent();
    }
}
