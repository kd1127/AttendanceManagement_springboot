package com.example.demo.error;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.example.demo.entity.RegisterEntity;

@Component
public class CourseUpdateErrorCheck{
	public int courseUpdateErrorCheck(RegisterEntity registerEntity, Model model) {
		//	戻り値（エラーあり：１、エラーなし：-1）
		int judge = 0;
		//	エラーあり：true エラーなし:false
		boolean judge_flag = false;
		
		//	講座名入力チェック
		String courseNameError = "";
		if(registerEntity.getCourse_name() == null || registerEntity.getCourse_name().equals("")) {
			courseNameError = "「講座名」は必須項目です";
			model.addAttribute("courseNameError", courseNameError);
			judge_flag = true;
		}
		
		String message = "";
		if(registerEntity.getIyear() == null) {
			message = "「講座開催日(年";
			judge_flag = true;
			if(registerEntity.getImonth() == null) {
				message = message.concat("月");
				if(registerEntity.getIday() == null) {
					message = message.concat("日)」は必須項目です");
				}
				else {
					message = message.concat(")」は必須項目です");
				}
			}
			else {
				if(registerEntity.getIday() == null) {
					message = message.concat("日)」は必須項目です");
					judge_flag = true;
				}
			}
		}
		else {
			if(registerEntity.getImonth() == null) {
				message = "「講座開催日(月";
				judge_flag = true;
				if(registerEntity.getIday() == null) {
					message = message.concat("日)」は必須項目です");
				}
				else {
					message = message.concat(")」は必須項目です");
				}
			}
			else {
				if(registerEntity.getIday() == null) {
					message = "「講座開催日(日)」は必須項目です";
					judge_flag = true;
				}
			}
		}
		model.addAttribute("message", message);
		
		String startTimeError = "";
		if(registerEntity.getShour() == null) {
			startTimeError = "開始時刻(時";
			judge_flag = true;
			if(registerEntity.getSminute() == null || registerEntity.getSminute().equals("")) {
				startTimeError = startTimeError.concat("分)は必須項目です");
			}
			else {
				startTimeError = startTimeError.concat(")は必須項目です");
			}
		}
		else {
			if(registerEntity.getSminute() == null || registerEntity.getSminute().equals("")) {
				startTimeError = startTimeError.concat("開始時刻(分)は必須項目です");
				judge_flag = true;
			}
		}
		model.addAttribute("startTimeError", startTimeError);
		
		String endTimeError = "";
		if(registerEntity.getEhour() == null) {
			endTimeError = "終了時刻(時";
			judge_flag = true;
			if(registerEntity.getEminute() == null || registerEntity.getEminute().equals("")) {
				endTimeError = endTimeError.concat("分)は必須項目です");
			}
			else {
				endTimeError = endTimeError.concat(")は必須項目です");
			}
		}
		else {
			if(registerEntity.getSminute() == null || registerEntity.getEminute().equals("")) {
				endTimeError = endTimeError.concat("終了時刻(分)は必須項目です");
				judge_flag = true;
			}
		}
		model.addAttribute("endTimeError", endTimeError);
		
		//	エラーメッセージ格納変数
		String contradiction = "";
		int start_time = 0;
		int end_time = 0;
		try {
			//	開始時刻
			Integer tmp_shour = registerEntity.getShour();
			String tmp_startTime = String.valueOf(tmp_shour).concat(registerEntity.getSminute());
			start_time = Integer.parseInt(tmp_startTime);
			//	終了時刻
			Integer tmp_ehour = registerEntity.getEhour();
			String tmp_endTime = String.valueOf(tmp_ehour).concat(registerEntity.getEminute());
			end_time = Integer.parseInt(tmp_endTime);
			
			if(start_time > end_time || start_time == end_time) {
				contradiction = "「終了時刻」は「開始時刻」よりも後の時刻を入力してください";
				judge_flag = true;
			}
			model.addAttribute("contradiction", contradiction);
		} catch (NumberFormatException | NullPointerException e) {	}
				
		String capacityError = "";
		int capacity = 0;
		if(registerEntity.getCapacity() == null) {
			capacityError = "「定員」は必須項目です";
			judge_flag = true;
		}
		else {
			capacity = registerEntity.getCapacity();
			
			if(capacity == -999999999) {
				capacityError = "「定員」は数字で入力してください";
				judge_flag = true;
			}
			else if(capacity < 1 || capacity > 50) {
				capacityError = "「定員」は1以上、50以下で入力してください";
				judge_flag = true;
			}
		}
		model.addAttribute("capacityError", capacityError);
		
		if(judge_flag == true) {	//	エラーあり
			judge = 1;
			return judge;
		}
		else {						//	エラーなし
			judge = -1;
			return judge;
		}
	}
}
