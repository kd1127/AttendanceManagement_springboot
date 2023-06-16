package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.ApplicationEntity;
import com.example.demo.entity.RegisterInsertEntity;

@Mapper
public interface AttendanceMapper {
	//	講座登録機能
	void insert(RegisterInsertEntity riEntity);
	//	講座申し込み機能()
	void insertApplication(ApplicationEntity apEntity);
	//	講座申し込み機能(取得)
	List<String> selectCourseList();	//	講座名抽出
	List<String> selectTheDate();		//	開催日時
	List<String> selectStartTime();		//	開始時刻
	List<String> selectEndTime();		//	終了時刻
	List<Integer> selectCapacity();		//	定員	
}
