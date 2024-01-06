package com.techacademy.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Employee.Role;
import com.techacademy.entity.Report;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportsController {

    private final ReportService reportService;

    @Autowired
    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(Model model,@AuthenticationPrincipal UserDetail userDetail) {

        if (userDetail.getEmployee().getRole() == Role.ADMIN) {

            model.addAttribute("listSize", reportService.findAll().size());
            model.addAttribute("reportList", reportService.findAll());
        } else {

            String code = userDetail.getEmployee().getCode();
            model.addAttribute("listSize", reportService.findAllByEmployeeCode(code).size());
            model.addAttribute("reportList", reportService.findAllByEmployeeCode(code));
        }
        return "reports/list";
    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {

        model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

//    // 従業員新規登録画面
//    @GetMapping(value = "/add")
//    public String create(@ModelAttribute Employee employee) {
//
//        return "employees/new";
//    }
//
//    // 従業員新規登録処理
//    @PostMapping(value = "/add")
//    public String add(@Validated Employee employee, BindingResult res, Model model) {
//
//        // パスワード空白チェック
//        /*
//         * エンティティ側の入力チェックでも実装は行えるが、更新の方でパスワードが空白でもチェックエラーを出さずに
//         * 更新出来る仕様となっているため上記を考慮した場合に別でエラーメッセージを出す方法が簡単だと判断
//         */
//        if ("".equals(employee.getPassword())) {
//            // パスワードが空白だった場合
//            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.BLANK_ERROR),
//                    ErrorMessage.getErrorValue(ErrorKinds.BLANK_ERROR));
//
//            return create(employee);
//
//        }
//
//        // 入力チェック
//        if (res.hasErrors()) {
//            return create(employee);
//        }
//
//        // 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
//        // (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
//        try {
//            ErrorKinds result = employeeService.save(employee);
//
//            if (ErrorMessage.contains(result)) {
//                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
//                return create(employee);
//            }
//
//        } catch (DataIntegrityViolationException e) {
//            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
//                    ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
//            return create(employee);
//        }
//
//        return "redirect:/employees";
//    }

    // 日報削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable Integer id, Model model) {

        reportService.delete(id);
        return "redirect:/reports";
    }

    // 日報更新画面
    @GetMapping("/{id}/update")
    public String edit(@PathVariable Integer id,Report report,Model model) {

        // 従業員更新処理でエラーが発生した場合の遷移はnullを受け取る
        if (id == null) {
            model.addAttribute("report", report);
        } else {
            model.addAttribute("report", reportService.findById(id));
        }
        return "reports/update";
    }

    // 従業員更新処理
    @PostMapping("/{id}/update")
    public String update(@Validated Report report,BindingResult res,Model model,@PathVariable Integer id) {

        // Entityの入力チェック
        if(res.hasErrors()) {
            return edit(null,report,model);
        }

        String checkId = reportService.findById(report.getId()).getEmployee().getCode();
        LocalDate checkOldDate = reportService.findById(id).getReportDate();
        LocalDate checkNewDate = report.getReportDate();

        // 重複チェック
        if(reportService.findExistReport(checkId,checkNewDate).isEmpty() || checkOldDate.equals(checkNewDate)) {
            reportService.update(report);
        } else {
            model.addAttribute("existError","登録されている日付です");
            return edit(null,report,model);
        }

        return "redirect:/reports";
    }

}
