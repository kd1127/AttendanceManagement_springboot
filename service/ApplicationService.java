package com.example.demo.service;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.ApplicationEntity;
import com.example.demo.mapper.AttendanceMapper;

/*
 * 業務ロジック実装クラス
 */

@Service
@Transactional
public class ApplicationService {
	@Autowired private AttendanceMapper mapper;
	
	public void insertData(ApplicationEntity apEntity) {
		//	DB登録するため生年月日を-含めた形式にする
		LocalDate ldate2 = LocalDate.of(apEntity.getYear(), apEntity.getMonth(), apEntity.getDay());
		Date date = Date.valueOf(ldate2);
		apEntity.setBirthday(date);
		
		//	inp_dateに入れる値を作成
		LocalDate local = LocalDate.now();
		Date ldate = Date.valueOf(local);
		apEntity.setInp_date(ldate);
		
		try {
			mapper.insertApplication(apEntity);
			System.out.println("登録完了しました。");
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("登録失敗しました。。。");
		}
	}
	
	//	コース名取得メソッド
	public List<String> courseList(){
		List<String> courseList = new ArrayList<>();
		courseList = mapper.selectCourseList();
		return courseList;
	}
	
	//	表に出力するため開催日時・開始時刻・終了時刻を取得して変数dateAndTimeListに入れる
	public List<String> dateAndTimeList(){
		List<String> dateAndTime = new ArrayList<>();
		
		List<String> theDateList = new ArrayList<>();
		theDateList = mapper.selectTheDate();
		
		List<String> startTimeList = new ArrayList<>();
		startTimeList = mapper.selectStartTime();
		
		List<String> endTimeList = new ArrayList<>();
		endTimeList = mapper.selectEndTime();
		
		LocalDate ld2 = LocalDate.of(1987, 11, 27);
		DayOfWeek d = ld2.getDayOfWeek();
		System.out.println("d:" + d);
		
		for(int i=0; i<theDateList.size(); i++) {
			//	開催日時・曜日・開始時刻・終了時刻を結合した値を格納
			String dateTime = "";
			//	曜日を格納
			DayOfWeek dow;
			String sweek = "";
			//	年月日を格納
			LocalDate week;
			//	年月日を格納
			int year = 0, month = 0, day = 0;
			//	DBから取得した値の年月日を抽出する
			year = Integer.parseInt(theDateList.get(i).substring(0, 4));
			month = Integer.parseInt(theDateList.get(i).substring(5, 7));
			day = Integer.parseInt(theDateList.get(i).substring(8, 10));
			//	変数weekにyear, month, dayを格納
			week = LocalDate.of(year, month, day);
			dow = week.getDayOfWeek();
			//	dowにはこの時点ではアルファベットの曜日が入っているからswitch文で日本語に変換
			switch(dow) {
			case SUNDAY:
				sweek = "（日）";
				break;
			case MONDAY:
				sweek = "（月）";
				break;
			case TUESDAY:
				sweek = "（火）";
				break;
			case WEDNESDAY:
				sweek = "（水）";
				break;
			case THURSDAY:
				sweek = "（木）";
				break;
			case FRIDAY:
				sweek = "（金）";
				break;
			case SATURDAY:
				sweek = "（土）";
			}
			//	dateTimeに年月日・曜日・開始時刻・終了時刻を格納
			dateTime = theDateList.get(i).substring(0, 4);
			dateTime = dateTime.concat("年");
			dateTime = dateTime.concat(theDateList.get(i).substring(5, 7));
			dateTime = dateTime.concat("月");
			dateTime = dateTime.concat(theDateList.get(i).substring(8, 10));
			dateTime = dateTime.concat("日");
			dateTime = dateTime.concat(sweek);
			dateTime = dateTime.concat(startTimeList.get(i));
			dateTime = dateTime.concat("-");
			dateTime = dateTime.concat(endTimeList.get(i));
			dateAndTime.add(dateTime);
		}
		
		return dateAndTime;
	}
}
