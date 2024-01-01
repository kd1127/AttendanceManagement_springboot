package com.example.demo.entity;

import java.io.Serializable;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="ユーザーIDを入力してください。")
	private String userId;
	@NotBlank(message="パスワードを入力してください。")
	private String passWd;
	@NotBlank(message="再入力パスワードを入力してください。")
	private String re_passWd;
	private Integer authority;
	private Date inp_date;
	
	@Autowired
	public UserEntity(String passWd, String userId, String re_passWd, Integer authority, Date inp_date) {
		this.userId = userId;
		this.passWd = passWd;
		this.re_passWd = re_passWd;
		this.authority = authority;
		this.inp_date = inp_date;
	}
}
