package pl.haladyj.springsecurity8.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.haladyj.springsecurity8.entity.User;
import pl.haladyj.springsecurity8.repository.UserRepository;
import pl.haladyj.springsecurity8.security.model.SecurityUser;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalUser = repository.findUserByUsername(username);
        User user = optionalUser.orElseThrow(()->new UsernameNotFoundException("User not found"));
        return new SecurityUser(user);
    }
}
