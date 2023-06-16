package com.example.demo.entity;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ApplicationEntity {
	private String name;
	
	private String furigana;
	
	@Email
	private String mail;
	
	private Integer gender;
	
	private Date birthday;
	
	private String tel;
	
	private String remarks;
	
	private Date inp_date;
	
	private Integer year;
	
	private Integer month;
	
	private Integer day;
	
	@Autowired
	public ApplicationEntity(String name, String furigana, String mail, Integer gender, Date birthday, 
			String tel, String remarks, Date inp_date, Integer year, Integer month, Integer day) {
		this.name = name;
		this.furigana = furigana;
		this.mail = mail;
		this.gender = gender;
		this.birthday = birthday;
		this.tel = tel;
		this.remarks = remarks;
		this.inp_date = inp_date;
		this.year = year;
		this.month = month;
		this.day = day;
	}
}
