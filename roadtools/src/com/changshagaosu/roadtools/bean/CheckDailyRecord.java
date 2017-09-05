package com.changshagaosu.roadtools.bean;


public class CheckDailyRecord {
	private int id;
	private String CheckDailyRecordID, Company, CheckDate, CheckTime,
			CheckPerson, RoadLine, RoadPart, Weather, Recordor, LoginID,
			IsUpload;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCheckDailyRecordID() {
		return CheckDailyRecordID;
	}

	public void setCheckDailyRecordID(String checkDailyRecordID) {
		CheckDailyRecordID = checkDailyRecordID;
	}

	public String getCompany() {
		return Company;
	}

	public void setCompany(String company) {
		Company = company;
	}

	public String getCheckDate() {
		return CheckDate;
	}

	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}

	public String getCheckTime() {
		return CheckTime;
	}

	public void setCheckTime(String checkTime) {
		CheckTime = checkTime;
	}

	public String getCheckPerson() {
		return CheckPerson;
	}

	public void setCheckPerson(String checkPerson) {
		CheckPerson = checkPerson;
	}

	public String getRoadLine() {
		return RoadLine;
	}

	public void setRoadLine(String roadLine) {
		RoadLine = roadLine;
	}

	public String getRoadPart() {
		return RoadPart;
	}

	public void setRoadPart(String roadPart) {
		RoadPart = roadPart;
	}

	public String getWeather() {
		return Weather;
	}

	public void setWeather(String weather) {
		Weather = weather;
	}

	public String getRecordor() {
		return Recordor;
	}

	public void setRecordor(String recordor) {
		Recordor = recordor;
	}

	public String getLoginID() {
		return LoginID;
	}

	public void setLoginID(String loginID) {
		LoginID = loginID;
	}

	public String getIsUpload() {
		return IsUpload;
	}

	public void setIsUpload(String isUpload) {
		IsUpload = isUpload;
	}

}
