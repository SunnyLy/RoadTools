package com.nobcdz.upload;

import java.util.Properties;

import android.content.Context;

import com.changshagaosu.roadtools.preference.FtpPreference;
import com.changshagaosu.roadtools.preference.ServerPreference;

public class URLUtils {
	private Context context;
	private Properties props;
	private String FtpHost;
	private int FtpPort;
	private String FtpUser;
	private String FtpPwd;
	private String LogOn;
	private String InitData;
	private String AddCheckDailyRecord;
	private String AddCheckDailyRecordGPSData;
	private String DiseaseReportingSubmitData;
	private String AddPaveBlock;

	private Properties loadProperties() {

		Properties props = new Properties();
		try {
			int id = context.getResources().getIdentifier("url", "raw",
					context.getPackageName());
			props.load(context.getResources().openRawResource(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}

	public String getAddCheckDailyRecordGPSData(Context context) {
		this.context = context;
		props = loadProperties();
		AddCheckDailyRecordGPSData = props.getProperty(
				"AddCheckDailyRecordGPSData", "");
		return "http://"+ServerPreference.getHostName()+"/yhapp/AndroidAPI/AddCheckDailyRecordGPSData";
	}

	public String getDiseaseReportingSubmitData(Context context) {
		this.context = context;
		props = loadProperties();
		DiseaseReportingSubmitData = props.getProperty(
				"DiseaseReportingSubmitData", "");
		return "http://"+ServerPreference.getHostName()+"/yhapp/AndroidAPI/DiseaseReportingSubmitData";
	}

	public String getAddPaveBlock(Context context) {
		this.context = context;
		props = loadProperties();
		AddPaveBlock = props.getProperty("AddPaveBlock", "");
		return "http://"+ServerPreference.getHostName()+"/yhapp/AndroidAPI/AddPaveBlock";
	}

	public String getLogOn(Context context) {
		this.context = context;
		props = loadProperties();
		LogOn = props.getProperty("LogOn", "");
		return "http://"+ServerPreference.getHostName()+"/yhapp/API/API.aspx?Action=Login";
	}

	public String getInitData(Context context) {
		this.context = context;
		props = loadProperties();
		InitData = props.getProperty("InitData", "");
		return "http://"+ServerPreference.getHostName()+"/yhapp/API/API.aspx?";
	}

	public String getAddCheckDailyRecord(Context context) {
		this.context = context;
		props = loadProperties();
		AddCheckDailyRecord = props.getProperty("AddCheckDailyRecord", "");
		return "http://"+ServerPreference.getHostName()+"/yhapp/API/API.aspx?Action=AddMLog";
	}

	public String getFtpHost(Context context) {
		this.context = context;
		props = loadProperties();
		FtpHost = props.getProperty("FtpHost", "");
		return FtpPreference.getHostName().split(":")[0];
	}

	public int getFtpPort(Context context) {
		this.context = context;
		props = loadProperties();
		FtpPort = Integer.parseInt(props.getProperty("FtpPort", "0"));
		return Integer.parseInt(FtpPreference.getHostName().split(":")[1]);
	}

	public String getFtpUser(Context context) {
		this.context = context;
		props = loadProperties();
		FtpUser = props.getProperty("FtpUser", "");
		return FtpUser;
	}

	public String getFtpPwd(Context context) {
		this.context = context;
		props = loadProperties();
		FtpPwd = props.getProperty("FtpPwd", "");
		return FtpPwd;
	}

}