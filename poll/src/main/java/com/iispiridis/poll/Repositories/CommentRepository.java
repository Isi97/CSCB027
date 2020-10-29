package com.iispiridis.poll.Repositories;


import com.iispiridis.poll.Models.adComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<adComment, Long>
{
    @Query("SELECT c from adComment c where c.ad.id = :adId")
    List<adComment> findAllByAd(Long adId);

    @Transactional
    @Modifying
    @Query("DELETE from adComment i where i.ad.id = :adId")
    void deleteByAdId(Long adId);

    @Transactional
    @Modifying
    @Query("DELETE from adComment i where i.id = :adId")
    void deleteById(Long adId);

    @Query("SELECT c from adComment c where c.createdBy = :userId")
    List<adComment> findByUserId(Long userId);
}
