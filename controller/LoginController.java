package com.example.demo.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.demo.entity.ApplicationEntity;
import com.example.demo.entity.CourseApplyEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.AttendanceMapper;
import com.example.demo.service.ApplicationService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes(types=UserEntity.class)
@Transactional
public class LoginController {
	@Autowired private ApplicationService service;
	@Autowired private AttendanceMapper mapper;
	@Autowired private ApplyController controller;
	
	//	URL直打ち対策フラグ（初期値&未ログインfalse、ログイン済みtrue） 作成途中
	private static boolean loginFlag = false;
	
	@ModelAttribute
	public UserEntity setUpForm() {
		return new UserEntity(null, null, null, null, null);
	}
	
	@GetMapping("/login")
	public String login(@ModelAttribute UserEntity userEntity, HttpSession session, SessionStatus status) {
		session.removeAttribute("userEntity");
		session.removeAttribute("registerEntity");
		session.invalidate();
		status.setComplete();
		loginFlag = false;
		return "/Login/login";
	}
	
	@GetMapping("/judgement")
	public String TransitionJudgement(@ModelAttribute UserEntity userEntity, ApplicationEntity applicationEntity,
			Model model) {
		List<String> userIdList = new ArrayList<>();
		Integer authority = 9999;
		userIdList = mapper.userIdListSelect();
		
		List<String> passWdList = new ArrayList<>();
		passWdList = mapper.passWdListSelect();
		
		int i;
		String userId = "";
		String passWd = "";
		
		for(i=0; i<userIdList.size(); i++) {
			userId = userIdList.get(i);
			if(userId.equals(userEntity.getUserId())) {
				for(int j=0; j<passWdList.size(); j++) {
					passWd = passWdList.get(j);
					if(passWd.equals(userEntity.getPassWd())) {
						authority = mapper.authorityListSelect(userEntity.getUserId());
						loginFlag = true;
						break;
					}
				}
			}
			if(authority != 9999) {
				break;
			}	
			
			if(i == userIdList.size()-1) {
				model.addAttribute("message", "※ アカウントが存在しません。");
				return "/Login/login";
			}
		}
		
		int j;
		for(j=0; j<passWdList.size(); j++) {
			if(userEntity.getPassWd().equals(passWdList.get(j))) {
				break;
			}
		}
		
		if(authority == 0) {
			RegisterController.userId = userEntity.getUserId();
			model.addAttribute("userId", userId); 
			return "/Register/RegisterTop";
		}
		else{
			controller.databaseAccess(model);
			model.addAttribute("applicationEntity", applicationEntity);
			return "/Apply/apply";
		}
	}
		
	@GetMapping("/userRegister")
	public String userRegister(@ModelAttribute UserEntity userEntity, Model model, @RequestParam String name) {
		if(!name.equals("ユーザー登録")) {
			model.addAttribute("userEntity", userEntity);
		}
		else {
			userEntity.setUserId("");
			userEntity.setPassWd("");
		}
		return "/Login/userRegister";
	}
	
	@PostMapping("/userConfirm")
	public String userConfirm(@ModelAttribute @Validated UserEntity userEntity, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("userEntity", userEntity);
			return "/Login/userRegister";
		}
		List<String> message = new ArrayList<>();
		message = service.RegisterDataJudgement(userEntity);
		
		if(!message.isEmpty()) {
			model.addAttribute("message", message);
			return "Login/userRegister";
		}
		return "Login/userConfirm";
	}
	
	@GetMapping("/userCompletion")
	public String userCompletion(@ModelAttribute UserEntity userEntity, Model model, HttpSession session, 
			SessionStatus status) {
		if(userEntity.getUserId() != null && userEntity.getPassWd() != null) {
			service.userDbOpration(userEntity);
			session.removeAttribute("userEntity");
			session.invalidate();
			status.setComplete();
		}
		return "Login/userCompletion";
	}
}

//System.out.println("userId: " + userEntity.getUserId());
//System.out.println("passWd: " + userEntity.getPassWd());