package com.keuangan.app.dto.response;

public class YearlyChartDTO {
    private Integer year;
    private double income;
    private double expense;

    public YearlyChartDTO() {
    }

    public YearlyChartDTO(Integer year, double income, double expense) {
        this.year = year;
        this.income = income;
        this.expense = expense;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }
}
