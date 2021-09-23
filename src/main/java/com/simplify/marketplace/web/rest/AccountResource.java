package com.simplify.marketplace.web.rest;

import com.simplify.marketplace.domain.User;
import com.simplify.marketplace.repository.UserRepository;
import com.simplify.marketplace.security.SecurityUtils;
import com.simplify.marketplace.service.MailService;
import com.simplify.marketplace.service.UserService;
import com.simplify.marketplace.service.dto.AdminUserDTO;
import com.simplify.marketplace.service.dto.PasswordChangeDTO;
import com.simplify.marketplace.service.dto.UserDTO;
import com.simplify.marketplace.web.rest.errors.*;
import com.simplify.marketplace.web.rest.vm.KeyAndPasswordVM;
import com.simplify.marketplace.web.rest.vm.ManagedUserVM;
import java.net.URI;
import java.net.URISyntaxException;
// import com.simplify.marketplace.service.UserService;
import java.time.LocalDate;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PasswordEncoder passwordEncoder;

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(
        UserRepository userRepository,
        UserService userService,
        MailService mailService,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */

    @PostMapping("/register/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody AdminUserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);
        User newUser;
        System.out.print("\n\n\n---------" + userDTO + "\n\n\n");

        if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            // Lowercase the user login before comparing with database
            newUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).get();
            if (!newUser.isActivated()) {
                // SignUp Resend Condition
                //System.out.println("\n\n\n\n\nI'm in resend\n\n\n\n\n");
                mailService.sendActivationEmail(newUser);
            } else {
                //Login Condition
                newUser.setResetKey(userService.generateOTP());
                userRepository.save(newUser);
                //System.out.println("\n\n\n\n\nhello I'm in login condition"+newUser.getResetKey()+"hello\n\n\n\n\n");
                mailService.sendCreationEmail(newUser);
            }
        } else {
            //Register Condition
            //System.out.println("\n\n\n\n\nI'm in register\n\n\n\n\n");
            newUser = userService.createUser(userDTO);
            mailService.sendActivationEmail(newUser);
        }
        return ResponseEntity
            .created(new URI("/api/users/" + newUser.getLogin()))
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getLogin()))
            .body(newUser);
    }

    @PostMapping("/register/thirdparty")
    public ResponseEntity<JSONObject> Loginforgoogle(@Valid @RequestBody AdminUserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);
        JSONObject obj = new JSONObject();
        Boolean check = false;
        User newUser;
        if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            //signin condition
            newUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).get();
            check = true;
        } else {
            //Signup Condition
            newUser = userService.createUser(userDTO);
        }
        obj.put("user", newUser);
        obj.put("check", check);
        return ResponseEntity
            .created(new URI("/api/admin/users/" + newUser.getLogin()))
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getLogin()))
            .body(obj);
    }

    @PostMapping("/register/authenticate")
    public ResponseEntity<Boolean> VadlidatingOtp(@RequestBody Map<String, String> map) throws URISyntaxException {
        Boolean check = false;
        Optional<User> existingUser;
        if (!userRepository.findOneByEmailIgnoreCase(map.get("email")).isPresent()) {
            throw new BadRequestAlertException("A new user cannot get Otp wihtout using email", "userManagement", "enter email");
        } else {
            existingUser = userRepository.findOneByEmailIgnoreCase(map.get("email"));
            if (!existingUser.get().isActivated()) {
                if (existingUser.get().getActivationKey().equals(map.get("otp"))) {
                    existingUser.get().setPassword(passwordEncoder.encode("1234"));

                    existingUser.get().setActivated(true);
                    check = existingUser.get().isActivated();
                    userRepository.save(existingUser.get());
                }
            } else {
                if (existingUser.get().getResetKey().equals(map.get("otp"))) {
                    existingUser.get().setPassword(passwordEncoder.encode("1234"));
                    System.out.println("\n\n\n\n 5\n\n\n\n");
                    check = true;
                }
            }
            return ResponseEntity
                .created(new URI("/api/users/authentiacte"))
                .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", existingUser.get().getLogin()))
                .body(check);
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }
}
