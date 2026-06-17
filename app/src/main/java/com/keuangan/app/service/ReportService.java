package com.keuangan.app.service;

import com.keuangan.app.dto.response.DashboardResponseDTO;
import com.keuangan.app.model.Transaction;
import com.keuangan.app.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReportService {
    private final TransactionRepository transactionRepository;

    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public DashboardResponseDTO getDashboardSummary() {

        List<Transaction> transactions = transactionRepository.findAll();

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (Transaction t : transactions) {

            if ("INCOME".equalsIgnoreCase(t.getType())) {
                totalIncome = totalIncome.add(t.getAmount());
            }

            if ("EXPENSE".equalsIgnoreCase(t.getType())) {
                totalExpense = totalExpense.add(t.getAmount());
            }
        }

        double saldo = totalIncome.subtract(totalExpense).doubleValue();

        return new DashboardResponseDTO(
                totalIncome.doubleValue(),
                totalExpense.doubleValue(),
                saldo
        );
    }
}