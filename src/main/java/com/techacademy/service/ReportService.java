package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techacademy.entity.Report;
import com.techacademy.repository.EmployeeRepository;
import com.techacademy.repository.ReportRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository,EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.reportRepository = reportRepository;
    }

    // 従業員削除
    @Transactional
    public void delete(Integer id) {

        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);
        reportRepository.save(report);
    }

    // 日報一覧表示処理(管理者)
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 日報一覧表示処理(一般)
    public List<Report> findAllByEmployeeCode(String employeeCode) {
        return reportRepository.findAllByEmployeeCode(employeeCode);
    }

    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

}
