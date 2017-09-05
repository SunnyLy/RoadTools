package com.changshagaosu.roadtools.bean;

import java.util.List;

public class Item {
	private int id;
	private String DTypeID;
	private String DTypeNumber;
	private String DTypeName;
	private String DTypeUnit;

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
		private List<Item> DiseaseItem;

		public List<Item> getDiseaseItem() {
			return DiseaseItem;
		}

		public void setDiseaseItem(List<Item> diseaseItem) {
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