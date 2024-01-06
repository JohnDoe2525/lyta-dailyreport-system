package com.techacademy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findAllByEmployeeCode(String employeeCode );
    List<Report> findAllByEmployeeCodeAndReportDate(String employeeCode,LocalDate reportDate);
}