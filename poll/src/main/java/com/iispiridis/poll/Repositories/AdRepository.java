package com.iispiridis.poll.Repositories;


import com.iispiridis.poll.Models.Ad;
import com.iispiridis.poll.Models.CategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long>
{
    Optional<Ad> findById(Long adId);

    @Query("SELECT a from Ad a where a.createdBy = :userId")
    List<Ad> findByUserId(Long userId);


    @Query("SELECT a from Ad a join a.categories c WHERE c.name IN (:cnames) group by a having count(c)=:ccount")
    Page<Ad> findByCategory(Pageable pageable, @Param("cnames") List<CategoryName> c, @Param("ccount") Long categoryCount);
}
