package com.keuangan.app.dto.response;

public class CategoryBreakdownDTO {
    private String category;
    private double amount;
    public CategoryBreakdownDTO() {
    }

    public CategoryBreakdownDTO(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
