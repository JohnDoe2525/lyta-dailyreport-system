package com.techacademy.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

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
    private final EmployeeService employeeService;

    @Autowired
    public ReportsController(ReportService reportService,EmployeeService employeeService) {
        this.reportService = reportService;
        this.employeeService = employeeService;
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

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report,@AuthenticationPrincipal UserDetail userDetail,Model model) {
        report.setEmployee(employeeService.findByCode(userDetail.getUsername()));
        return "reports/new";
    }

    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report,BindingResult res,@AuthenticationPrincipal UserDetail userDetail,Model model) {

        // 入力チェック
        if (res.hasErrors()) {
            return create(report,userDetail,model);
        }

        // 重複チェック
        if (reportService.findExistReport(userDetail.getUsername(), report.getReportDate()).isEmpty()) {
            reportService.save(report, userDetail);
        } else {
            model.addAttribute("existError","既に登録されている日付です");
            return create(report,userDetail,model);
        }

        return "redirect:/reports";
    }

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

    // 日報更新処理
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
        // 日付を変更する場合に同じ日付かつ同じ従業員の日報が存在したらエラーを表示
        // 日付を変更しない場合も日付は同じという判定になるが、他の要素(タイトル・内容)を変更できなくなるためエラーの対象外とする
        if(reportService.findExistReport(checkId,checkNewDate).isEmpty() || checkOldDate.equals(checkNewDate)) {
            reportService.update(report);
        } else {
            model.addAttribute("existError","登録されている日付です");
            return edit(null,report,model);
        }

        return "redirect:/reports";
    }

}
