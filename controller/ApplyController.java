package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.demo.entity.ApplicationEntity;
import com.example.demo.error.ApplyErrorCheck;
import com.example.demo.mapper.AttendanceMapper;
import com.example.demo.service.ApplicationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes(types = ApplicationEntity.class)
@Transactional
public class ApplyController {
	@Autowired private AttendanceMapper mapper;
	@Autowired private ApplicationService service;
	@Autowired private ApplyErrorCheck errorCheck;
	
	@ModelAttribute
	ApplicationEntity setUpForm() {
		return new ApplicationEntity(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	@GetMapping("apply")
	public String apply(@ModelAttribute ApplicationEntity applicationEntity, Model model) {
		databaseAccess(model);
		model.addAttribute("ApplicationEntity", applicationEntity);
		return "Apply/apply";
	}
	
	@PostMapping("/aConfirm")
	public String applyConfirm(@Validated @ModelAttribute ApplicationEntity applicationEntity, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			//	エラーメッセージ格納
			List<String> errorMsg = new ArrayList<>();
			databaseAccess(model);
			errorMsg = errorCheck.errorCheck(applicationEntity);
			model.addAttribute("errorMsg", errorMsg);
			databaseAccess(model);
			return "Apply/apply";
		}
		model.addAttribute("applicationEntity", applicationEntity);
		return "Apply/confirm";
	}
	
	@GetMapping("/aCompletion")
	public String applyCompletion(@ModelAttribute ApplicationEntity apEntity, HttpServletRequest request, HttpSession session, SessionStatus sessionStatus) {
		if(apEntity.getName() != null) {
			service.insertData(apEntity);
			service.updateCapacity(apEntity);
		}
		//	セッション破棄
		session.removeAttribute("apEntity");
		session.invalidate();
		sessionStatus.setComplete();
		return "Apply/completion";
	}
	
	//	入力フォームで使用する日付のリストを作成（呼び出し元はapplyConfirmと）
	public void dateList(Model model){
		//	年を取得
		LocalDate local = LocalDate.now();
		int now = local.getYear();
		//	年を格納するlist
		List<Integer> ylist = new ArrayList<>();
		
		for(int i=now-18; i>=now-60; i--) {
			ylist.add(i);
		}
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
	}
	
	public void databaseAccess(Model model){
		//	講座名取得
		List<String> courseList = new ArrayList<>();
		courseList = service.courseList();
		//	定員取得
		List<Integer> capacityList = new ArrayList<>();
		capacityList = mapper.selectCapacity();
		//	開催日時取得
		List<String> dateAndTime = new ArrayList<String>();
		dateAndTime = service.dateAndTimeList();
		model.addAttribute("dateAndTime", dateAndTime);
		model.addAttribute("courseList", courseList);
		model.addAttribute("capacityList", capacityList);		
		
		//	入力フォームのプルダウンメニューの値を生成
		dateList(model);
	}
}
