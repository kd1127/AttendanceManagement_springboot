package com.example.demo.error;

import java.util.List;

import com.example.demo.entity.ApplicationEntity;
import com.example.demo.entity.RegisterEntity;

public interface AttendanceErrorCheck {
	List<String> errorCheck(ApplicationEntity apEntity);
}
