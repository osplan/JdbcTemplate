package com.zenoc.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class dealDate {
	public static final int YEAR = 1;
	public static final int MONTH = 2;
	public static final int DAY = 3;
	public static final int WEEK = 4;
	public static final int HOURS = 5;
	public static final int MINUTE = 6;
	public static final int SECOND = 7;
	public static final long TIME_AREA;
	private static int[] lastDays = {Calendar.getInstance().get(Calendar.MONTH)+1,31,28,31,30,31,30,31,31,30,31,30,31};
	
	static{
		TIME_AREA = Timestamp.valueOf("1970-01-01 00:00:00.000").getTime();
	}
	private dealDate(){
	}
	
	public static long dateDiff(int flag, Calendar c1, Calendar c2){
		if( c1==null || c2==null )throw new RuntimeException("Null Pointer In Two Calendar instance");
		//long distance = c2.getTimeInMillis() - c1.getTimeInMillis();
		switch(flag){
		case dealDate.YEAR :
			return c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		case dealDate.MONTH :
			return (c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH)+12*(c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR)));
		case dealDate.WEEK :
			return ((c2.getTimeInMillis()-TIME_AREA)/(86400000L*7) 
					- (c1.getTimeInMillis()-TIME_AREA)/(86400000L*7));
		case dealDate.DAY :
			return ((c2.getTimeInMillis()-TIME_AREA)/86400000 
					- (c1.getTimeInMillis()-TIME_AREA)/86400000);
		case dealDate.HOURS :
			return ((c2.getTimeInMillis()-TIME_AREA)/3600000 
					- (c1.getTimeInMillis()-TIME_AREA)/3600000);
		case dealDate.MINUTE :
			return ((c2.getTimeInMillis()-TIME_AREA)/60000 
					- (c1.getTimeInMillis()-TIME_AREA)/60000);
		case dealDate.SECOND :
			return ((c2.getTimeInMillis()-TIME_AREA)/1000 
					- (c1.getTimeInMillis()-TIME_AREA)/1000);
		default :
			throw new RuntimeException("Flag Not Found:"+flag);
		}
	}
	public static long dateDiff(int flag, Timestamp t1, Timestamp t2) {
		if( t1==null || t2==null ) throw new RuntimeException("Null Pointer In Two Timestamp Argument");
		Calendar c1,c2;
		(c1 = Calendar.getInstance()).setTimeInMillis(t1.getTime());
		(c2 = Calendar.getInstance()).setTimeInMillis(t2.getTime());
		return dealDate.dateDiff(flag,c1,c2);
	}
	public static long dateDiff(int flag,java.sql.Date t1,java.sql.Date t2){
		if( t1==null || t2==null ) throw new RuntimeException("Null Pointer In Two java.sql.Date Argument");
		Calendar c1,c2;
		(c1 = Calendar.getInstance()).setTimeInMillis(t1.getTime());
		(c2 = Calendar.getInstance()).setTimeInMillis(t2.getTime());
		return dealDate.dateDiff(flag,c1,c2);
	}
	public static long dateDiff(int flag,java.util.Date t1,java.util.Date t2){
		if( t1==null || t2==null ) throw new RuntimeException("Null Pointer In Two java.util.Date Argument");
		Calendar c1,c2;
		(c1 = Calendar.getInstance()).setTimeInMillis(t1.getTime());
		(c2 = Calendar.getInstance()).setTimeInMillis(t2.getTime());
		return dealDate.dateDiff(flag,c1,c2);
	}
	public static boolean isLeapYear(int year){
		return year%100==0?year%400==0:year%4==0;
	}
	public static boolean isLeapYear(Calendar t){
		return null==t?false:isLeapYear(t.get(Calendar.YEAR));
	}
	public static boolean isLeapYear(Timestamp t){
		if(null==t)return false;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return isLeapYear(c.get(Calendar.YEAR));
	}
	public static boolean isLeapYear(java.sql.Date t){
		if(null==t)return false;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return isLeapYear(c.get(Calendar.YEAR));
	}
	public static boolean isLeapYear(java.util.Date t){
		if(null==t)return false;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return isLeapYear(c.get(Calendar.YEAR));
	}
	public static void dateAdd(int flag,int increase,Calendar c){
		if(c==null)throw new RuntimeException("Null Pointer Calendar");
		if(increase==0)return;

		switch(flag){
		case dealDate.YEAR :
			c.add(Calendar.YEAR,increase);
			break;
		case dealDate.MONTH :
			c.add(Calendar.MONTH,increase);
			break;
		case dealDate.DAY :
			c.add(Calendar.DATE,increase);
			break;
		case dealDate.WEEK :
			c.add(Calendar.DATE,increase*7);
			break;
		case dealDate.HOURS :
			c.add(Calendar.HOUR_OF_DAY,increase);
			break;
		case dealDate.MINUTE :
			c.add(Calendar.MINUTE,increase);
			break;
		case dealDate.SECOND :
			c.add(Calendar.SECOND,increase);
			break;
		default :
			throw new RuntimeException("Flag Not Found:"+flag);
		}
	}
	public static void dateAdd(int flag,int increase,Timestamp e){
		if(e==null)throw new RuntimeException("Null Pointer Timestamp");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(e.getTime());
		dealDate.dateAdd(flag,increase,c);
		e.setTime(c.getTimeInMillis());
	}
	public static void dateAdd(int flag,int increase,java.sql.Date t){
		if(t==null)throw new RuntimeException("Null Pointer java.sql.Date");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		dealDate.dateAdd(flag,increase,c);
		t.setTime(c.getTimeInMillis());
	}
	public static void dateAdd(int flag,int increase,java.util.Date t){
		if(t==null)throw new RuntimeException("Null Pointer java.util.Date");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		dealDate.dateAdd(flag,increase,c);
		t.setTime(c.getTimeInMillis());
	}
	public static String toShortDate(Calendar c){
		if(c==null)return "";
		String m = "0"+(c.get(Calendar.MONTH)+1),d="0"+c.get(Calendar.DAY_OF_MONTH);
		return c.get(Calendar.YEAR)+"-"+m.substring(m.length()-2,m.length())+"-"+d.substring(d.length()-2,d.length());
	}
	public static String toShortDate(java.util.Date t){
		if(t==null)return "";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return dealDate.toShortDate(c);
	}
	public static String toShortDate(java.sql.Date t){
		if(t==null)return "";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return dealDate.toShortDate(c);
	}
	public static String toShortDate(Timestamp t){
		if(t==null)return "";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return dealDate.toShortDate(c);
	}
	public static String toShortDate(String t){
		return toShortDate(createDate(t, Calendar.getInstance()));
	}
	public static String toFullDateTime(Timestamp t){
		if(t==null)return "";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return dealDate.toFullDateTime(c);
	}
	public static String toFullDateTime(Calendar t){
		if(t==null)return "";
		String y,m,d,h,mi,se,ms;
		y = "" + t.get(Calendar.YEAR);
		m = "0" + (t.get(Calendar.MONTH)+1);
		d = "0" + t.get(Calendar.DAY_OF_MONTH);
		h = "0" + t.get(Calendar.HOUR_OF_DAY);
		mi = "0" + t.get(Calendar.MINUTE);
		se = "0" + t.get(Calendar.SECOND);
		ms = "" + t.get(Calendar.MILLISECOND)+"00";
		m = m.substring(m.length()-2,m.length());
		d = d.substring(d.length()-2,d.length());
		h = h.substring(h.length()-2,h.length());
		mi = mi.substring(mi.length()-2,mi.length());
		se = se.substring(se.length()-2,se.length());
		ms = ms.substring(0,3);
		return y+"-"+m+"-"+d+
			" " + h +":"+ mi +":"+ se
			+"."+ ms;
//		return toFullDateTime(new Timestamp(t.getTimeInMillis()));
	}
	public static String toFullDateTime(java.sql.Date t){
		if(t==null)return "";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return dealDate.toFullDateTime(c);
	}
	public static String toFullDateTime(java.util.Date t){
		if(t==null)return "";
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return dealDate.toFullDateTime(c);
	}
	public static String toFullDateTime(String t){
		return toFullDateTime(createDate(t, Calendar.getInstance()));
	}
	public static Date createDate(){
		return createDate(toShortDate(Calendar.getInstance()), new Date());
	}
	public static Calendar createDate(String ts, Calendar t){
//		Pattern p = Pattern.compile("^(\\d{1,4})([-.])(\\d{1,2})\\1(\\d{1,2)([ ](\\d{1,2}):(\\d{1,2}):(\\d{1,2})([.](\\d{1,3}))?)?");
//		Matcher m = p.matcher(ts);
//		t = Calendar.getInstance();
//		if(null==ts || "".equals(ts))return;
		Timestamp tt = null;
		tt = createDate(ts,tt);
		if(tt==null)return null;

		t = Calendar.getInstance();

		t.setTimeInMillis(tt.getTime());
		return t;
	}
	public static Timestamp createDate(String ts, Timestamp t){
		try{
			t = Timestamp.valueOf(ts);
		}catch(Exception e){
			try{
				t = Timestamp.valueOf(ts + ".000");
			}catch(Exception ex){
				try{
					t = Timestamp.valueOf(ts+" 00:00:00.000");
				}catch(Exception ex1){
					try{
						String fs[] = ts.split("-");
						fs[1] = "0"+fs[1];
						fs[2] = "0"+fs[2];
						fs[1] = fs[1].substring(fs[1].length()-2,fs[1].length());
						fs[2] = fs[2].substring(fs[2].length()-2,fs[2].length());
						t = Timestamp.valueOf(fs[0]+"-"+fs[1]+"-"+fs[2]+" 00:00:00.000");
					}catch(Exception ex2){
						return null;
					}
				}
			}
		}
		return t;
	}
	public static java.sql.Date createDate(String ts, java.sql.Date t){
		Timestamp tt = null;
		tt = createDate(ts,tt);
		if(tt==null)return null;

		t = new java.sql.Date(0L);

		t.setTime(tt.getTime());
		return t;
	}
	public static java.util.Date createDate(String ts, java.util.Date t){
		Timestamp tt = null;
		tt = createDate(ts,tt);
		if(tt==null)return null;

		t = new java.util.Date(0L);

		t.setTime(tt.getTime());
		return t;
	}
	/*
	 * getLastDayOfMonth()方法获取系统当前日期所在月的最大天数
	 */
	public static int getLastDayOfMonth(){
		int year = lastDays[0]/100;
		int month = lastDays[0]%100;
		return lastDays[month]+(month==2?(isLeapYear(year)?1:0):0);
	}
	public static int getLastDayOfMonth(int year,int month){
		if(month>12 || month<1){
			return 0;
		}
		return lastDays[month]+(month==2?(isLeapYear(year)?1:0):0);
	}
	public static int getLastDayOfMonth(Calendar t){
		if(null==t)return 0;
		return getLastDayOfMonth(t.get(Calendar.YEAR),t.get(Calendar.MONTH)+1);
	}
	public static int getLastDayOfMonth(Timestamp t){
		if(null==t)return 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return getLastDayOfMonth(c);
	}
	public static int getLastDayOfMonth(java.util.Date t){
		if(null==t)return 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return getLastDayOfMonth(c);
	}
	public static int getLastDayOfMonth(java.sql.Date t){
		if(null==t)return 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return getLastDayOfMonth(c);
	}
	public static int getLastDayOfMonth(String time){
		return getLastDayOfMonth(createDate(time, Calendar.getInstance()));
	}/**
	 * 取每月第一天日期
	 * @return
	 */
	public static java.util.Date getLastDateOfMonth(){
		java.util.Date d = createDate();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(d.getTime());
		c.set(Calendar.DAY_OF_MONTH,1);
		dateAdd(MONTH, 1, c);//加一个月
		dateAdd(DAY, -1, c);//退一天
		return createDate(toFullDateTime(c),d);
	}
	public static java.util.Date getLastDateOfMonth(int year,int month){
		String m = "00"+month,d = "00"+getLastDayOfMonth(year, month);
		return createDate(year+"-"+m.substring(m.length()-2,m.length())+"-"+d.substring(d.length()-2,d.length()),new Date(0L));
	}
	public static Calendar getLastDateOfMonth(Calendar t){
		if(null==t)return createDate(toFullDateTime(getLastDateOfMonth()), t);
		t.set(Calendar.DAY_OF_MONTH, 1);
		dateAdd(MONTH, 1, t);//加一个月
		dateAdd(DAY, -1, t);//退一天
		return t;
	}
	public static Timestamp getLastDateOfMonth(Timestamp t){
		if(null==t)return createDate(toFullDateTime(getLastDateOfMonth()), t);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		c.set(Calendar.DAY_OF_MONTH,1);
		dateAdd(MONTH, 1, c);//加一个月
		dateAdd(DAY, -1, c);//退一天
		return createDate(toFullDateTime(c),t);
	}
	public static java.util.Date getLastDateOfMonth(java.util.Date t){
		if(null==t)return createDate(toFullDateTime(getLastDateOfMonth()), t);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		c.set(Calendar.DAY_OF_MONTH,1);
		dateAdd(MONTH, 1, c);//加一个月
		dateAdd(DAY, -1, c);//退一天
		return createDate(toFullDateTime(c),t);
	}
	public static java.sql.Date getLastDateOfMonth(java.sql.Date t){
		if(null==t)return createDate(toFullDateTime(getLastDateOfMonth()), t);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		c.set(Calendar.DAY_OF_MONTH,1);
		dateAdd(MONTH, 1, c);//加一个月
		dateAdd(DAY, -1, c);//退一天
		return createDate(toFullDateTime(c),t);
	}
	public static Date getLastDateOfMonth(String t){
		if(null==t)return createDate(toFullDateTime(getLastDateOfMonth()), new Date(0L));
		return getLastDateOfMonth(createDate(t,new Date(0L)));
	}
	/**
	 * 取每月第一天日期
	 * @return
	 */
	public static java.util.Date getFirstDateOfMonth(){
		java.util.Date d = createDate();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(d.getTime());
		c.set(Calendar.DAY_OF_MONTH,1);
		return createDate(toFullDateTime(c),d);
	}
	public static java.util.Date getFirstDateOfMonth(int year,int month){
		return createDate(year+"-"+month+"-1",new Date(0L));
	}
	public static Calendar getFirstDateOfMonth(Calendar t) {
		if(null==t)return createDate(toFullDateTime(getFirstDateOfMonth()), t);
		t.set(Calendar.DAY_OF_MONTH, 1);
		return t;
	}
	public static Timestamp getFirstDateOfMonth(Timestamp t) {
		if(null==t)return createDate(toFullDateTime(getFirstDateOfMonth()), t);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		c.set(Calendar.DAY_OF_MONTH,1);
		return createDate(toFullDateTime(c),t);
	}
	public static java.util.Date getFirstDateOfMonth(java.util.Date t){
		if(null==t)return createDate(toFullDateTime(getFirstDateOfMonth()), t);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		c.set(Calendar.DAY_OF_MONTH,1);
		return createDate(toFullDateTime(c),t);
	}
	public static java.sql.Date getFirstDateOfMonth(java.sql.Date t){
		if(null==t)return createDate(toFullDateTime(getFirstDateOfMonth()), t);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		c.set(Calendar.DAY_OF_MONTH,1);
		return createDate(toFullDateTime(c),t);
	}
	public static Date getFirstDateOfMonth(String t){
		if(null==t)return createDate(toFullDateTime(getFirstDateOfMonth()), new Date(0L));
		return getFirstDateOfMonth(createDate(t,new Date(0L)));
	}
	/**
	 * 取两日期的大者，按日期的各个部分进行比较，如：dealDate.YEAR 只判断年份，dealDate.DATE
	 * @param flag
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static Calendar max(int flag, Calendar t1, Calendar t2){
		try{
			if(t1==null)return t2;
			if(t2==null)return t1;
			return dateDiff(flag,t1,t2)>=0?t2:t1;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static Timestamp max(int flag, Timestamp t1, Timestamp t2){
		try{
			if(t1==null)return t2;
			if(t2==null)return t1;
			return dateDiff(flag,t1,t2)>=0?t2:t1;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static java.sql.Date max(int flag,java.sql.Date t1,java.sql.Date t2){
		try{
			if(t1==null)return t2;
			if(t2==null)return t1;
			return dateDiff(flag,t1,t2)>=0?t2:t1;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static java.util.Date max(int flag,java.util.Date t1,java.util.Date t2){
		try{
			if(t1==null)return t2;
			if(t2==null)return t1;
			return dateDiff(flag,t1,t2)>=0?t2:t1;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static Date max(int flag, String t, String t1, String t2){
		return max(flag,createDate(t1,new Date(0L)),createDate(t2,new Date(0L)));
	}
	public static Calendar min(int flag, Calendar t1, Calendar t2){
		try{
			if(t1==null || t2==null)return null;
			return dateDiff(flag,t1,t2)>=0?t1:t2;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static Timestamp min(int flag, Timestamp t1, Timestamp t2){
		try{
			if(t1==null || t2==null)return null;
			return dateDiff(flag,t1,t2)>=0?t1:t2;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static java.sql.Date min(int flag,java.sql.Date t1,java.sql.Date t2){
		try{
			if(t1==null || t2==null)return null;
			return dateDiff(flag,t1,t2)>=0?t1:t2;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static java.util.Date min(int flag,java.util.Date t1,java.util.Date t2){
		try{
			if(t1==null || t2==null)return null;
			return dateDiff(flag,t1,t2)>=0?t1:t2;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	public static Date min(int flag, String t, String t1, String t2){
		return min(flag,createDate(t1,new Date(0L)),createDate(t2,new Date(0L)));
	}
	public static boolean isBetween(int flag, Calendar t, Calendar t1, Calendar t2){
		boolean true1=true,true2=true;
		try{
			if(t==null) return false;
			if(null==t1) true1=true;
			else{
				true1 = dateDiff(flag,t1,t)>=0?true:false;
			}
			if(null==t2){
				true2=true;
			}else{
				true2 = dateDiff(flag,t,t2)>=0?true:false;
			}
		}catch(Exception e){
			return false;
		}
		return (true1 && true2);
	}
	public static boolean isBetween(int flag, Timestamp t, Timestamp t1, Timestamp t2){
		boolean true1=true,true2=true;
		try{
			if(t==null) return false;
			if(null==t1) true1=true;
			else{
				true1 = dateDiff(flag,t1,t)>=0?true:false;
			}
			if(null==t2){
				true2=true;
			}else{
				true2 = dateDiff(flag,t,t2)>=0?true:false;
			}
		}catch(Exception e){
			return false;
		}
		return (true1 && true2);
	}
	public static boolean isBetween(int flag,java.sql.Date t,java.sql.Date t1,java.sql.Date t2){
		boolean true1=true,true2=true;
		try{
			if(t==null) return false;
			if(null==t1) true1=true;
			else{
				true1 = dateDiff(flag,t1,t)>=0?true:false;
			}
			if(null==t2){
				true2=true;
			}else{
				true2 = dateDiff(flag,t,t2)>=0?true:false;
			}
		}catch(Exception e){
			return false;
		}
		return (true1 && true2);
	}
	public static boolean isBetween(int flag,java.util.Date t,java.util.Date t1,java.util.Date t2){
		boolean true1=true,true2=true;
		try{
			if(t==null) return false;
			if(null==t1) true1=true;
			else{
				true1 = dateDiff(flag,t1,t)>=0?true:false;
			}
			if(null==t2){
				true2=true;
			}else{
				true2 = dateDiff(flag,t,t2)>=0?true:false;
			}
		}catch(Exception e){
			return false;
		}
		return (true1 && true2);
	}
	public static boolean isBetween(int flag, String t, String t1, String t2){
		return isBetween(flag,createDate(t, Calendar.getInstance()),createDate(t1, Calendar.getInstance()),createDate(t2, Calendar.getInstance()));
	}
	public static int getFullYear(Calendar t){
		if(null==t)return 0;
		return t.get(Calendar.YEAR);
	}
	public static int getFullYear(Timestamp t){
		if(null==t)return 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return getFullYear(c);
	}
	public static int getFullYear(java.util.Date t){
		if(null==t)return 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return getFullYear(c);
	}
	public static int getFullYear(java.sql.Date t){
		if(null==t)return 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return getFullYear(c);
	}
	public static int getFullYear(String t){
		if(null==t)return 0;
		return getFullYear(createDate(t, Calendar.getInstance()));
	}
	public static int getMonth(Calendar t){
		if(null==t)return 0;
		return t.get(Calendar.MONTH)+1;
	}
	public static int getMonth(Timestamp t){
		if(null==t)return 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return getMonth(c);
	}
	public static int getMonth(java.util.Date t){
		if(null==t)return 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return getMonth(c);
	}
	public static int getMonth(java.sql.Date t){
		if(null==t)return 0;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTime());
		return getMonth(c);
	}
	public static int getMonth(String t){
		if(null==t)return 0;
		return getMonth(createDate(t, Calendar.getInstance()));
	}
}