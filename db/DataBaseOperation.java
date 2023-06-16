package com.example.demo.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.RegisterInsertEntity;

/*	
 * DB操作クラス
 * MyBatis適用後は未使用
 */

public class DataBaseOperation {
	ResultSet rs = null;
	PreparedStatement ps = null;
	Connection con = null;
	String sql = null;
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("クラスのロードに失敗しました。");
		}
	}
	
	//	insert 登録クラス
	public void insertOperation(RegisterInsertEntity riEntity) throws SQLException{
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance", "root", "");
			con.setAutoCommit(true);
			sql = "insert into course(course_no, course_name, the_date, start_time, end_time, capacity, inp_date) values(?, ?, ?, ?, ?, ?, ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, riEntity.getCourse_no());
			ps.setString(2, riEntity.getCourse_name());
			ps.setDate(3, riEntity.getThe_date());
			ps.setString(4, riEntity.getStart_time());
			ps.setString(5, riEntity.getEnd_time());
			ps.setInt(6, riEntity.getCapacity());
			ps.setDate(7, Date.valueOf(LocalDate.now()));
			
			ps.executeUpdate();
		} catch(SQLIntegrityConstraintViolationException e2) {
			System.out.println("登録失敗しました。。。");
		} catch (SQLException e) {
			System.out.println("登録失敗しました。。。");
			
			if(con != null) {
				con.close();
			}
			if(ps != null) {
				ps.close();
			}
		}
	}
	
	//	講座番号重複チェック
	public List<String> DuplicateCourseNum() throws SQLException{
		List<String> courseNoList = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance", "root", "");
			con.setAutoCommit(true);
			sql = "SELECT course_no FROM course";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while(rs.next()) {
				courseNoList.add(rs.getString(1));		//	メモ：左記の1はテーブルの列番号を指定するので、ループで繰り返しても不変
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			System.out.println("データ抽出失敗しました。。。。");
			
			if(con != null) {
				con.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(rs != null) {
				rs.close();
			}
		}
		return courseNoList;
	}
}
