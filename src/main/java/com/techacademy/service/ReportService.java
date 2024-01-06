package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
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

    // 日報削除
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

    // 日報更新
    @Transactional
    public Report update(Report report) {

        // 社員番号を検索してセット
        report.setEmployee(findById(report.getId()).getEmployee());

        // 登録日時は更新せずに登録済みの登録日時を呼び出す。
        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(findById(report.getId()).getCreatedAt());
        report.setUpdatedAt(now);

        return reportRepository.save(report);

    }

    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }
    
    // 重複チェック
    public List<Report> findExistReport(String employeeCode,LocalDate reportDate){
        return reportRepository.findAllByEmployeeCodeAndReportDate(employeeCode,reportDate);
    }

}
