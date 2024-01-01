package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.demo.entity.RegisterEntity;
import com.example.demo.entity.RegisterInsertEntity;
import com.example.demo.error.CourseUpdateErrorCheck;
import com.example.demo.error.ErrorCheck;
import com.example.demo.mapper.AttendanceMapper;
import com.example.demo.service.ApplicationService;

import jakarta.servlet.http.HttpSession;

@Controller
@Transactional
@SessionAttributes(types = RegisterEntity.class)
public class AdminController {
	@Autowired private ApplicationService service;
	@Autowired private AttendanceMapper mapper;
	@Autowired private CourseUpdateErrorCheck errorCheck;
	@Autowired private ErrorCheck registerErrorCheck;
	
	//	グローバル変数
	private String course_no;
	private RegisterEntity registerEntity;
	
	@ModelAttribute
	RegisterEntity setUpForm() {
		return new RegisterEntity();
	}
	
	@GetMapping("/search")
	public String search(Model model, @ModelAttribute RegisterEntity registerEntity) {
		createHoldingTime(model);
		model.addAttribute("registerEntity", registerEntity);
		return "Admin/search";
	}
	
	@PostMapping("/courseList")
	public String courseList(Model model, @ModelAttribute RegisterEntity registerEntity, @RequestParam String situation) {
		boolean flag = registerErrorCheck.courseSearchErrorCheck(registerEntity, model);
		
		//	入力エラーがある場合、自画面遷移、なければ講座一覧画面へ
		if(flag == true) {
			createHoldingTime(model);
			model.addAttribute("registerEntity", registerEntity);
			return "Admin/search";
		}
		else {
			if(!situation.equals("講座一覧")) {
				//	講座検索画面での入力値を保持
				this.registerEntity = registerEntity;
			}
			if(situation.equals("戻る")) {
				this.registerEntity.setCourse_no("");
				this.registerEntity.setCourse_name("");
				this.registerEntity.setIyear(null);
				this.registerEntity.setImonth(null);
				this.registerEntity.setIday(null);
				this.registerEntity.setShour(null);
				this.registerEntity.setSminute("");
				this.registerEntity.setEhour(null);
				this.registerEntity.setEminute("");
				this.registerEntity.setCapacity(null);
			}
			//	DB操作するためにsituationをRegisterEntityの当該フィールドに格納
			this.registerEntity.setSituation(situation);
			//	講座一覧の表を作成するために各カラムのデータを取得してaddAttributeする
			List<String> dateAndTimeList = new ArrayList<>();
			List<String> courseNoList = new ArrayList<>();
			List<String> courseNameList = new ArrayList<>();
			List<Integer> capacityList = new ArrayList<>();
			dateAndTimeList = service.dateAndTimeSearch(this.registerEntity);
			courseNoList = mapper.courseNoSearch(this.registerEntity);			
			courseNameList = mapper.courseNameSearch(this.registerEntity);			
			capacityList = mapper.capacitySearch(this.registerEntity);
			
			//	講座一覧の表で何行出力するか求めて、th:eachの終了条件式にする
			List<Integer> maxList = new ArrayList<>();
			for(int i=0; i<courseNoList.size(); i++) {
				maxList.add(i);
			}
			//	各行のデータの状態を取得
			List<String> situationList = new ArrayList<>();
			situationList = service.situationDemand(this.registerEntity);
			
			model.addAttribute("maxList", maxList);
			model.addAttribute("courseNoList", courseNoList);
			model.addAttribute("courseNameList", courseNameList);
			model.addAttribute("dateAndTimeList", dateAndTimeList);
			model.addAttribute("capacityList", capacityList);
			model.addAttribute("situationList", situationList);
			return "Admin/courseList";
		}
	}
	
	@GetMapping("/courseDelete")
	public String courseDelete(@RequestParam String course_no_de, Model model, RegisterEntity registerEntity) {
		registerEntity = mapper.courseSelect(course_no_de);
		registerEntity = service.theDateDisassembly(registerEntity);	
		this.course_no = course_no_de;
		model.addAttribute("registerEntity", registerEntity);
		return "Admin/courseDelete";
	}
	
	@PostMapping("/deleteCompletion")
	public String deleteCompletion(@ModelAttribute RegisterEntity registerEntity, HttpSession session, SessionStatus sessionStatus) {
		List<String> courseNoList = new ArrayList<>();
		courseNoList = mapper.courseNoFindAll();
		
		for(int i=0; i<courseNoList.size(); i++) {
			if(this.course_no.equals(courseNoList.get(i))) {
				mapper.Preparation();
				mapper.courseDelete(this.course_no);
				mapper.courseApplyDelete(this.course_no);
				mapper.postProcessing();
			}
		}
		
		//	セッション情報を破棄
		session.removeAttribute("registerEntity");
		session.invalidate();
		sessionStatus.setComplete();
		return "Admin/deleteCompletion";
	}
	
	@GetMapping("/courseUpdate")
	public String courseUpdate(Model model, @RequestParam String course_no_ud, RegisterEntity registerEntity) {
		try {
			for(int i=1; i<course_no_ud.length(); i++) {
				int j = i + 1;
				//	変数numにcourse_no_udのi, j番目の値を入れてエラーがでるかどうかを調べる
				int num = Integer.parseInt(course_no_ud.substring(i, j));
				this.course_no = course_no_ud;
			}
			registerEntity = mapper.courseSelect(this.course_no);
			registerEntity = service.theDateDisassembly(registerEntity);	
		}
		catch(NumberFormatException e) {}
		
		model.addAttribute("registerEntity", registerEntity);
		createHoldingTime(model);
		return "Admin/courseUpdate";
	}
	
