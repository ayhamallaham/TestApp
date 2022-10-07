package com.ayham.testapp.service.impl;

import com.ayham.testapp.domain.User;
import com.ayham.testapp.repository.UserRepository;
import com.ayham.testapp.service.Exception.*;
import com.ayham.testapp.service.UserService;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        log.debug("Request to save User : {}", user);
        return userRepository.save(user);
    }

    @Override
    public User register(User user) {
        log.debug("Request to register User : {}", user);
        if (!StringUtils.hasLength(user.getUsername()) || !StringUtils.hasLength(user.getPassword())) {
            throw new MissingRequiredDataException();
        }
        List<User> existingUsers = userRepository.findByUsername(user.getUsername());
        if (!CollectionUtils.isEmpty(existingUsers)) {
            throw new UsernameAlreadyUsedException();
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return save(user);
    }

    @Override
    public User update(User user) {
        log.debug("Request to update User : {}", user);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> partialUpdate(User user) {
        log.debug("Request to partially update User : {}", user);

        return userRepository
            .findById(user.getId())
            .map(existingMyUser -> {
                if (user.getUsername() != null) {
                    existingMyUser.setUsername(user.getUsername());
                }
                if (user.getEmail() != null) {
                    existingMyUser.setEmail(user.getEmail());
                }
                if (user.getPassword() != null) {
                    existingMyUser.setPassword(user.getPassword());
                }

                return existingMyUser;
            })
            .map(userRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        log.debug("Request to get all Users");
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findOne(Long id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findOneEager(Long id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete User : {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public String login(User user) {
        log.debug("Request to login User : {}", user.getUsername());
        List<User> existingUsers = userRepository.findByUsername(user.getUsername());
        if (CollectionUtils.isEmpty(existingUsers)) {
            throw new UserDoesNotExistException();
        }
        User userToLogin = existingUsers.get(0);
        if (passwordEncoder.matches(user.getPassword(), userToLogin.getPassword())) {
            String token = base64Encoder.encodeToString(user.getId().toString().getBytes());
            userToLogin.setToken(token);
            partialUpdate(userToLogin);
            return token;
        } else {
            throw new InvalidPasswordException();
        }
    }

    @Override
    public void logout(User user) {
        List<User> existingUsers = userRepository.findByToken(user.getToken());
        if (CollectionUtils.isEmpty(existingUsers)) {
            throw new InvalidTokenException();
        }
        User userToLogout = existingUsers.get(0);
        userToLogout.setToken(null);
        update(userToLogout);
    }

    @Override
    public boolean isTokenValid(String token) {
        List<User> existingUsers = userRepository.findByToken(token);
        return !CollectionUtils.isEmpty(existingUsers);
    }

    @Override
    public String getIdFromToken(String token) {
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        return new String(decodedBytes);
    }
}
