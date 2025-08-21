package com.cosmo.cosmo.repository;

import com.cosmo.cosmo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findByUserName(@Param("userName") String userName);

    @Query("SELECT u FROM User u WHERE u.userName = :userName AND u.enabled = true")
    Optional<User> findByUserNameAndEnabled(@Param("userName") String userName);

    boolean existsByUserName(String userName);
}
