package com.keuangan.app.repository;

import com.keuangan.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
        SELECT MONTH(t.date), t.type, SUM(t.amount)
        FROM Transaction t
        WHERE YEAR(t.date) = :year
        GROUP BY MONTH(t.date), t.type
        ORDER BY MONTH(t.date)
    """)
    List<Object[]> getMonthlySummary(Integer year);

    @Query("""
        SELECT YEAR(t.date), t.type, SUM(t.amount)
        FROM Transaction t
        GROUP BY YEAR(t.date), t.type
        ORDER BY YEAR(t.date)
    """)
    List<Object[]> getYearlySummary();
}