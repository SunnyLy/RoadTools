package com.changshagaosu.roadtools.bean;

import java.util.List;

public class InitData {
	private boolean success;
	private data data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public data getData() {
		return data;
	}

	public void setData(data data) {
		this.data = data;
	}

	public static class data {
		private List<R_RoadLine> R_RoadLine;
		private List<R_RoadPart> R_RoadPart;
		private List<R_DiseaseType> R_DiseaseType;
		private List<C_Company> C_Company;
		private List<R_TrafficLine> R_TrafficLine;
		
		public List<R_TrafficLine> getR_TrafficLine() {
			return R_TrafficLine;
		}

		public void setR_TrafficLine(List<R_TrafficLine> r_TrafficLine) {
			R_TrafficLine = r_TrafficLine;
		}

		public List<R_RoadLine> getR_RoadLine() {
			return R_RoadLine;
		}

		public void setR_RoadLine(List<R_RoadLine> r_RoadLine) {
			R_RoadLine = r_RoadLine;
		}

		public List<R_RoadPart> getR_RoadPart() {
			return R_RoadPart;
		}

		public void setR_RoadPart(List<R_RoadPart> r_RoadPart) {
			R_RoadPart = r_RoadPart;
		}

		public List<R_DiseaseType> getR_DiseaseType() {
			return R_DiseaseType;
		}

		public void setR_DiseaseType(List<R_DiseaseType> r_DiseaseType) {
			R_DiseaseType = r_DiseaseType;
		}

		public List<C_Company> getC_Company() {
			return C_Company;
		}

		public void setC_Company(List<C_Company> c_Company) {
			C_Company = c_Company;
		}

	}
}
