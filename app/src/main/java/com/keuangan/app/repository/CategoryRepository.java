package com.keuangan.app.repository;

import com.keuangan.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.userId = :userId OR c.userId = 'admin'")
    List<Category> findAllByUserIdOrSystem(@Param("userId") String userId);

    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name) AND c.type = :type AND (c.userId = :userId OR c.userId = 'admin')")
    Optional<Category> findByNameTypeAndUserOrSystem(@Param("name") String name, @Param("type") String type, @Param("userId") String userId);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.name) = LOWER(:name) AND c.type = :type AND (c.userId = :userId OR c.userId = 'admin')")
    boolean existsByNameTypeAndUserOrSystem(@Param("name") String name, @Param("type") String type, @Param("userId") String userId);
}