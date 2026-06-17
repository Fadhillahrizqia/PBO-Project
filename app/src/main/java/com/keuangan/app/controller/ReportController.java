package com.keuangan.app.controller;

import com.keuangan.app.dto.response.DashboardResponseDTO;
import com.keuangan.app.dto.response.MonthlyChartDTO;
import com.keuangan.app.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportController {
     private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/api/report/dashboard")
    public DashboardResponseDTO getDashboard() {
        return reportService.getDashboardSummary();
    }
    @GetMapping("/api/report/monthly-chart")
    public List<MonthlyChartDTO> getMonthlyChart(
            @RequestParam Integer year) {

        return reportService.getMonthlyChart(year);
            }               
}