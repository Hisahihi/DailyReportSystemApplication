package com.techacademy.entity;

import java.time.LocalDateTime;


import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "address_books")
@SQLRestriction("delete_flg = false")
public class AddressBook {

    //ID（主キー）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//呼び出しはcode


    //郵便番号
    @Column(length = 7)//桁数は7桁
    @NotEmpty
    @Length(max =  7)
    private String postalNumber;//0始まりの表現に困るからIntegerでなくString

    //住所
    @Column(length = 161)//桁数１６１
    @Length(max = 161)
    private String address;

    //電話番号
    @Column(length = 11)
    @Length(min =10,max = 11)
    private String phoneNumber;//0始まりの表現に困るから型はString

    //メールアドレス
    @Column(length = 254)
    @Length(max= 254)
    @Email
    private String email;

    //削除フラグ
    @Column(columnDefinition="TINYINT", nullable = false)//columnDefinitionオプションでデータベースに保存する型を指定する
    private boolean deleteFlg;

    // 登録日時
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 更新日時
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //従業員番号
    @ManyToOne
    @JoinColumn(name = "employee_code", referencedColumnName = "code", nullable = false)
    private Employee employee;



}

