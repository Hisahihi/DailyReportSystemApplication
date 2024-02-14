package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ReportService(ReportRepository reportRepository, PasswordEncoder passwordEncoder) {
        this.reportRepository = reportRepository;
        this.passwordEncoder = passwordEncoder;
    }

 /**   // 従業員保存
    @Transactional
    public ErrorKinds save(Report report) {

        // パスワードチェック
        ErrorKinds result = reportPasswordCheck(report);
        if (ErrorKinds.CHECK_OK != result) {
            return result;
        }

        // 従業員番号重複チェック
        if (findById(report.getId()) != null) {
            return ErrorKinds.DUPLICATE_ERROR;
        }

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 従業員更新
    @Transactional
    public ErrorKinds update(Report report) {

        // ・テーブルから既存のパスワードを取得する（ユーザが入力した従業員情報の中の Id をキーとして取得
        Report info = findById(report.getId());// info変数にDBに登録された情報が入っている

        // パスワードが空の場合→テーブルに登録されているパスワードを取得して設定する
        if ("".equals(report.getPassword())) {

            report.setPassword(info.getPassword());
            // ・ユーザが入力した従業員情報の中に取得した既存のパスワードを設定する
        } else {

            // パスワードチェック
            ErrorKinds result = reportPasswordCheck(report);
            if (ErrorKinds.CHECK_OK != result) {
                return result;
            }
        }

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();

        report.setCreatedAt(info.getCreatedAt());
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 従業員削除
    @Transactional
    public ErrorKinds delete(String id, UserDetail userDetail) {

        // 自分を削除しようとした場合はエラーメッセージを表示
        if (id.equals(userDetail.getReport().getId())) {
            return ErrorKinds.LOGINCHECK_ERROR;
        }
        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }
*/
    // 従業員一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }
/**
    // 1件を検索
    public Report findById(String id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    // 従業員パスワードチェック
    private ErrorKinds reportPasswordCheck(Report report) {

        // 従業員パスワードの半角英数字チェック処理
        if (isHalfSizeCheckError(report)) {

            return ErrorKinds.HALFSIZE_ERROR;
        }

        // 従業員パスワードの8文字～16文字チェック処理
        if (isOutOfRangePassword(report)) {

            return ErrorKinds.RANGECHECK_ERROR;
        }

        report.setPassword(passwordEncoder.encode(report.getPassword()));

        return ErrorKinds.CHECK_OK;
    }

    // 従業員パスワードの半角英数字チェック処理
    private boolean isHalfSizeCheckError(Report report) {

        // 半角英数字チェック
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher matcher = pattern.matcher(report.getPassword());
        return !matcher.matches();
    }

    // 従業員パスワードの8文字～16文字チェック処理
    public boolean isOutOfRangePassword(Report report) {

        // 桁数チェック
        int passwordLength = report.getPassword().length();
        return passwordLength < 8 || 16 < passwordLength;
    }
*/
}

