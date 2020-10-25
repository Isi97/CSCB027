package com.iispiridis.poll.Repositories;


import com.iispiridis.poll.Models.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long>
{
    Optional<Ad> findById(Long adId);

    @Query("SELECT a from Ad a where a.createdBy = :userId")
    List<Ad> findByUserId(Long userId);
}
