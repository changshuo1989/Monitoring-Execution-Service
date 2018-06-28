package com.execution.service.monitoring_execution_service.model;


public class CheckInfo {
	
	private int id;
	private int sequence;
	private String benchmark;
	private String checkBenchmarkType;
	private String attributeName;
	private boolean isActive;
	private String checkConjunctionType;
	private String checkLogicType;
	private String checkOperatorType;
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getBenchmark() {
		return benchmark;
	}
	public void setBenchmark(String benchmark) {
		this.benchmark = benchmark;
	}
	public String getCheckBenchmarkType() {
		return checkBenchmarkType;
	}
	public void setCheckBenchmarkType(String checkBenchmarkType) {
		this.checkBenchmarkType = checkBenchmarkType;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getCheckConjunctionType() {
		return checkConjunctionType;
	}
	public void setCheckConjunctionType(String checkConjunctionType) {
		this.checkConjunctionType = checkConjunctionType;
	}
	public String getCheckLogicType() {
		return checkLogicType;
	}
	public void setCheckLogicType(String checkLogicType) {
		this.checkLogicType = checkLogicType;
	}
	public String getCheckOperatorType() {
		return checkOperatorType;
	}
	public void setCheckOperatorType(String checkOperatorType) {
		this.checkOperatorType = checkOperatorType;
	}
	/*
	 * 1 - trigger
	 * 0 - waiting for the complete traverse(trigger)
	 * -1 - not trigger
	 * */
	
	public int isTriggered(String columnName, int sqlType, String value){
		int res = -1;
		System.out.println("attributeName: "+attributeName);
		System.out.println("columnName: "+columnName);
		System.out.println("check isActive: " + this.isActive);
		System.out.println("this.checkOperatorType: "+this.checkOperatorType);
		//System.out.println("check logic type:"+ this.getCheckLogicType());
		try{
			if(this.attributeName.equalsIgnoreCase(columnName) && this.isActive){
				if (this.checkBenchmarkType.equalsIgnoreCase("Numeric") && 
						(this.checkOperatorType.equals("=") || this.checkOperatorType.equals(">") 
						|| this.checkOperatorType.equals(">=") || this.checkOperatorType.equals("<") ||
						this.checkOperatorType.equals("<="))){
					
					double v = Double.parseDouble(value);
					//System.out.println("v:" +v);
					double b = Double.parseDouble(this.benchmark);
					//System.out.println("b:" +b);
					
					if(this.checkOperatorType.equals("=")){
						if(v == b){
							if(this.checkLogicType.equalsIgnoreCase("Is all of")){
								res = 0;
							}
							else if(this.checkLogicType.equalsIgnoreCase("Is any of")){
								res = 1;
							}
						}
					}
					
					else if(this.checkOperatorType.equals("!=")){
						System.out.println("v: " +v);
						System.out.println("b: " +b);
						if(v != b){
							if(this.checkLogicType.equalsIgnoreCase("Is all of")){
								res = 0;
							}
							else if(this.checkLogicType.equalsIgnoreCase("Is any of")){
								res = 1;
							}
						}
					}
					else if(this.checkOperatorType.equals(">")){
						if(v > b){
							if(this.checkLogicType.equalsIgnoreCase("Is all of")){
								res = 0;
							}
							else if(this.checkLogicType.equalsIgnoreCase("Is any of")){
								res = 1;
							}
						}
					}
					else if(this.checkOperatorType.equalsIgnoreCase(">=")){
						if(v >= b){
							if(this.checkLogicType.equalsIgnoreCase("Is all of")){
								res = 0;
							}
							else if(this.checkLogicType.equalsIgnoreCase("Is any of")){
								res = 1;
							}
						}
					}
					else if(this.checkOperatorType.equals("<")){
						if(v < b){
							if(this.checkLogicType.equalsIgnoreCase("Is all of")){
								res = 0;
							}
							else if(this.checkLogicType.equalsIgnoreCase("Is any of")){
								res = 1;
							}
						}
					}
					else if(this.checkOperatorType.equals("<=")){
						if(v <= b){
							if(this.checkLogicType.equalsIgnoreCase("Is all of")){
								res = 0;
							}
							else if(this.checkLogicType.equalsIgnoreCase("Is any of")){
								res = 1;
							}
						}
					}
				}
				else if(this.checkBenchmarkType.equalsIgnoreCase("String") 
						&& this.checkOperatorType.equalsIgnoreCase("CONTAINS")
						&& this.isActive){
					if(value.contains(this.benchmark)){
						
						if(this.checkLogicType.equalsIgnoreCase("Is all of")){
							res = 0;
						}
						else if(this.checkLogicType.equalsIgnoreCase("Is any of")){
							res = 1;
						}
					}
				}
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("res:" +res);
		return res;
	}
	
	
	
}
