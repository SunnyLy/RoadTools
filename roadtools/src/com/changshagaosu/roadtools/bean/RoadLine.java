package com.changshagaosu.roadtools.bean;

import java.util.List;

public class RoadLine {
	private int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String LineID;
	private String LinePID;
	private String LineShowNumber;
	private String LineNumber;
	private String LineName;
	private String LineGrade;
	private String LineStakeNumberStart;
	private String LineStakeNumberEnd;
	private String LineLength;
	private String Remark;
	private String ExtendName;
	private String CreateDate;
	private String CreateUserId;
	private String CreateUserName;
	private String LineStauts;
	
	public String getLineID() {
		return LineID;
	}

	public void setLineID(String lineID) {
		LineID = lineID;
	}

	public String getLinePID() {
		return LinePID;
	}

	public void setLinePID(String linePID) {
		LinePID = linePID;
	}

	public String getLineShowNumber() {
		return LineShowNumber;
	}

	public void setLineShowNumber(String lineShowNumber) {
		LineShowNumber = lineShowNumber;
	}

	public String getLineNumber() {
		return LineNumber;
	}

	public void setLineNumber(String lineNumber) {
		LineNumber = lineNumber;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public String getLineGrade() {
		return LineGrade;
	}

	public void setLineGrade(String lineGrade) {
		LineGrade = lineGrade;
	}

	public String getLineStakeNumberStart() {
		return LineStakeNumberStart;
	}

	public void setLineStakeNumberStart(String lineStakeNumberStart) {
		LineStakeNumberStart = lineStakeNumberStart;
	}

	public String getLineStakeNumberEnd() {
		return LineStakeNumberEnd;
	}

	public void setLineStakeNumberEnd(String lineStakeNumberEnd) {
		LineStakeNumberEnd = lineStakeNumberEnd;
	}

	public String getLineLength() {
		return LineLength;
	}

	public void setLineLength(String lineLength) {
		LineLength = lineLength;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getExtendName() {
		return ExtendName;
	}

	public void setExtendName(String extendName) {
		ExtendName = extendName;
	}

	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public String getCreateUserId() {
		return CreateUserId;
	}

	public void setCreateUserId(String createUserId) {
		CreateUserId = createUserId;
	}

	public String getCreateUserName() {
		return CreateUserName;
	}

	public void setCreateUserName(String createUserName) {
		CreateUserName = createUserName;
	}

	public String getLineStauts() {
		return LineStauts;
	}

	public void setLineStauts(String lineStauts) {
		LineStauts = lineStauts;
	}

	

	public static class Data {
		private List<RoadLine> RoadLine;
		public List<RoadLine> getRoadLine() {
			return RoadLine;
		}

		public void setRoadLine(List<RoadLine> roadLine) {
			RoadLine = roadLine;
		}
	}

	public static class RequestData {
		private boolean success;
		private Data data;
		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}

		
	}

}
