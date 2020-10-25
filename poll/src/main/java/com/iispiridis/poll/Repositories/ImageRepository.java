package com.iispiridis.poll.Repositories;

import com.iispiridis.poll.Models.DBImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<DBImage, Long>
{

    @Query("SELECT i from DBImage i where i.ad.id = :adId")
    List<DBImage> findAllByAd(Long adId);
}
