package com.changshagaosu.roadtools.bean;

public class C_Company {
	private int id;
	private String Name;
	private String HeadPerson;
	private String Phone;
	private String CompanyType;
	private int ParentCompany;
	private String Remark;
	private int OrderNumber;
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

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getHeadPerson() {
		return HeadPerson;
	}

	public void setHeadPerson(String headPerson) {
		HeadPerson = headPerson;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getCompanyType() {
		return CompanyType;
	}

	public void setCompanyType(String companyType) {
		CompanyType = companyType;
	}

	public int getParentCompany() {
		return ParentCompany;
	}

	public void setParentCompany(int parentCompany) {
		ParentCompany = parentCompany;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public int getOrderNumber() {
		return OrderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		OrderNumber = orderNumber;
	}

}
