package com.changshagaosu.roadtools.bean;

import java.util.List;

/**
 * 维修工程Bean
 */
public class RepairProjectBean {
	private int id;//这应该是自己的ID
	private String DTypeID; //这应该是父Id
	private String DTypeNumber; //工程量
	private String DTypeName; //工程名称
	private String DTypeUnit; //单位(年、月、日)

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDTypeID() {
		return DTypeID;
	}

	public void setDTypeID(String dTypeID) {
		DTypeID = dTypeID;
	}

	public String getDTypeNumber() {
		return DTypeNumber;
	}

	public void setDTypeNumber(String dTypeNumber) {
		DTypeNumber = dTypeNumber;
	}

	public String getDTypeName() {
		return DTypeName;
	}

	public void setDTypeName(String dTypeName) {
		DTypeName = dTypeName;
	}

	public String getDTypeUnit() {
		return DTypeUnit;
	}

	public void setDTypeUnit(String dTypeUnit) {
		DTypeUnit = dTypeUnit;
	}

	public static class Data {
		private List<RepairProjectBean> DiseaseItem;

		public List<RepairProjectBean> getDiseaseItem() {
			return DiseaseItem;
		}

		public void setDiseaseItem(List<RepairProjectBean> diseaseItem) {
			DiseaseItem = diseaseItem;
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