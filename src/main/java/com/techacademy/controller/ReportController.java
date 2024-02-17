package com.techacademy.controller;


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
         return "reports/update";
    }
    /**    if(id !=null) {
        //入力エラーがあった場合はemployeeオブジェクトの箱ににエラーをもってきてエラー表示を行うためのif
        report = reportService.findById(id);
        }
        model.addAttribute("report", report);


    }

    // 従業員更新処理
    @PostMapping(value = "/{code}/update")
    public String update(@Validated Report report, BindingResult res, Model model) {


        // 入力チェック
        if (res.hasErrors()) {
            return edit(null,model,report);
        }


        // 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
        // (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
        try {
            ErrorKinds result = reportService.update(report);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return edit(report.getId(),model,report);
            }

        } catch (DataIntegrityViolationException e) {
           // e.printStackTrace();
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
            return edit(report.getId(),model,report);
        }


        return "redirect:/report";
    }
*/

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
        if (res.hasErrors()) {
            return create(report,model,userDetail);
        }

        report.setEmployee(userDetail.getEmployee());

        reportService.save(report);

        return "redirect:/reports";
    }

        //空白チェック

    /**
        if ("".equals(report.getPassword())) {
            // パスワードが空白だった場合
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.BLANK_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.BLANK_ERROR));

            return create(report);

        }



        // 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
        // (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
        try {
            ErrorKinds result = reportService.save(report);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return create(report);
            }

        } catch (DataIntegrityViolationException e) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
            return create(report);
        }

        return "redirect:/report";
    }

    // 従業員削除処理
    @PostMapping(value = "/{code}/delete")
    public String delete(@PathVariable String id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = reportService.delete(id, userDetail);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findById(id));
            return detail(id, model);
        }

        return "redirect:/report";
    }
*/
}

