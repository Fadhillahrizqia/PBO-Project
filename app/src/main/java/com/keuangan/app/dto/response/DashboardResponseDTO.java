package com.keuangan.app.dto.response;

public class DashboardResponseDTO {
    private double totalIncome;
    private double totalExpense;
    private double saldo;
    public DashboardResponseDTO() {
    }

    public DashboardResponseDTO(double totalIncome,
                                double totalExpense,
                                double saldo) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.saldo = saldo;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}