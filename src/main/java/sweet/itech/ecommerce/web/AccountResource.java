package sweet.itech.ecommerce.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sweet.itech.ecommerce.domain.User;
import sweet.itech.ecommerce.repository.UserRepository;
import sweet.itech.ecommerce.security.SecurityUtils;
import sweet.itech.ecommerce.service.UserService;
import sweet.itech.ecommerce.service.dto.UserDTO;
import sweet.itech.ecommerce.web.errors.EmailAlreadyUsedException;
import sweet.itech.ecommerce.web.errors.InternalServerErrorException;
import sweet.itech.ecommerce.web.errors.InvalidPasswordException;
import sweet.itech.ecommerce.web.errors.LoginAlreadyUsedException;
import sweet.itech.ecommerce.web.vm.ManagedUserVM;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    public AccountResource(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody UserDTO userDTO){
        if(!checkPasswordLength(userDTO.getPassword())){
            throw new InvalidPasswordException();
        }
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail().toLowerCase()).ifPresent(u-> {throw new EmailAlreadyUsedException();});
        User user = userService.registerUser(userDTO, userDTO.getPassword());
    }

    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value="userId") Long userId){
        Optional<User> user = userService.activateRegistration(userId);
        if(!user.isPresent()){
            throw new InternalServerErrorException("No user was found for this user");
        }
    }

    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request){
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    @GetMapping("/account")
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities()
                .map(UserDTO::new)
                .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
    }

    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserEmail().orElseThrow(() -> new InternalServerErrorException("Current user email not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getEmail().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByEmailIgnoreCase(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                userDTO.getPassword());
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
                password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
