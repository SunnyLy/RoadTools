package com.changshagaosu.roadtools.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 病害项目Bean
 */
public class DeaseProjectBean implements Serializable {
    private int ItemID;//病害项目主键
    private String ItemCode; //病害项目代号
    private String ItemName; //病害项目名称
    private String Engineering; //预计工程量
    private String ItemUnit; //病害项目单位
    private String Unitprice;//病害项目单价
    private String TotalMoney;//病害项目总额

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getEngineering() {
        return Engineering;
    }

    public void setEngineering(String engineering) {
        Engineering = engineering;
    }

    public String getItemUnit() {
        return ItemUnit;
    }

    public void setItemUnit(String itemUnit) {
        ItemUnit = itemUnit;
    }

    public String getUnitprice() {
        return Unitprice;
    }

    public void setUnitprice(String unitprice) {
        Unitprice = unitprice;
    }

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }

    public int getItemID() {

        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    @Override
    public String toString() {
        return "DeaseProjectBean{" +
                "ItemID=" + ItemID +
                ", ItemCode='" + ItemCode + '\'' +
                ", ItemName='" + ItemName + '\'' +
                ", Engineering='" + Engineering + '\'' +
                ", ItemUnit='" + ItemUnit + '\'' +
                ", Unitprice='" + Unitprice + '\'' +
                ", TotalMoney='" + TotalMoney + '\'' +
                '}';
    }

    public static class Data {
        private List<DeaseProjectBean> DiseaseItem;

        public List<DeaseProjectBean> getDiseaseItem() {
            return DiseaseItem;
        }

        public void setDiseaseItem(List<DeaseProjectBean> diseaseItem) {
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