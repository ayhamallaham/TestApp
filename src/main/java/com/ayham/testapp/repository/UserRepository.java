package com.ayham.testapp.repository;

import com.ayham.testapp.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MyUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);

    List<User> findByToken(String token);

    @Query("select user from User user left join fetch user.likes where user.id =:id")
    Optional<User> findOne(@Param("id") Long id);
}