	@PostMapping("/updateConfirm")
	public String updateConfirm(@ModelAttribute RegisterEntity registerEntity, Model model) {
		//	講座修正画面に再度、講座番号を表示させるためにgl_course_noを使用してRegisterEntityに設定
		registerEntity.setCourse_no(this.course_no);			
		model.addAttribute("registerEntity", registerEntity);
		int judge = errorCheck.courseUpdateErrorCheck(registerEntity, model);
		createHoldingTime(model);
		if(judge == 1) {
			return "Admin/courseUpdate";
		}
		String start_time = String.valueOf(registerEntity.getShour()).concat(":").concat(registerEntity.getSminute());
		String end_time = String.valueOf(registerEntity.getEhour()).concat(":").concat(registerEntity.getEminute());
		registerEntity.setStart_time(start_time);
		registerEntity.setEnd_time(end_time);
		model.addAttribute("registerEntity", registerEntity);
		return "Admin/updateConfirm";
	}
	
	@GetMapping("/updateCompletion")
	public String updateCompletion(@ModelAttribute RegisterEntity registerEntity, HttpSession session, SessionStatus sessionStatus) {		
		RegisterInsertEntity riEntity = new RegisterInsertEntity();
		riEntity = service.registerDataChangeStore(registerEntity);
		mapper.courseUpdate(riEntity);
		//	セッション情報を破棄
		session.removeAttribute("registerEntity");
		session.invalidate();
		sessionStatus.setComplete();
		return "Admin/updateCompletion";
	}
	
	public void createHoldingTime(Model model) {
		List<Integer> yearList = new ArrayList<>();
		LocalDate ld = LocalDate.now();
		int iyear = ld.getYear();
		for(int i=iyear; i<iyear+5; i++) {
			yearList.add(i);
		}
		model.addAttribute("yearList", yearList);
		
		List<Integer> monthList = new ArrayList<>();
		for(int i=1; i<=12; i++) {
			monthList.add(i);
		}
		model.addAttribute("monthList", monthList);
		
		List<Integer> dayList = new ArrayList<>();
		for(int i=1; i<=31; i++) {
			dayList.add(i);
		}
		model.addAttribute("dayList", dayList);
		
		List<Integer> stHourList = new ArrayList<>();
		for(int i=10; i<=18; i++) {
			stHourList.add(i);
		}
		model.addAttribute("stHourList", stHourList);
		
		List<String> stMinuteList = new ArrayList<>();
		for(int i=0; i<=3; i+=3) {
			stMinuteList.add(String.valueOf(i).concat("0"));
		}
		model.addAttribute("stMinuteList", stMinuteList);
		
		List<Integer> etHourList = new ArrayList<>();
		for(int i=10; i<=18; i++) {
			etHourList.add(i);
		}
		model.addAttribute("etHourList", etHourList);
		
		List<String> etMinuteList = new ArrayList<>();
		for(int i=0; i<=3; i+=3) {
			etMinuteList.add(String.valueOf(i).concat("0"));
		}
		model.addAttribute("etMinuteList", etMinuteList);
	}
	
	//	ControllerAdviceから呼びだされた時の処理
	public void exceptionHandlerReturn(Model model, RegisterEntity registerEntity) {		
		registerEntity.setCourse_no(this.course_no);
		registerEntity.setCapacity(null);
		model.addAttribute("registerEntity", registerEntity);
		errorCheck.courseUpdateErrorCheck(registerEntity, model);
		createHoldingTime(model);
	}
}

//	デバッグツール置き場
//System.out.println("course_no: " + registerEntity.getCourse_no());
//System.out.println("course_name: " + registerEntity.getCourse_name());
//System.out.println("the_date: " + registerEntity.getThe_date());
//System.out.println("start_time: " + registerEntity.getStart_time());
//System.out.println("end_time: " + registerEntity.getEnd_time());
//System.out.println("capacity: " + registerEntity.getCapacity());
//System.out.println("year: " + registerEntity.getIyear());
//System.out.println("month: " + registerEntity.getImonth());
//System.out.println("day: " + registerEntity.getIday());
//System.out.println("shour: " + registerEntity.getShour());
//System.out.println("sminute: " + registerEntity.getSminute());
//System.out.println("ehour: " + registerEntity.getEhour());
//System.out.println("eminute: " + registerEntity.getEminute());

//System.out.println("course_no: " + this.registerEntity.getCourse_no());
//System.out.println("course_name: " + this.registerEntity.getCourse_name());
//System.out.println("the_date: " + this.registerEntity.getThe_date());
//System.out.println("start_time: " + this.registerEntity.getStart_time());
//System.out.println("end_time: " + this.registerEntity.getEnd_time());
//System.out.println("capacity: " + this.registerEntity.getCapacity());
//System.out.println("year: " + this.registerEntity.getIyear());
//System.out.println("month: " + this.registerEntity.getImonth());
//System.out.println("day: " + this.registerEntity.getIday());
//System.out.println("shour: " + this.registerEntity.getShour());
//System.out.println("sminute: " + this.registerEntity.getSminute());
//System.out.println("ehour: " + this.registerEntity.getEhour());
//System.out.println("eminute: " + this.registerEntity.getEminute());