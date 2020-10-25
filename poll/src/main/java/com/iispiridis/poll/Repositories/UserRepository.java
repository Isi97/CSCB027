package com.iispiridis.poll.Repositories;

import com.iispiridis.poll.Models.ContactInformation;
import com.iispiridis.poll.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u.contactInformation from User u where u.id = :userId")
    ContactInformation getUserContactInformation(@Param("userId") Long userId);

    Optional<User> findById(Long id);
}
