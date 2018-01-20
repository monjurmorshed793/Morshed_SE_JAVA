package sweet.itech.ecommerce.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sweet.itech.ecommerce.domain.User;
import sweet.itech.ecommerce.repository.UserRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService{
    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);
    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.debug("Authenticating {}", userName);
        String lowerCaseUserName = userName.toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase = userRepository.findOneByEmailIgnoreCase(userName);
        return userFromDatabase.map(user->{
            if (!user.getActivated()) {
                throw new UserNotActivatedException("User "+lowerCaseUserName+" was not activated");
            }
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowerCaseUserName, user.getPassword(), grantedAuthorities);
        }).orElseThrow(()-> new UsernameNotFoundException("User "+lowerCaseUserName+" was not found in the database"));
    }
}
