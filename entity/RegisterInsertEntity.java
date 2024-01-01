package com.example.demo.entity;

import java.sql.Date;

/*
 * DB登録用クラス
 * RegisterEntityでフォームの入力値を受け取り、このクラスに変換する
 */

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class RegisterInsertEntity {
	private String course_no;
	
	private String course_name;
	
	private Date the_date;
	
	private String start_time;
	
	private String end_time;
	
	private Integer capacity;
	
	private Date inp_date;
}
