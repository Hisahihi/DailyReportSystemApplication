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
import com.techacademy.repository.ReportRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;


    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;

    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report) {

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報更新
    @Transactional
    public ErrorKinds update(Report report) {

        //ユーザが入力した情報の中の Id をキーとして取得
        Report info = findById(report.getId());
        // info変数にDBに登録された情報が入っている



        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();

        report.setCreatedAt(info.getCreatedAt());
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 従業員削除
    @Transactional
    public ErrorKinds delete(Integer id, UserDetail userDetail) {

        // 自分を削除しようとした場合はエラーメッセージを表示
        if (id.equals(userDetail.getEmployee().getCode())) {
            return ErrorKinds.LOGINCHECK_ERROR;
        }
        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    //従業員の情報をもとに日報検索
    public List<Report> findByEmployee(Employee employee){
        return reportRepository.findByEmployee(employee);
    }

  /**  //従業員個人の日報のみ検索
    public List<Report> findId(Employee employee){
         return reportRepository.findId(employee);
    }
*/
    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    //新規登録画面日付入力チェックのためのメソッド
    public boolean existsByEmployeeAndReportDate(Employee employee,LocalDate reportDate) {
        return reportRepository.existsByEmployeeAndReportDate(employee, reportDate);
    }

    //日報更新画面日付チェックのためのメソッド
    public boolean isUpdateDateError(Employee employee,LocalDate reportDate, Report oldReport) {
      //日報テーブルに、「画面で表示中の従業員 かつ 入力した日付」の日報データが存在する場合エラー
        //※画面で表示中の日報データを除いたものについて、上記のチェックを行なうものとします
        List<Report> reportList =reportRepository.findByEmployee(employee);
        if (reportList != null && oldReport.getEmployee().getCode().equals(employee.getCode())
                && !oldReport.getReportDate().equals(reportDate)) {
            for(Report report:reportList) {
                if(report.getReportDate().equals(reportDate)) {
                    return true;
                }
            }

        }
        return false;
    }


}

