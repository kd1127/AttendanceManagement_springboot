package com.example.demo.entity;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

import lombok.Data;

@Data
public class RegisterEntity implements Serializable{
private static final long serialVersionUID = 1L;
	
	private String course_no;
	
	private String course_name;
	
	private Date the_date;
	
	private String start_time;
	
	private String end_time;
	
	private Integer capacity;
	
	private LocalDate imp_date;
	
	private LocalDate upd_date;

	//	session field
	private Integer iyear;
	
	private Integer imonth;
	
	private Integer iday;
	
	private Integer shour;
	
	private String sminute;
	
	private Integer ehour;
	
	private String eminute;
}
