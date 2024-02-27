package com.techacademy.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

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
    public String list(Model model, @AuthenticationPrincipal UserDetail userDetail) {
        // 一般ユーザーと管理者ユーザーの切り分けのif
        userDetail.getEmployee(); // userDetailがログインユーザの情報をもっているから従業員の情報をもらう
        Employee emp = userDetail.getEmployee();// ログインした従業員情報がempに詰め込む クラス名 好きな名前 ⁼＝ 実際の処理内容（オブジェクト名。クラス名の中にあったメソッド）
        emp.getRole();// empから権限を取り出す
        Role role = emp.getRole();// roleに権限情報を詰め込む
        // 一般ユーザーと管理者それぞれの場合
        if (role == Employee.Role.GENERAL) {
            //roleが一般ユーザ（GENERAL）の時
            model.addAttribute("listSize", reportService.findByEmployee(emp).size());// ログインしたユーザーの日報の件数（size）をもってきている
            model.addAttribute("reportList", reportService.findByEmployee(emp));// ログインしたユーザーの日報一覧をもってきている
            //roleが管理者の時
        } else {
            model.addAttribute("listSize", reportService.findAll().size());
            model.addAttribute("reportList", reportService.findAll());
        }

        return "reports/list";

    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {

        model.addAttribute("report", reportService.findById(id));

        return "reports/detail";
    }

    // 日報更新画面の表示
    @GetMapping("/{id}/update")
    public String edit(@PathVariable Integer id, Model model, @ModelAttribute Report report,
            @AuthenticationPrincipal UserDetail userDetail) {

        // 入力エラーがあった場合はreportオブジェクトの箱にエラーをもってきてエラー表示を行うためのif
        if (id != null) {

            report = reportService.findById(id);
        }
        // 未来日過去日をはじくif

        model.addAttribute("report", report);

        return "reports/update";

    }

    // 日報更新処理
    @PostMapping(value = "/{id}/update")
    public String update(@PathVariable Integer id, @Validated Report report, BindingResult res, Model model,
            @AuthenticationPrincipal UserDetail userDetail) {

        // 入力チェック
        if (res.hasErrors()) {
            return edit(null, model, report, userDetail);

        }
        //比較材料の収集　いるもの　ログインしたユーザーの情報、reportに入っている日付情報、DBにある現状の日報

        userDetail.getEmployee();// userDetailがログインユーザの情報をもっているから従業員の情報をもらう
        Employee employee = userDetail.getEmployee();// ログインした従業員情報をemployeeに詰め込む ※記入の要約 クラス名 好きな名前 ⁼＝
                                                     // 実際の処理内容（オブジェクト名。クラス名の中にあったメソッド）
        LocalDate reportDate = report.getReportDate();// reportに日付情報をもっているからreportDateに詰め込む
        Report oldReport = reportService.findById(id);// findById(id)で主キーから紐づけされた日報をoldReportに入れる

        boolean isReportDateError = reportService.isUpdateDateError(employee, reportDate, oldReport);
        if (isReportDateError) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));

            return edit(id, model, report, userDetail);
        }

        report.setId(id);
        ErrorKinds result = reportService.update(report);

        return "redirect:/reports";
    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report, Model model, @AuthenticationPrincipal UserDetail userDetail) {

        Employee employee = userDetail.getEmployee();

        model.addAttribute("employee", employee);
        return "reports/new";
    }

    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, @AuthenticationPrincipal UserDetail userDetail,
            Model model) {
        // 入力チェック existsByEmployeeAndReportDateメソッドはReportServiceで定義
        // 具体的なとこはReportRepositoryを参照
        boolean isReportDateError = reportService.existsByEmployeeAndReportDate(userDetail.getEmployee(),
                report.getReportDate());//はいかいいえの択一でbooleanのデータ型にする isReportDateErrorに入れるものを右辺で定義する　
        if (isReportDateError) {//isReportDateErrorの中身
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));

        }

        if (isReportDateError || res.hasErrors()) {  //isReportDateErrorと入力チェックにエラーがあったら戻る
            return create(report, model, userDetail);//createの戻り値の（）の中は送るデータは定義の順番通りにすること
        }

        if (reportService.existsByEmployeeAndReportDate(userDetail.getEmployee(), report.getReportDate())) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DATECHECK_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DATECHECK_ERROR));
            return create(report, model, userDetail);
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
