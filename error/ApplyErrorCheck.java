package com.example.demo.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.entity.ApplicationEntity;

@Component
public class ApplyErrorCheck implements AttendanceErrorCheck{
	@Override
	public List<String> errorCheck(ApplicationEntity apEntity) {
		List<String> errorMsg = new ArrayList<String>();
		System.out.println("test");
		if(apEntity.getName() == null || "".equals(apEntity.getName())) {
			errorMsg.add("「お名前」は必須項目です。");
		}
		
		if(apEntity.getFurigana() == null || "".equals(apEntity.getFurigana())) {
			errorMsg.add("「フリガナ」は必須項目です。");
		}
		
		if(apEntity.getMail() == null || "".equals(apEntity.getMail())) {
			errorMsg.add("「Eメールアドレス」は必須項目です。");
		}
		else {
			if(apEntity.getMail().indexOf("@") == -1) {
				errorMsg.add("「Eメールアドレス」の形式が不正です。");
			}
		}
		
		if(apEntity.getYear() == null || apEntity.getMonth() == null || apEntity.getDay() == null){			
			errorMsg.add("「生年月日」は必須項目です。");
		}
		
		//	電話番号のエラーチェック		
		try {
			long long_tel = Long.parseLong(apEntity.getTel());
		}
		catch(NumberFormatException e) {
			errorMsg.add("「電話番号」は数字で入力してください。");
		}
		
		//	希望講座のチェックボックス入力チェック
		if(apEntity.getCheck1() == false && apEntity.getCheck2() == false && apEntity.getCheck3() == false
				 && apEntity.getCheck4() == false && apEntity.getCheck5() == false) {
			errorMsg.add("「希望講座」は必須項目です。");
		}
		
		return errorMsg;
	}
}
