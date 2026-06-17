package com.keuangan.app.controller;

import com.keuangan.app.dto.response.DashboardResponseDTO;
import com.keuangan.app.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}