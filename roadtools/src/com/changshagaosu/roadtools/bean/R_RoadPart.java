package com.changshagaosu.roadtools.bean;

public class R_RoadPart {
	
	private int id;
	private String RoadPartName;
	private int RoadLineID;
	private String StakeNumberStart;
	private String StakeNumberEnd;
	private String RoadComposition;
	private String Remark;
	private String CreateDate;
	private int Creator;
	private String Guid;
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
	public String getRoadPartName() {
		return RoadPartName;
	}
	public void setRoadPartName(String roadPartName) {
		RoadPartName = roadPartName;
	}
	public int getRoadLineID() {
		return RoadLineID;
	}
	public void setRoadLineID(int roadLineID) {
		RoadLineID = roadLineID;
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
	public String getRoadComposition() {
		return RoadComposition;
	}
	public void setRoadComposition(String roadComposition) {
		RoadComposition = roadComposition;
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
	
}
