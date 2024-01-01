package com.example.demo.service;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.ApplicationEntity;
import com.example.demo.entity.CourseApplyEntity;
import com.example.demo.entity.RegisterEntity;
import com.example.demo.entity.RegisterInsertEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.AttendanceMapper;

/*
 * 業務ロジック実装クラス
 */

@Service
@Transactional
public class ApplicationService {
	@Autowired private AttendanceMapper mapper;
	private CourseApplyEntity caEntity;
	
	/*	
	 * @param apEntity 申し込み画面で入力した値が入っている
	 */
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
			insertCourseApplyPreparation(apEntity);
			System.out.println("登録完了しました。");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("登録できませんでした。。。");
		}
	}
	
	//	コース名取得メソッド
	public List<String> courseList(){
		List<String> courseList = new ArrayList<>();
		courseList = mapper.selectCourseList();
		return courseList;
	}
	
	//	コース番号取得
	public List<String> courseNoList(){
		List<String> courseNoList = new ArrayList<>();
		courseNoList = mapper.selectCourseNo();
		return courseNoList;
	}
	
	//	講座申し込み完了後、定員を１減らすメソッド
	public void updateCapacity(ApplicationEntity apEntity) {
		List<String> courseNoList = new ArrayList<>();
		courseNoList = mapper.selectCourseNo();
		
		if(apEntity.getCheck1() == true) {
			mapper.updateCapacity(courseNoList.get(0));
		}
		if(apEntity.getCheck2() == true) {
			mapper.updateCapacity(courseNoList.get(1));
		}
		if(apEntity.getCheck3() == true) {
			mapper.updateCapacity(courseNoList.get(2));
		}
		if(apEntity.getCheck4() == true) {
			mapper.updateCapacity(courseNoList.get(3));
		}
		if(apEntity.getCheck5() == true) {
			mapper.updateCapacity(courseNoList.get(4));
		}
	}
	
	//	course_applyテーブルに登録するための準備メソッド
	public void insertCourseApplyPreparation(ApplicationEntity apEntity) {
		//	caEntity初期化処理
		caEntity = new CourseApplyEntity(null, null, null);
		//	講座番号取得
		List<String> courseNoList = new ArrayList<>();
		courseNoList = mapper.selectCourseNo();
		//	application.id取得
		int app_id = mapper.selectApplicationId();
//		inp_dateに入れる値を作成
		LocalDate local = LocalDate.now();
		Date ldate = Date.valueOf(local);
		caEntity.setInp_date(ldate);
		
		if(apEntity.getCheck1() == true) {
			caEntity.setId_application(app_id);
			caEntity.setCourse_no(courseNoList.get(0));
			mapper.insertCourseApply(caEntity);
		}
		if(apEntity.getCheck2() == true) {
			caEntity.setId_application(app_id);
			caEntity.setCourse_no(courseNoList.get(1));
			mapper.insertCourseApply(caEntity);
		}
		if(apEntity.getCheck3() == true) {
			caEntity.setId_application(app_id);
			caEntity.setCourse_no(courseNoList.get(2));
			mapper.insertCourseApply(caEntity);
		}
		if(apEntity.getCheck4() == true) {
			caEntity.setId_application(app_id);
			caEntity.setCourse_no(courseNoList.get(3));
			mapper.insertCourseApply(caEntity);
		}
		if(apEntity.getCheck5() == true) {
			caEntity.setId_application(app_id);
			caEntity.setCourse_no(courseNoList.get(4));
			mapper.insertCourseApply(caEntity);
		}
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
	
	//	一般ユーザーをDBに登録
	public void userDbOpration(UserEntity userEntity) {
		userEntity.setAuthority(1);
		LocalDate local = LocalDate.now();
		Date ldate = Date.valueOf(local);
		userEntity.setInp_date(ldate);
		
		List<String> idList = new ArrayList<>();
		idList = mapper.idListSelect();
		if(!idList.contains(userEntity.getUserId())) {
			mapper.insertAllUser(userEntity.getUserId(), userEntity.getPassWd(), userEntity.getAuthority(), ldate);
		}
	}
	
	//	ユーザー登録画面で入力されたユーザIDと、パスワードが正しいかどうかを判定する処理
	public List<String> RegisterDataJudgement(UserEntity userEntity){
		List<String> message = new ArrayList<>();
		List<String> userIdList = new ArrayList<>();
		userIdList = mapper.userIdListSelect();
		
		if(!userEntity.getPassWd().equals(userEntity.getRe_passWd())) {
			message.add("「パスワード」と「パスワード（確認用）」が一致していません");
		}
		
		for(int i=0; i<userIdList.size(); i++) {
			if(userEntity.getUserId().equals(userIdList.get(i))) {
				message.add("「ユーザID」が重複しています");
				break;
			}
		}
		
		return message;
	}
	
	//	講座検索機能にて、講座一覧画面の表に出力する開催日時のデータを取得する
	public List<String> dateAndTimeSearch(RegisterEntity registerEntity){
		List<RegisterEntity> rEntityList = new ArrayList<>(); 
		//	表に出力する形式に変換した後、格納するリスト
		List<String> dateAndTimeList = new ArrayList<>();
//		rEntityList = mapper.dateAndTimeFindAll();
		rEntityList = mapper.dateAndTimeSearch(registerEntity);
		
		//	開催日時を表に出力するため、形式を整える。
		for(int i=0; i<rEntityList.size(); i++) {
			String dateAndTime = "";
			String year = String.valueOf(rEntityList.get(i).getThe_date()).substring(0, 4);
			String month = String.valueOf(rEntityList.get(i).getThe_date()).substring(5, 7);
			String day = String.valueOf(rEntityList.get(i).getThe_date()).substring(8, 10);
			
			dateAndTime = dateAndTime.concat(year).concat("年");
			dateAndTime = dateAndTime.concat(month).concat("月");
			dateAndTime = dateAndTime.concat(day).concat("日");
			
			//	曜日部分の実装
			LocalDate ldate;
			DayOfWeek week;
			int iyear = Integer.parseInt(year);
			int imonth = Integer.parseInt(month);
			int iday = Integer.parseInt(day);
			ldate= LocalDate.of(iyear, imonth, iday);
			week = ldate.getDayOfWeek();
			
			switch(week) {
			case SUNDAY:
				dateAndTime = dateAndTime.concat("（日）");
				break;
			case MONDAY:
				dateAndTime = dateAndTime.concat("（月）");
				break;
			case TUESDAY:
				dateAndTime = dateAndTime.concat("（火）");
				break;
			case WEDNESDAY:
				dateAndTime = dateAndTime.concat("（水）");
				break;
			case THURSDAY:
				dateAndTime = dateAndTime.concat("（木）");
				break;
			case FRIDAY:
				dateAndTime = dateAndTime.concat("（金）");
				break;
			case SATURDAY:
				dateAndTime = dateAndTime.concat("（土）");
				break;
			}
			
			dateAndTime = dateAndTime.concat(rEntityList.get(i).getStart_time()).concat("-");
			dateAndTime = dateAndTime.concat(rEntityList.get(i).getEnd_time());
			dateAndTimeList.add(dateAndTime);	
		}		
		return dateAndTimeList;
	}
	
	//	状態（終了・開催中・開催予定を開催日・開始時刻から求めてListに格納する
	public List<String> situationDemand(RegisterEntity registerEntity){
		List<RegisterEntity> rEntityList = new ArrayList<>();
//		rEntityList = mapper.dateAndTimeFindAll();
		rEntityList = mapper.dateAndTimeSearch(registerEntity);
		//	開催中・開催予定・終了のいずれかの状態を格納
		List<String> situationList = new ArrayList<>();
		
		//	現在時刻の取得
		LocalDateTime ldate = LocalDateTime.now();
		int ldateYear = ldate.getYear();
		int ldateMonth = ldate.getMonthValue();
		int ldateDay = ldate.getDayOfMonth();
		int ldateShour = ldate.getHour();
		int ldateSminute = ldate.getMinute();
		
		String sldate = String.valueOf(ldateYear);
		
		if(ldateMonth >= 1 && ldateMonth <= 9) {
			sldate = sldate.concat(String.valueOf("0" + ldateMonth));
		}
		else {
			sldate = sldate.concat(String.valueOf(ldateMonth));
		}
		
		if(ldateDay >= 1 && ldateDay <= 9) {
			sldate = sldate.concat(String.valueOf("0" + ldateDay));
		}
		else {
			sldate = sldate.concat(String.valueOf(ldateDay));
		}
		
		if(ldateShour >= 1 && ldateShour <= 9) {
			sldate = sldate.concat(String.valueOf("0" + ldateShour));
		}
		else {
			sldate = sldate.concat(String.valueOf(ldateShour));
		}
		
		if(ldateSminute >= 0 && ldateSminute <= 9) {
			sldate = sldate.concat(String.valueOf("0" + ldateSminute));
		}
		else {
			sldate = sldate.concat(String.valueOf(ldateSminute));
		}
		long localNowTime = Long.parseLong(sldate);
		
		for(int i=0; i<rEntityList.size(); i++) {
			//	講座情報の開催日・開始時刻のデータを格納
			String year = String.valueOf(rEntityList.get(i).getThe_date()).substring(0, 4);
			int iyear = Integer.parseInt(year);
			String month = String.valueOf(rEntityList.get(i).getThe_date()).substring(5, 7);
			int imonth = Integer.parseInt(month);
			String day = String.valueOf(rEntityList.get(i).getThe_date()).substring(8, 10);
			int iday = Integer.parseInt(day);
			String start_time = String.valueOf(rEntityList.get(i).getStart_time());
			String start_time2 = start_time.substring(0, 2);
			start_time2 = start_time2.concat(start_time.substring(3, 5));
			int istart_time = Integer.parseInt(start_time2);
			year = String.valueOf(iyear);
			month = String.valueOf(imonth);
			day = String.valueOf(iday);
			String changeStartTime = String.valueOf(istart_time);
			
			String changeBeforeInt = year;
			if(imonth >= 1 && imonth <= 9) {
				changeBeforeInt = changeBeforeInt.concat("0" + month);
			}
			else {
				changeBeforeInt = changeBeforeInt.concat(month);
			}
			
			if(iday >= 1 && iday <= 9) {
				changeBeforeInt = changeBeforeInt.concat("0" + day);
			}
			else {
				changeBeforeInt = changeBeforeInt.concat(day);
			}
			changeBeforeInt = changeBeforeInt.concat(changeStartTime);
			long courseHoldTime = Long.parseLong(changeBeforeInt);
			
			if(localNowTime < courseHoldTime) {
				situationList.add("開催予定");
			}
			else if(localNowTime > courseHoldTime) {
				situationList.add("終了");
			}
			else {
				situationList.add("開催中");
			}
		}		
		return situationList;
	}
	
	//	講座更新・	削除機能で、DBからcourseテーブルのデータのthe_date, start_time, end_timeをそれぞれ年月日・時分に分解する
	public RegisterEntity theDateDisassembly(RegisterEntity registerEntity) {
		//	開催日
		Date the_date = registerEntity.getThe_date();
		String s_the_date = String.valueOf(the_date);
		String year = s_the_date.substring(0, 4);
		String month = s_the_date.substring(5, 7);
		String day = s_the_date.substring(8, 10);
		Integer iyear = Integer.parseInt(year);
		Integer imonth = Integer.parseInt(month);
		Integer iday = Integer.parseInt(day);
		registerEntity.setIyear(iyear);
		registerEntity.setImonth(imonth);
		registerEntity.setIday(iday);
		//	開始時刻
		String start_time = registerEntity.getStart_time();
		String shour = start_time.substring(0, 2);
		Integer hour = Integer.parseInt(shour);
		String sminute = start_time.substring(3, 5);
		registerEntity.setShour(hour);
		registerEntity.setSminute(sminute);
		//	終了時刻
		String end_time = registerEntity.getEnd_time();
		String ehour = end_time.substring(0, 2);
		Integer hour2 = Integer.parseInt(ehour);
		String eminute = end_time.substring(3, 5);
		registerEntity.setEhour(hour2);
		registerEntity.setEminute(eminute);		
		return registerEntity;
	}
	
	/*
	 * 分解された年月日時分をそれぞれthe_date, 
	 * start_time, end_timeに結合してDB格納用のRegisterInsertEntityに全カラムの当該データを格納する
	 * 
	 * @param RegisterEntity
	 */  
	public RegisterInsertEntity registerDataChangeStore(RegisterEntity registerEntity) {
		RegisterInsertEntity riEntity = new RegisterInsertEntity();
		//	開催日時
		String the_date = String.valueOf(registerEntity.getIyear());
		the_date = the_date.concat("-");
		the_date = the_date.concat(String.valueOf(registerEntity.getImonth()));
		the_date = the_date.concat("-");
		the_date = the_date.concat(String.valueOf(registerEntity.getIday()));
		Date date = Date.valueOf(the_date);
		riEntity.setThe_date(date);
		//	開始時刻
		String start_time = String.valueOf(registerEntity.getShour());
		start_time = start_time.concat(":");
		start_time = start_time.concat(registerEntity.getSminute());
		riEntity.setStart_time(start_time);
		//	終了時刻
		String end_time = String.valueOf(registerEntity.getEhour());
		end_time = end_time.concat(":");
		end_time = end_time.concat(registerEntity.getEminute());
		riEntity.setEnd_time(end_time);
		
		//	上記3カラム以外格納
		riEntity.setCourse_no(registerEntity.getCourse_no());
		riEntity.setCourse_name(registerEntity.getCourse_name());
		riEntity.setCapacity(registerEntity.getCapacity());	
		return riEntity;
	}
}
