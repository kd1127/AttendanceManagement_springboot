package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.exceptions.TemplateInputException;

import com.example.demo.entity.RegisterEntity;

@ControllerAdvice
public class AdminControllerAdvice {
	@Autowired private AdminController controller;
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String methodArgumentNotValidException(Model model, BindingResult result) {
		RegisterEntity registerEntity = new RegisterEntity();
		
		if(result.hasErrors()) {
			//	BindingResultのエラー情報からフォームで入力された値を取得してRegisterEntityに設定する
			//	講座名
			Object obj = result.getFieldValue("course_name");
			String course_name = String.valueOf(obj);
			registerEntity.setCourse_name(course_name);
			//	講座開催日 年
			obj = result.getFieldValue("iyear");
			String year = String.valueOf(obj);
			
			if(!year.equals("null") && !year.equals("")) {
				int iyear = Integer.parseInt(year);
				registerEntity.setIyear(iyear);
			}
			//	講座開催日 月
			obj = result.getFieldValue("imonth");
			String month = String.valueOf(obj);
			
			if(!month.equals("null") && !month.equals("")) {
				int imonth = Integer.parseInt(month);
				registerEntity.setImonth(imonth);
			}
			//	講座開催日 日
			obj = result.getFieldValue("iday");
			String day = String.valueOf(obj);
			
			if(!day.equals("null") && !day.equals("")) {
				int iday = Integer.parseInt(day);
				registerEntity.setIday(iday);
			}
			//	開始時刻 時
			obj = result.getFieldValue("shour");
			String hour = String.valueOf(obj);
			
			if(!hour.equals("null") && !hour.equals("")) {
				int shour = Integer.parseInt(hour);
				registerEntity.setShour(shour);
			}
			//	開始時刻 分
			obj = result.getFieldValue("sminute");
			String sminute = String.valueOf(obj);
			registerEntity.setSminute(sminute);
			//	終了時刻 時
			obj = result.getFieldValue("ehour");
			hour = String.valueOf(obj);
			
			if(!hour.equals("null") && !hour.equals("")) {
				int ehour = Integer.parseInt(hour);
				registerEntity.setEhour(ehour);
			}
			//	終了時刻 分
			obj = result.getFieldValue("eminute");
			String eminute = String.valueOf(obj);
			registerEntity.setEminute(eminute);
			//	定員
			obj = result.getFieldValue("capacity");
			String capa = String.valueOf(obj);
			
			int capacity = 0;
			if(!capa.equals("null") && !capa.equals("")) {
				try {
					capacity = Integer.parseInt(capa);
					registerEntity.setCapacity(capacity);
				} catch (NumberFormatException e) {
					capacity = -999999999;
					registerEntity.setCapacity(capacity);
				}
			}
			
			controller.exceptionHandlerReturn(model, registerEntity);
		}
		if(RegisterController.pageFlag == true) {
			model.addAttribute("userId", RegisterController.userId);
			RegisterController.pageFlag = false;
			return "Register/RegisterTop";
		}		
		return "Admin/courseUpdate";
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public String IllegalStateException(Model model, BindingResult result) {
		RegisterEntity registerEntity = new RegisterEntity();
		
		if(result.hasErrors()) {
			//	BindingResultのエラー情報からフォームで入力された値を取得してRegisterEntityに設定する
			//	講座名
			Object obj = result.getFieldValue("course_name");
			String course_name = String.valueOf(obj);
			registerEntity.setCourse_name(course_name);
			//	講座開催日 年
			obj = result.getFieldValue("iyear");
			String year = String.valueOf(obj);
			
			if(!year.equals("null") && !year.equals("")) {
				int iyear = Integer.parseInt(year);
				registerEntity.setIyear(iyear);
			}
			//	講座開催日 月
			obj = result.getFieldValue("imonth");
			String month = String.valueOf(obj);
			
			if(!month.equals("null") && !month.equals("")) {
				int imonth = Integer.parseInt(month);
				registerEntity.setImonth(imonth);
			}
			//	講座開催日 日
			obj = result.getFieldValue("iday");
			String day = String.valueOf(obj);
			
			if(!day.equals("null") && !day.equals("")) {
				int iday = Integer.parseInt(day);
				registerEntity.setIday(iday);
			}
			//	開始時刻 時
			obj = result.getFieldValue("shour");
			String hour = String.valueOf(obj);
			
			if(!hour.equals("null") && !hour.equals("")) {
				int shour = Integer.parseInt(hour);
				registerEntity.setShour(shour);
			}
			//	開始時刻 分
			obj = result.getFieldValue("sminute");
			String sminute = String.valueOf(obj);
			registerEntity.setSminute(sminute);
			//	終了時刻 時
			obj = result.getFieldValue("ehour");
			hour = String.valueOf(obj);
			
			if(!hour.equals("null") && !hour.equals("")) {
				int ehour = Integer.parseInt(hour);
				registerEntity.setEhour(ehour);
			}
			//	終了時刻 分
			obj = result.getFieldValue("eminute");
			String eminute = String.valueOf(obj);
			registerEntity.setEminute(eminute);
			//	定員
			obj = result.getFieldValue("capacity");
			String capa = String.valueOf(obj);
			
			int capacity = 0;
			if(!capa.equals("null") && !capa.equals("")) {
				try {
					capacity = Integer.parseInt(capa);
					registerEntity.setCapacity(capacity);
				} catch (NumberFormatException e) {
					capacity = -999999999;
					registerEntity.setCapacity(capacity);
				}
			}
			
			controller.exceptionHandlerReturn(model, registerEntity);
		}
		return "Admin/courseUpdate";
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public String httpRequestMethodNotSupportedException() {
		return "error/500";
	}
	
	@ExceptionHandler(TemplateInputException.class)
	public String templateInputException() {
		return "error/500";
	}
	
	@ExceptionHandler(NullPointerException.class)
	public String NullPointerException() {
		return "error/500";
	}
}
