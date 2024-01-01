package com.example.demo.mapper;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.ApplicationEntity;
import com.example.demo.entity.CourseApplyEntity;
import com.example.demo.entity.RegisterEntity;
import com.example.demo.entity.RegisterInsertEntity;

@Mapper
public interface AttendanceMapper {
	//	講座登録機能
	void insert(RegisterInsertEntity riEntity);
	//	講座申し込み機能(登録)
	void insertApplication(ApplicationEntity apEntity);
	//	講座申し込み機能(取得)
	List<String> selectCourseNo();		//	講座番号
	List<String> selectCourseList();	//	講座名
	List<String> selectTheDate();		//	開催日時
	List<String> selectStartTime();		//	開始時刻
	List<String> selectEndTime();		//	終了時刻
	List<Integer> selectCapacity();		//	定員
	int selectApplicationId();			//	applicationテーブルのid取得
	//	講座申し込み機能（更新）
	void updateCapacity(String course_no);
	//	course_applyテーブルに登録
	void insertCourseApply(CourseApplyEntity caEntity);
	//	ユーザー登録機能
	List<String> idListSelect();
	void insertAllUser(String user_id, String passwd, Integer authority, Date inp_date);
	//	ログイン機能
	List<String> userIdListSelect();
	Integer authorityListSelect(String userId);
	List<String> passWdListSelect();
	//	講座検索
	//	全件検索
	List<RegisterEntity> dateAndTimeFindAll();			//	開催時刻・開始時刻・終了時刻抽出
	List<String> courseNoFindAll();						//	講座番号
	List<String> courseNameFindAll();					//	講座名
	List<Integer> capacityFindAll();					//	定員
	//	絞り込み
	List<String> courseNoSearch(RegisterEntity registerEntity);	//	講座番号検索用メソッド
	List<String> courseNameSearch(RegisterEntity registerEntity);
	List<RegisterEntity> dateAndTimeSearch(RegisterEntity registerEntity);
	List<Integer> capacitySearch(RegisterEntity registerEntity);
	//	講座削除
	RegisterEntity courseSelect(String course_no);		//	講座番号をキーに講座情報を取得
	void courseDelete(String course_no);				//	講座番号をキーに講座を削除
	void courseApplyDelete(String course_no);			//	外部キーで連携されたcourse_applyテーブルのデータ削除
	void Preparation();									//	カラムに設定された外部キーの設定を削除
	void postProcessing();								//	外部キーの設定を元に戻す処理
	//	講座更新
	void courseUpdate(RegisterInsertEntity riEntity);				//	講座番号をキーに該当のデータを更新
}
