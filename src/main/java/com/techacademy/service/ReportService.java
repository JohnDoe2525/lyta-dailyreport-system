package com.techacademy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techacademy.entity.Report;
import com.techacademy.repository.EmployeeRepository;
import com.techacademy.repository.ReportRepository;
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

    // 従業員一覧表示処理(管理者)
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 従業員一覧表示処理(一般)
    public List<Report> findAllByEmployeeCode(String employeeCode) {
        return reportRepository.findAllByEmployeeCode(employeeCode);
    }

//    // 1件を検索
//    public Report findByCode(Integer id) {
//        // findByIdで検索
//        Optional<Report> option = reportRepository.findById(id);
//        // 取得できなかった場合はnullを返す
//        Report report = option.orElse(null);
//        return report;
//    }

}
