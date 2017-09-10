package com.changshagaosu.roadtools.bean.sub;

import java.util.List;

public class Disease {
	private int id;
	private String DictionaryName;
	private List<DiseaseType> DiseaseType;
    private String DictionaryId;

    @Override
    public String toString() {
        return "Disease{" +
                "id=" + id +
                ", DictionaryName='" + DictionaryName + '\'' +
                ", DiseaseType=" + DiseaseType +
                ", DictionaryId='" + DictionaryId + '\'' +
                '}';
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDictionaryName() {
		return DictionaryName;
	}

	public void setDictionaryName(String DictionaryName) {
		this.DictionaryName = DictionaryName;
	}

	public List<DiseaseType> getDiseaseType() {
		return DiseaseType;
	}

	public void setDiseaseType(List<DiseaseType> DiseaseType) {
		this.DiseaseType = DiseaseType;
	}

    public String getDictionaryId() {
        return DictionaryId;
    }

    public void setDictionaryId(String DictionaryCode) {
        this.DictionaryId = DictionaryCode;
    }

	public static class Data {
		private List<Disease> Disease;

		public List<Disease> getDisease() {
			return Disease;
		}

		public void setDisease(List<Disease> disease) {
			Disease = disease;
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

	public static class DiseaseType {
		private String DictionaryName;
        private String DictionaryId;
        private String CommonTypeID;
		private String CommonTypeName;
		private int id;

        @Override
        public String toString() {
            return "DiseaseType{" +
                    "DictionaryName='" + DictionaryName + '\'' +
                    ", DictionaryId='" + DictionaryId + '\'' +
                    ", CommonTypeID='" + CommonTypeID + '\'' +
                    ", CommonTypeName='" + CommonTypeName + '\'' +
                    ", id=" + id +
                    '}';
        }

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getCommonTypeID() {
			return CommonTypeID;
		}

		public void setCommonTypeID(String commonTypeID) {
			this.CommonTypeID = commonTypeID;
		}

		public String getCommonTypeName() {
			return CommonTypeName;
		}

		public void setCommonTypeName(String commonTypeName) {
			this.CommonTypeName = commonTypeName;
		}

		public String getDictionaryName() {
			return DictionaryName;
		}

		public void setDictionaryName(String dictionaryName) {
			DictionaryName = dictionaryName;
		}

        public String getDictionaryId() {
            return DictionaryId;
        }

        public void setDictionaryId(String dictionaryId) {
            DictionaryId = dictionaryId;
        }

	}
}
