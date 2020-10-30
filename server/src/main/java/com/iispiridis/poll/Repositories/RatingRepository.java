package com.iispiridis.poll.Repositories;


import com.iispiridis.poll.Models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>
{
    @Query("SELECT r from Rating r WHERE r.user.id = :userId")
    List<Rating> getUserRatings(@Param("userId") Long userId);

    @Query("SELECT r from Rating r WHERE r.ad.id = :adId")
    List<Rating> getAdRatings(@Param("adId") Long adId);

    @Transactional
    @Modifying
    @Query("DELETE from Rating r where r.ad.id = :adId")
    void deleteByAdId(Long adId);

    @Transactional
    @Modifying
    @Query("DELETE from Rating r where r.id = :Id")
    void deleteById(Long Id);
}
