package com.execution.service.monitoring_execution_service.utility;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeAdapter {
	public static final DateFormat dateTimeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat dateTimeTitleFormat=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	public static final DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	
	
	public static String fromDateTimeToString(Date dateTime){
		String dateTimeStr = "";
		if (dateTime != null){
			dateTimeStr=dateTimeFormat.format(dateTime);
		}
		return dateTimeStr;
	}
	
	public static String fromDateToString(Date date){
		String dateStr = "";
		if (date != null){
			dateStr = dateFormat.format(date);
		}
		return dateStr;
	}
	
	
	public static String fromDateTimeToTitleString(Date dateTime){
		String dateTimeStr = "";
		if (dateTime != null){
			dateTimeStr=dateTimeTitleFormat.format(dateTime);
		}
		return dateTimeStr;
	}
	
	public static Date fromStringToDateTime (String dateTimeStr){
		Date dateTime=null;
		try{
			if(dateTimeStr != null){
				dateTime=dateTimeFormat.parse(dateTimeStr);
			}
		}
		catch(ParseException e){
			
			//TODO: write into logger
		}
		return dateTime;
	}
	
	public static String fromTimestampToString(Timestamp ts){
		String dateTimeStr = "";
		if(ts != null){
			dateTimeStr=dateTimeFormat.format(ts);
		}
		return dateTimeStr;
	}
}
