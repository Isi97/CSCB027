package com.iispiridis.poll.Repositories;

import com.iispiridis.poll.Models.Category;
import com.iispiridis.poll.Models.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>
{
    public Optional<Category> findByName(CategoryName name);
}
