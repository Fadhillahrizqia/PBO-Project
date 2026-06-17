package com.keuangan.app.dto.response;

public class StatSummaryDTO {
    private int totalTransactions;
    private int totalCategories;
    public StatSummaryDTO() {
    }

    public StatSummaryDTO(int totalTransactions, int totalCategories) {
        this.totalTransactions = totalTransactions;
        this.totalCategories = totalCategories;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public int getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(int totalCategories) {
        this.totalCategories = totalCategories;
    }
}