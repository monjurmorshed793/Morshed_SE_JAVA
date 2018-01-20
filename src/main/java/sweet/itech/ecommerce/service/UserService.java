package sweet.itech.ecommerce.service;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sweet.itech.ecommerce.domain.Authority;
import sweet.itech.ecommerce.domain.User;
import sweet.itech.ecommerce.repository.AuthorityRepository;
import sweet.itech.ecommerce.repository.UserRepository;
import sweet.itech.ecommerce.security.AuthoritiesConstants;
import sweet.itech.ecommerce.security.SecurityUtils;
import sweet.itech.ecommerce.service.dto.UserDTO;
import sweet.itech.ecommerce.service.util.EcommerceUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public User registerUser(UserDTO userDTO, String password){
        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(userDTO.getEmail());
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setActivated(false);
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created information for user: {}", newUser);
        return newUser;
    }

    public User createUser(UserDTO userDTO){
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword( passwordEncoder.encode( userDTO.getPassword()==null? EcommerceUtil.generatePassword():userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created information for user: {}", user);
        return user;
    }

    public Optional<User> activateRegistration(Long id) {
        User user = userRepository.findOne(id);
        user.setActivated(true);
        return Optional.of(user);
    }

    public void updateUser(String firstName, String lastName, String email, String password){
        SecurityUtils.getCurrentUserEmail()
                .flatMap(userRepository::findOneByEmailIgnoreCase)
                .ifPresent(user->{
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPassword(password);
                    user.setEmail(email);
                });
    }

    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(){
        return SecurityUtils.getCurrentUserEmail().flatMap(userRepository::findOneByEmailIgnoreCase);
    }





}
