package com.ayham.testapp.web.rest;

import com.ayham.testapp.domain.User;
import com.ayham.testapp.repository.UserRepository;
import com.ayham.testapp.service.UserService;
import com.ayham.testapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link User}.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "user";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    public UserResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /users/register} : Create a new user.
     *
     * @param user the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the user has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/users/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) throws URISyntaxException {
        log.debug("REST request to register a User : {}", user);
        if (user.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User result = userService.register(user);
        return ResponseEntity
            .created(new URI("/api/users/register/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /users/login} : login a user.
     *
     * @param user the user to login.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body containing token, or with status {@code 400 (Bad Request)} in case of failure.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/users/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody User user) throws URISyntaxException {
        log.debug("REST request to login a User : {}", user.getUsername());
        String token = userService.login(user);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, "")).body(token);
    }

    /**
     * {@code POST  /users/logout} : logout user.
     *
     * @param user the user to logout.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/users/logout")
    public ResponseEntity<String> logoutUser(@Valid @RequestBody User user) throws URISyntaxException {
        log.debug("REST request to login a User : {}", user.getUsername());
        userService.logout(user);
        return ResponseEntity.ok().body("Success");
    }
}
