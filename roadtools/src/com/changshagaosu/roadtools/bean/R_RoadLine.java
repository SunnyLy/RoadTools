package com.changshagaosu.roadtools.bean;

public class R_RoadLine {

	private int id;

	private String RoadLineNumber;
	private String ParentRaodLineNumber;
	private String RoadLineName;
	private String RoadGrade;
	private String StakeNumberStart;
	private String StakeNumberEnd;
	private int Length;
	private String Remark;
	private String CreateDate;
	private int Creator;
	private String Guid;
	private int ParentID;
	private int initID;

	public int getInitID() {
		return initID;
	}

	public void setInitID(int initID) {
		this.initID = initID;
	}

	public int getId() {
		return id;
	}

	public void setId(int iD) {
		id = iD;
	}

	public String getRoadLineNumber() {
		return RoadLineNumber;
	}

	public void setRoadLineNumber(String roadLineNumber) {
		RoadLineNumber = roadLineNumber;
	}

	public String getParentRaodLineNumber() {
		return ParentRaodLineNumber;
	}

	public void setParentRaodLineNumber(String parentRaodLineNumber) {
		ParentRaodLineNumber = parentRaodLineNumber;
	}

	public String getRoadLineName() {
		return RoadLineName;
	}

	public void setRoadLineName(String roadLineName) {
		RoadLineName = roadLineName;
	}

	public String getRoadGrade() {
		return RoadGrade;
	}

	public void setRoadGrade(String roadGrade) {
		RoadGrade = roadGrade;
	}

	public String getStakeNumberStart() {
		return StakeNumberStart;
	}

	public void setStakeNumberStart(String stakeNumberStart) {
		StakeNumberStart = stakeNumberStart;
	}

	public String getStakeNumberEnd() {
		return StakeNumberEnd;
	}

	public void setStakeNumberEnd(String stakeNumberEnd) {
		StakeNumberEnd = stakeNumberEnd;
	}

	public int getLength() {
		return Length;
	}

	public void setLength(int length) {
		Length = length;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public int getCreator() {
		return Creator;
	}

	public void setCreator(int creator) {
		Creator = creator;
	}

	public String getGuid() {
		return Guid;
	}

	public void setGuid(String guid) {
		Guid = guid;
	}

	public int getParentID() {
		return ParentID;
	}

	public void setParentID(int parentID) {
		ParentID = parentID;
	}

}
