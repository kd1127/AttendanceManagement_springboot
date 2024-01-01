package com.example.demo.entity;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplicationEntity {
	@NotBlank
	private String name;
	
	@NotBlank
	private String furigana;
	
	@Email
	@NotBlank
	private String mail;
	
	private Integer gender;
	
	private Date birthday;
	
	private String tel;
	
	private String remarks;
	
	private Date inp_date;
	
	private Integer year;
	
	private Integer month;
	
	private Integer day;
	
	private Boolean check1;
	
	private Boolean check2;
	
	private Boolean check3;
	
	private Boolean check4;
	
	private Boolean check5;
	
	@Autowired
	public ApplicationEntity(String name, String furigana, String mail, Integer gender, Date birthday, 
			String tel, String remarks, Date inp_date, Integer year, Integer month, Integer day, Boolean check1, 
			Boolean check2, Boolean check3, Boolean check4, Boolean check5) {
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
		this.check1 = check1;
		this.check2 = check2;
		this.check3 = check3;
		this.check4 = check4;
		this.check5 = check5;
	}
}
