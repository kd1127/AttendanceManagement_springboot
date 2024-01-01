package com.example.demo.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.demo.entity.RegisterEntity;
import com.example.demo.entity.RegisterInsertEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.error.ErrorCheck;
import com.example.demo.mapper.AttendanceMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes(types = RegisterEntity.class)
@ComponentScan("com.example.demo.mapper.AttendanceMapper")
@Transactional
public class RegisterController {
	
	static boolean sessionFlag = false;
	//	講座登録画面の定員に文字を入れて戻ると、遷移先画面が表示される前にエラーになったので、
	//	フラグを定義して、講座登録画面に遷移するとtrueになる。ControllerAdviceでは、このフラグの条件で分岐する
	static boolean pageFlag = false;
	
	//	グローバル変数
	public static String userId;
	
	@Autowired private RegisterInsertEntity riEntity;
	
	@Autowired private AttendanceMapper mapper;
	
	//	入力フォームの値の受け渡しを行うためのインスタンス生成メソッド
	@ModelAttribute
	RegisterEntity setUpForm() {
		return new RegisterEntity();
	}
	
	//	講座開催日、開始時刻・終了時刻の初期化をするメソッド
	public void listInitialize(Model model) {
		//	------------------講座開催日、開始時刻・終了時刻に関する処理------------------
		//	年を格納
		LocalDate local = LocalDate.now();
		int year = local.getYear();
		List<Integer> ylist = Arrays.asList(year, year+1, year+2, year+3, year+4, year+5);
		model.addAttribute("ylist", ylist);
		//	月を格納
		List<Integer> mlist = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		model.addAttribute("mlist", mlist);
		//	日を格納
		List<Integer> dlist = new ArrayList<>();
		for(int i=1; i<=31; i++) {
			dlist.add(i);
		}
		model.addAttribute("dlist", dlist);
		//	開始時刻「時」を格納
		List<Integer> shlist = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18);
		model.addAttribute("shlist", shlist);
		//	開始時刻「分」を格納
		List<String> smlist = Arrays.asList("00", "30");
		model.addAttribute("smlist", smlist);
		//	終了時刻「時」を格納
		List<Integer> ehlist = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18);
		model.addAttribute("ehlist", ehlist);
		//	終了時刻「分」を格納
		List<String> emlist = Arrays.asList("00", "30");
		model.addAttribute("emlist", emlist);
		//	------------------------------------------------------
	}
	
	@PostMapping("/RegisterTop")
	public String registerTop(@ModelAttribute RegisterEntity registerEntity, HttpSession session, SessionStatus sessionStatus, 
			Model model, @RequestParam String situation) {
		model.addAttribute("userId", userId);
		//	セッション情報を破棄
		session.removeAttribute("registerEntity");
		session.invalidate();
		sessionStatus.setComplete();
		registerEntity = null;
		return "Register/RegisterTop";
	}
	
	@GetMapping("/atdcRegister")
	public String atdcRegister(RegisterEntity registerEntity, Model model) {
		listInitialize(model);
		sessionFlag = false;
		pageFlag = true;
		model.addAttribute("registerEntity", registerEntity);
		return "Register/atdcRegister";
	}
	
	@PostMapping("/confirm")
	public String confirm(@ModelAttribute RegisterEntity registerEntity, BindingResult bindingResult, Model model) {		
		if(bindingResult.hasErrors()) {
			String capacity = bindingResult.getFieldError().getField();
			if(capacity.equals("capacity")) {
				registerEntity.setCapacity(-999999999);
			}
			else {
				return "Register/atdcRegister";
			}
		}
		
		//		---------エラーチェック処理------
		List<String> errorMsg = new ArrayList<>();
		ErrorCheck errorCheck = new ErrorCheck();
		
		try {
			errorMsg = errorCheck.errorCheck(registerEntity);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("registerEntity", registerEntity);
		
		if(!errorMsg.isEmpty()) {
			listInitialize(model);
			model.addAttribute("errorMsg", errorMsg);
			return "Register/atdcRegister";
		}
		//	--------------------------
		return "Register/confirm";
	}
	
	@GetMapping("/completion")
	public String completion(@ModelAttribute RegisterEntity registerEntity, BindingResult result, Model model, HttpServletRequest request, HttpSession session, SessionStatus sessionStatus) {
		if(result.hasErrors()) {
			return "Register/confirm";
		}
		
		if(sessionFlag == false) {
			//	DB格納のため、講座開催日、開始時刻・終了時刻を、the_date, start_time, end_timeに格納するため変換処理
			//	講座開催日に変換
			LocalDate tdate = LocalDate.of(registerEntity.getIyear(), registerEntity.getImonth(), registerEntity.getIday());
			Date date = Date.valueOf(tdate);
			registerEntity.setThe_date(date);
			
			//	開始時刻
			String stime = "";
			stime = stime.concat(String.valueOf(registerEntity.getShour()));
			stime = stime.concat(":");
			stime = stime.concat(String.valueOf(registerEntity.getSminute()));
			registerEntity.setStart_time(stime);
			
			//	終了時刻
			String etime = "";
			etime = etime.concat(String.valueOf(registerEntity.getEhour()));
			etime = etime.concat(":");
			etime = etime.concat(String.valueOf(registerEntity.getEminute()));
			registerEntity.setEnd_time(etime);
			//	------------------------------------------------------------------
			
			Date inpDate = Date.valueOf(LocalDate.now());
			
			//	RegisterEntityクラスからRegisterInsertEntityクラスに変換
			riEntity.setCourse_no(registerEntity.getCourse_no());
			riEntity.setCourse_name(registerEntity.getCourse_name());
			riEntity.setThe_date(registerEntity.getThe_date());
			riEntity.setStart_time(registerEntity.getStart_time());
			riEntity.setEnd_time(registerEntity.getEnd_time());
			riEntity.setCapacity(registerEntity.getCapacity());
			riEntity.setInp_date(inpDate);
//			DataBaseOperation dbo = new DataBaseOperation();
			
			try {
				mapper.insert(riEntity);
//				dbo.insertOperation(riEntity);  JDBCによるDBアクセス
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.println("登録失敗しました。");
				return "Register/confirm";
			}
			
			//			セッション情報を破棄
			session.removeAttribute("registerEntity");
			session.invalidate();
			sessionStatus.setComplete();
			sessionFlag = true;
		}
		
		return "Register/completion";
	}
}

//	デバッグツール

//System.out.println("course_no: " + registerEntity.getCourse_no());
//System.out.println("course_name: " + registerEntity.getCourse_name());
//System.out.println("the_date: " + registerEntity.getThe_date());
//System.out.println("start_time: " + registerEntity.getStart_time());
//System.out.println("end_time: " + registerEntity.getEnd_time());
//System.out.println("capacity: " + registerEntity.getCapacity());
//System.out.println("iyear: " + registerEntity.getIyear());
//System.out.println("imonth: " + registerEntity.getImonth());
//System.out.println("iday: " + registerEntity.getIday());
//System.out.println("shour: " + registerEntity.getShour());
//System.out.println("sminute: " + registerEntity.getSminute());
//System.out.println("ehour: " + registerEntity.getEhour());
//System.out.println("eminute: " + registerEntity.getEminute());

//		System.out.println("course_no: " + riEntity.getCourse_no());
//		System.out.println("course_name: " + riEntity.getCourse_name());
//		System.out.println("the_date: " + riEntity.getThe_date());
//		System.out.println("start_time: " + riEntity.getStart_time());
//		System.out.println("end_time: " + riEntity.getEnd_time());
//		System.out.println("capacity: " + riEntity.getCapacity());