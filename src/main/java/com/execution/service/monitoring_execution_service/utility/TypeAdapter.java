package com.execution.service.monitoring_execution_service.utility;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;







public class TypeAdapter<T> {
	
	
	public static Class<?> fromCheckTypeToJavaType(String checkType){
		if (checkType!= null && checkType.equalsIgnoreCase("Numeric")){
			return Double.class;
		}
		else if (checkType!= null && checkType.equalsIgnoreCase("String")){
			return String.class;
		}
		return null;
	}
	/*
	public static boolean isSameType(String checkType, int sqlType){
		if (checkType!= null && checkType.equalsIgnoreCase("Numeric")){
			return Double.class;
		}
		else if (checkType!= null && checkType.equalsIgnoreCase("String")){
			return String.class;
		}
		return null;
	}
	
*/

	
	public static Class<?> fromSqlTypeToJavaType(int sqlType){
		//String
		if(sqlType == Types.VARCHAR || sqlType == Types.CHAR || sqlType == Types.LONGNVARCHAR || sqlType == -1){
			return String.class;
		}
		//Integer
		else if(sqlType == Types.INTEGER || sqlType == Types.TINYINT || sqlType == Types.SMALLINT ){
			return Integer.class;
		}
		//Long
		else if(sqlType == Types.BIGINT){
			return Long.class;
		}
		//Float
		else if(sqlType == Types.REAL){
			return Float.class;
		}
		//Double
		else if(sqlType == Types.FLOAT || sqlType == Types.DOUBLE){
			return Double.class;
		}
		//Date
		else if(sqlType == Types.DATE){
			return Date.class;
		}
		//BigDecimal
		else if(sqlType == Types.NUMERIC || sqlType == Types.DECIMAL){
			return BigDecimal.class;
			
		}
		//Boolean
		else if(sqlType ==  Types.BOOLEAN || sqlType == Types.BIT){
			return Boolean.class;
		}
		//Time
		else if(sqlType == Types.TIME){
			return Time.class;
		}
		//Timestamp
		else if(sqlType == Types.TIMESTAMP){
			return Timestamp.class;
		}
		
		return null;
	}
	
	
	public static String fromResultSetToString(ResultSet rs, int columnNumber, int sqlType) throws SQLException{
		if(rs == null ){
			return "";
		}
		
		//String
		if(sqlType == Types.VARCHAR || sqlType == Types.CHAR || sqlType == Types.LONGNVARCHAR || sqlType == -1){
			return rs.getString(columnNumber);
		}
		//Integer
		else if(sqlType == Types.INTEGER || sqlType == Types.TINYINT || sqlType == Types.SMALLINT ){
			return String.valueOf(rs.getInt(columnNumber));
		}
		//BigInt
		else if(sqlType == Types.BIGINT){
			BigInteger bi =  BigInteger.valueOf(rs.getLong(columnNumber));
			return bi.toString();
			//return String.valueOf(rs.getLong(columnNumber));
		}
		//Float
		else if(sqlType == Types.REAL){
			return String.valueOf(rs.getFloat(columnNumber));
		}
		//Double
		else if(sqlType == Types.FLOAT || sqlType == Types.DOUBLE){
			return String.valueOf(rs.getDouble(columnNumber));
		}
		//Date
		else if(sqlType == Types.DATE){
			Date date=rs.getDate(columnNumber);
			if(date!=null){
				java.util.Date value=new java.util.Date(date.getTime());
				return DateTimeAdapter.fromDateTimeToString(value);
			}
		}
		//BigDecimal
		else if(sqlType == Types.NUMERIC || sqlType == Types.DECIMAL){
			BigDecimal value= rs.getBigDecimal(columnNumber);
			if(value != null){
				//double dValue = value.doubleValue();
				return value.toPlainString();
			}
			
		}
		//Boolean
		else if(sqlType ==  Types.BOOLEAN || sqlType == Types.BIT){
			Boolean value=rs.getBoolean(columnNumber);
			if(value){
				return "True";
			}
			else{
				return "False";
			}
		}
		//Time
		else if(sqlType == Types.TIME){
			Time value=rs.getTime(columnNumber);
			return value.toString();
		}
		//Timestamp
		else if(sqlType == Types.TIMESTAMP){
			Timestamp ts=rs.getTimestamp(columnNumber);
			return DateTimeAdapter.fromTimestampToString(ts);
		}
		
		return "";
		
	}
	
	

	
}
