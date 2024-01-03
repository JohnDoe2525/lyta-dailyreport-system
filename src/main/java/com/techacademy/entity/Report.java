
package com.techacademy.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class Report {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 日付
    @NotNull
    @Column(nullable = false)
    private Date reportDate;

    // タイトル
    @NotEmpty
    @Length(max=100)
    @Column(length = 100,nullable = false)
    private String title;

    // 内容
    @Column(columnDefinition="LONGTEXT",nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "employee_code", referencedColumnName = "code", nullable = false)
    private Employee employee;

//    //社員番号(外部キー)
//    @Column(length = 10)
//    @NotEmpty
//    @Length(max = 10)
//    private String employeeCode;

    // 削除フラグ(論理削除を行うため)
    @Column(columnDefinition="TINYINT",nullable = false)
    private boolean deleteFlg;

    // 登録日時
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 更新日時
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}