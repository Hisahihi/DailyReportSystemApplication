package com.techacademy.controller;


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
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(Model model,@AuthenticationPrincipal UserDetail userDetail) {
        //userdetailの中に必要な情報は入ってきている。ここから各画面へ情報を引き渡していく
        if (userDetail  !=null){
            model.addAttribute("listSize", reportService.findAll().size());
            model.addAttribute("reportList", reportService.findAll( ));
        }else {
            return "login/login";
        }
        return "reports/list";

    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {

        model.addAttribute("report", reportService.findById(id));

        return "reports/detail";
       }


    //日報更新画面の表示
    @GetMapping("/{id}/update")
    public String edit(@PathVariable Integer id, Model model, @ModelAttribute Report report) {

        if(id !=null) {
        //入力エラーがあった場合はreportオブジェクトの箱にエラーをもってきてエラー表示を行うためのif
        report = reportService.findById(id);
        }
        //未来日過去日をはじくif

        model.addAttribute("report", report);

        return "reports/update";


    }

    // 日報更新処理
    @PostMapping(value = "/{id}/update")
    public String update(@PathVariable Integer id,@Validated Report report, BindingResult res, Model model,@AuthenticationPrincipal UserDetail userDetail) {


        // 入力チェック
        if (res.hasErrors()) {
            return edit(null,model,report);

        }
        report.setId(id);
        ErrorKinds result = reportService.update(report);


        return "redirect:/reports";
    }


    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report ,Model model,@AuthenticationPrincipal UserDetail userDetail) {

        Employee employee = userDetail.getEmployee();

        model.addAttribute("employee", employee);
        return "reports/new";
    }

    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report,BindingResult res,@AuthenticationPrincipal UserDetail userDetail,Model model) {
     // 入力チェック
        boolean isReportDateError = reportService.existsByEmployeeAndReportDate(userDetail.getEmployee(), report.getReportDate());
        if(isReportDateError) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR), ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));

        }

        if (isReportDateError || res.hasErrors()) {
            return create(report,model,userDetail);
        }

        if(reportService.existsByEmployeeAndReportDate(userDetail.getEmployee(), report.getReportDate())) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR), ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
            return create(report,model,userDetail);
        }

//        List<Report> reportList=reportService.findAll( );
//        for(Report rep:reportList) {
//            if(userDetail.getEmployee().getCode().equals(rep.getEmployee().getCode())) { //ReportとreportListのcodsが同一か？
//                if(report.getReportDate().equals(rep.getReportDate())) { //ReportとreportListのReportDateは同一か？
//                 System.out.println("同じ");
//
//                 model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR), ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
//                 return create(report,model,userDetail);
//
//                }
//
//            }
//        }

        report.setEmployee(userDetail.getEmployee());

        reportService.save(report);

        return "redirect:/reports";
    }




    // 日報削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = reportService.delete(id, userDetail);



        return "redirect:/reports";
    }

}

