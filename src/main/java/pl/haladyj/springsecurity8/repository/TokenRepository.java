package pl.haladyj.springsecurity8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.haladyj.springsecurity8.entity.Token;

;import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,String> {
    Optional<Token> findTokenByTokenhash(String tokenhash);
    Optional<Token> findTokenByUsername(String username);
}
