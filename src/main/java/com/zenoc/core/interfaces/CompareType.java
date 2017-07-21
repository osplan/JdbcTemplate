package com.zenoc.core.interfaces;

public class CompareType {
	//EQUALS 为 =
	public static final CompareType EQUALS = new CompareType(0, "=");
	//GREATER 为 >
	public static final CompareType GREATER = new CompareType(1, ">");
	//LESS 为 <
	public static final CompareType LESS = new CompareType(2, "<");
	//GREATER_EQUALS 为 >=
	public static final CompareType GREATER_EQUALS = new CompareType(3, ">=");
	//LESS_EQUALS 为 <=
	public static final CompareType LESS_EQUALS = new CompareType(4, "<=");
	//NOT_EQUALS 为<>
	public static final CompareType NOT_EQUALS = new CompareType(5, "<>");
	//IS 为 is 
	public static final CompareType IS = new CompareType(6, "is");
	//IS_NOT 为 is null
	public static final CompareType IS_NOT = new CompareType(7, "is not");
	//INCLUDE 为 in
	public static final CompareType INCLUDE = new CompareType(8, "in");
	//EXCLUDE 为 not in
	public static final CompareType EXCLUDE = new CompareType(9, "not in");
	//LIKE 为 like
	public static final CompareType LIKE = new CompareType(10, "like");
	//NOT LIKE 为 not like
	public static final CompareType NOT_LIKE = new CompareType(11, "not like");
	//FIELD_EQUALS 为=
	public static final CompareType FIELD_EQUALS = new CompareType(12, "=");
	//FIELD_GREATER 为 >
	public static final CompareType FIELD_GREATER = new CompareType(13, ">");
	//FIELD_LESS 为 <
	public static final CompareType FIELD_LESS = new CompareType(14, "<");
	//FIELD_GREATER_EQUALS 为 >=
	public static final CompareType FIELD_GREATER_EQUALS = new CompareType(15, ">=");
	//FIELD_LESS_EQUALS 为 <=
	public static final CompareType FIELD_LESS_EQUALS = new CompareType(16, "<=");
	//FIELD_NOT_EQUALS 为<>
	public static final CompareType FIELD_NOT_EQUALS = new CompareType(17, "<>");
	
	private int value;
	private String type;
	
	private CompareType(int v,String t){
		this.value = v;
		this.type = t;
	}
	public int getValue(){
		return value;
	}
	public String getType(){
		return type;
	}
	public String toString(){
		return type;
	}
	public boolean equals(Object obj) {
		if(obj instanceof CompareType){
			CompareType o = (CompareType)obj;
			return o!=null && this.getValue()==o.getValue();
		}
		return super.equals(obj);
	}
}
