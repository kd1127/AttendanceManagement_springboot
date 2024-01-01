package com.example.demo.entity;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;

@Data
public class CourseApplyEntity {
	/*	
	 * application tebleのidと外部キー設定している変数
	 */
	private Integer id_application;
	
	/*	
	 * course tableのcourse_noと外部キー設定している変数
	 */
	private String course_no;
	
	/*
	 * 現在日付を格納する変数
	 */
	private Date inp_date;
	
	@Autowired
	public CourseApplyEntity(Integer id_application, String course_no, Date inp_date) {
		this.id_application = id_application;
		this.course_no = course_no;
		this.inp_date = inp_date;
	}
}
