package com.zenoc.core.interfaces;


public class Condition {
	private String fieldName;
	private CompareType compareType;
	private Object value;
	public Condition(String fieldName, CompareType ct, Object v){
		this.fieldName = fieldName;
		this.compareType = ct;
		this.value = v;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.fieldName).append(compareType).append(value);
		return sb.toString();
	}
}
