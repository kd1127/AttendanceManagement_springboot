package com.example.demo.error;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.example.demo.db.DataBaseOperation;
import com.example.demo.entity.RegisterEntity;

@Component
public class ErrorCheck {
	public List<String> errorCheck(RegisterEntity reEntity) throws SQLException{
		//	コントローラークラスに返却するときに使用するList
		List<String> errorMsg = new ArrayList<>();
		//	DB操作クラスのインスタンス生成
		DataBaseOperation dbo = new DataBaseOperation();
		//	DB操作クラスから取得する値を格納
		List<String> courseNoList = new ArrayList<>();		
		//	開催日時、開始時刻、終了時刻のエラーメッセージを格納
		String message = new String();
		
		//	講座番号が空白かどうかのチェック
		if(reEntity.getCourse_no().equals("") || reEntity.getCourse_no() == null) {
			errorMsg.add("「講座番号」は必須項目です。");
		}
		
		//	重複している講座番号のチェック
		try {
			courseNoList = dbo.DuplicateCourseNum();
			
			for(String courseNo : courseNoList) {
				if(reEntity.getCourse_no().equals(courseNo)) {
					errorMsg.add("「講座番号」が重複しています。");
					break;
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("データ取得できませんでした。。。");
		}
		
		//	講座名のチェック
		if(reEntity.getCourse_name().equals("") || reEntity.getCourse_name() == null) {
			errorMsg.add("「講座名」は必須項目です。");
		}
		
		//	講座開催日のチェック（年月日）
		message = "講座開催日";
		if(reEntity.getIyear() == null || reEntity.getImonth() == null || reEntity.getIday() == null) {
			if(reEntity.getIyear() == null) {
				message = message.concat("「年");
				
				if(reEntity.getImonth() == null) {
					message = message.concat("月");
					
					if(reEntity.getIday() == null) {
						message = message.concat("日」");
					}
					else {
						message = message.concat("」");
					}
				}
				else {
					if(reEntity.getIday() == null) {
						message = message.concat("日」");
					}
					else {
						message = message.concat("」");
					}
				}
			}
			else if(reEntity.getImonth() == null) {
				message = message.concat("「月」");
				
				if(reEntity.getIday() == null) {
					message = message.concat("日」");
				}
				else {
					message = message.concat("」");
				}
			}
			else {
				message = message.concat("「日」");
			}
			message = message.concat("は必須項目です。");
			errorMsg.add(message);
		}
		
		//	開始時刻のチェック
		message = "開始時刻";
		if(reEntity.getShour() == null || reEntity.getSminute().equals("")) {
			if(reEntity.getShour() == null) {
				message = message.concat("「時");
				
				if(reEntity.getSminute().equals("")) {
					message = message.concat("分」");
				}
				else {
					message = message.concat("」");
				}
			}
			else {
				if(reEntity.getSminute().equals("")) {
					message = message.concat("「分」");
				}
			}
			message = message.concat("は必須項目です。");
			errorMsg.add(message);
		}
		
		//	終了時刻のチェック
		message = "終了時刻";
		if(reEntity.getEhour() == null || reEntity.getEminute().equals("")) {
			if(reEntity.getEhour() == null) {
				message = message.concat("「時");
				
				if(reEntity.getEminute().equals("")) {
					message = message.concat("分」");
				}
				else {
					message = message.concat("」");
				}
			}
			else {
				if(reEntity.getEminute().equals("")) {
					message = message.concat("「分」");
				}
			}
			message = message.concat("は必須項目です。");
			errorMsg.add(message);
		}
		
		//	開始時刻が終了時刻よりも後の時間に設定されているかのチェック
		if(reEntity.getShour() != null && !reEntity.getSminute().equals("") && reEntity.getEhour() != null && !reEntity.getEminute().equals("")) {
			
			int start_time = 0;
			int end_time = 0;
			String shour = String.valueOf(reEntity.getShour()).concat(reEntity.getSminute());
			String ehour = String.valueOf(reEntity.getEhour()).concat(reEntity.getEminute());
			start_time = Integer.parseInt(shour);
			end_time = Integer.parseInt(ehour);
			
			if(start_time >= end_time) {
				errorMsg.add("「終了時刻」は「開始時刻」よりも後の時刻を入力してください。");
			}
		}
		
		//	定員のチェック
		int capacity = 0;
		if(reEntity.getCapacity() == null) {
			errorMsg.add("「定員」は必須項目です。");
		}
		else {
			capacity = reEntity.getCapacity();
			
			if(capacity == -999999999) {
				errorMsg.add("「定員」は数字で入力してください。");
			}
			else if(capacity < 1 || capacity > 50) {
				errorMsg.add("「定員」は1以上、50以下で入力してください。");
			}	
		}
		
		return errorMsg;
	}
	
	//	講座検索画面でのエラーチェック（開始時刻と終了時刻のみ）
	public boolean courseSearchErrorCheck(RegisterEntity registerEntity, Model model) {
		//	エラーがあればtrue、なければfalse
		boolean flag = false;
		
		//	開始時刻の検証、両方とも空だとエラー
		String startTimeError = "";
		if(registerEntity.getShour() != null && registerEntity.getSminute().equals("")) {
			startTimeError = "「開始時刻」は「時」「分」どちらも入力してください";
			flag = true;
		}
		model.addAttribute("startTimeError", startTimeError);
		
		//	終了時刻の検証、両方とも空だとエラー
		String endTimeError = "";
		if(registerEntity.getEhour() != null && registerEntity.getEminute().equals("")) {
			endTimeError = "「終了時刻」は「時」「分」どちらも入力してください";
			flag = true;
		}
		model.addAttribute("endTimeError", endTimeError);
		
		return flag;
	}
}
