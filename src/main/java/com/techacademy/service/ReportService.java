package com.techacademy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techacademy.entity.Report;
import com.techacademy.repository.EmployeeRepository;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository,EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.reportRepository = reportRepository;
    }

//    // 従業員削除
//    @Transactional
//    public ErrorKinds delete(String code, UserDetail userDetail) {
//
//        // 自分を削除しようとした場合はエラーメッセージを表示
//        if (code.equals(userDetail.getEmployee().getCode())) {
//            return ErrorKinds.LOGINCHECK_ERROR;
//        }
//        Employee employee = findByCode(code);
//        LocalDateTime now = LocalDateTime.now();
//        employee.setUpdatedAt(now);
//        employee.setDeleteFlg(true);
//
//        return ErrorKinds.SUCCESS;
//    }

    // 従業員一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

//    // 1件を検索
//    public Employee findByCode(String code) {
//        // findByIdで検索
//        Optional<Employee> option = employeeRepository.findById(code);
//        // 取得できなかった場合はnullを返す
//        Employee employee = option.orElse(null);
//        return employee;
//    }

}
