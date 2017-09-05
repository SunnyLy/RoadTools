package com.changshagaosu.roadtools.bean.sub;

import java.util.List;

public class RoadLine {
	private int id;
	private String LineName;
	private List<Traffic> Traffic;
	private String LineID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLineName() {
		return LineName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public List<Traffic> getTraffic() {
		return Traffic;
	}

	public void setTraffic(List<Traffic> Traffic) {
		this.Traffic = Traffic;
	}

	public String getLineID() {
		return LineID;
	}

	public void setLineID(String lineID) {
		LineID = lineID;
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

	public static class Traffic {
		private String LineName;
		private String LineID;
		private String TrafficLineID;
		private String TrafficLineName;
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTrafficLineID() {
			return TrafficLineID;
		}

		public void setTrafficLineID(String trafficLineID) {
			TrafficLineID = trafficLineID;
		}

		public String getTrafficLineName() {
			return TrafficLineName;
		}

		public void setTrafficLineName(String trafficLineName) {
			TrafficLineName = trafficLineName;
		}

		public String getLineName() {
			return LineName;
		}

		public void setLineName(String lineName) {
			LineName = lineName;
		}

		public String getLineID() {
			return LineID;
		}

		public void setLineID(String lineID) {
			LineID = lineID;
		}

	}
}
