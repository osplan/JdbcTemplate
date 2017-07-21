package com.zenoc.util;

import com.google.gson.GsonBuilder;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	private static final String maskCode = "!@$#%^&*(%^##$!";
	public static final String maskS = "***********************************************************************************";
	public static final String ZERO_STR = "000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	public static BigDecimal getBigDecimalDefault(String v, String d){
		return new BigDecimal(getDefault(v, d));
	}
	/**
	 * 字符串v如果为空串，则返回默认值d，否则为v本身。
	 * 
	 * @param v
	 * @param d
	 * @return
	 */
	public static String getDefault(String v, String d) {
		return isEmpty(v) ? d : v;
	}

	/**
	 * 判断字符串是否为空串
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s.trim());
	}

	/**
	 * 判断字符串是否为有效手机号
	 * 
	 * @param m
	 * @return
	 */
	public static boolean isTelephone(String m) {
		return !isEmpty(m) && m.matches("^1[3-9]\\d{9}$");
	}

	/**
	 * 判断字符串是否为Email格式
	 * 
	 * @param em
	 * @return
	 */
	public static boolean isEmail(String em) {
		return !isEmpty(em)
				&& em.matches("^([a-zA-Z0-9_\\.\\-])+@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
	}
	public static boolean isIpAddr(String ip){
		return !isEmpty(ip) && ip.matches("\\d+(\\.\\d+){3}");
	}

	/**
	 * 把字符串以splitBy为分隔符分成N个字符串，返回类型为List<String>
	 * 
	 * @param s
	 * @param splitBy
	 * @return
	 */
	public static List<String> splitToList(String s, String splitBy) {
		List<String> l = new ArrayList<String>();
		if (!isEmpty(s) && !isEmpty(splitBy)) {
			for (String f : s.split(splitBy)) {
				l.add(f);
			}
		}
		return l;
	}

	/**
	 * 首字母小写
	 * 
	 * @param s
	 * @return
	 */
	public static String toLowerFirstChar(String s) {
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	/**
	 * 首字母大写
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpperFirstChar(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
	public static String getPattern(String fs) {
		return getPatternString(fs, "pattern *= *(.*?)(,|$)", 1).replace("。",
				".").replace("，", ",");
	}
	public static String format(Object o, String pattern) {
		if (StringUtil.isEmpty(pattern))
			return String.valueOf(o);
		if (o instanceof Date) {
			return new SimpleDateFormat(pattern).format(o);
		} else if (o instanceof BigDecimal || o instanceof Integer
				|| o instanceof Float || o instanceof Double
				|| o instanceof Long) {
			return new DecimalFormat(pattern).format(o);
		} else
			return String.valueOf(o);
	}
	/**
	 * 获取指定类定义的成员变量（可获取父类成员变量）
	 * @param c 指定类
	 * @param field 指定成员名称
	 * @return Field
	 * @throws Exception
	 */
	public static Field getDeclaredField(Class<?> c, String field) throws Exception {
		if(c==null) throw new IllegalArgumentException(field + "变量不存在!");
		try{
			return c.getDeclaredField(field);
		}catch(NoSuchFieldException ex){
			if (!c.getName().equals(Object.class.getName()))
				return getDeclaredField(c.getSuperclass(), field);
		}
		throw new IllegalArgumentException(field + "变量不存在!");
	}
	/**
	 * 获取bean中的属性值
	 * 
	 * @param o
	 *            bean对象
	 * @param p
	 *            属性路径列表，一般直接使用 getValue(Object o,String p)
	 * @return
	 * @throws Exception
	 */
	public static String getValue(Object o, List<String> p) throws Exception {
		String ps = p.get(0);
		p.remove(0);

		try {
			if (p.size() > 0) {
//				Field f = o.getClass().getDeclaredField(ps);
				Field f = getDeclaredField(o.getClass(), ps);
				f.setAccessible(true);
				return getValue(f.get(o), p);
			} else {

				String[] pps = ps.split(",", 2);
//				Field f = o.getClass().getDeclaredField(pps[0]);
				Field f = getDeclaredField(o.getClass(),pps[0]);
				f.setAccessible(true);
				return format(f.get(o), getPattern(pps.length > 1
						? pps[1]
						: null));
				// return String.valueOf(f.get(o));
			}
		} catch (SecurityException e) {
			throw new Exception(ps + "变量不存在!");
		} catch (IllegalArgumentException e) {
			throw new Exception(ps + "变量不存在!");
		} catch (NoSuchFieldException e) {
			throw new Exception(ps + "变量不存在!");
		} catch (IllegalAccessException e) {
			throw new Exception(ps + "变量不存在!");
		} catch (Exception e) {
			throw new Exception(ps + "." + e.getMessage());
		}

	}

	/**
	 * 获取bean中属性值。
	 * 
	 * @param o
	 *            bean对象
	 * @param p
	 *            属性路径，可以多层访问，如
	 *            StringUtil.getValue(member,"account.totalAssets");
	 * @return
	 * @throws Exception
	 */
	public static String getValue(Object o, String p) throws Exception {
		return getValue(o, splitToList(p, "\\."));
	}


	/**
	 * 将 bean中的属性 替换到模板字符串中对应的{propertyName}中
	 * 
	 * @param s
	 *            模板字符串
	 * @param o
	 *            bean数据对象
	 * @return
	 * @throws Exception
	 */
	public static String fillBean(String s, Object o) {
		if (o == null)
			return s;
		if(isEmpty(s))return "";
		String v = null;
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher m = p.matcher(s);
		int i = 0;
		while (m.find()) {
			sb.append(s.substring(i, m.start()));
			try {
				v = getValue(o, m.group(1));
				sb.append(v);
			} catch (Exception e) {
				sb.append(m.group(0));
			}
			i = m.start() + m.group(0).length();
		}
		sb.append(s.substring(i));
		return sb.toString();
	}

	/**
	 * 获取一天划分成份对应中文名称
	 * 
	 * @return
	 */
	public static String getDayPart() {
		Calendar c = Calendar.getInstance();
		int h = c.get(Calendar.HOUR_OF_DAY);
		if (h >= 20)
			return "晚上";
		else if (h >= 13)
			return "下午";
		else if (h >= 11)
			return "中午";
		else
			return "上午";
	}

	/**
	 * 判断字符串是否为有效数值，是则返回true，否则为false
	 * 
	 * @param page
	 *            被检查字符串
	 * @param checkFloat
	 *            判断是否包含小数点
	 * @return
	 */
	public static boolean isNumeric(String page, boolean checkFloat) {
		if (checkFloat) {
			return page != null && page.matches("^-?\\d+(\\.\\d+)?$");
		} else {
			return page != null && page.matches("^-?\\d+$");
		}
	}

	/**
	 * 判断字符串是否为纯数字组成，是则返回true，否则为false
	 * 
	 * @param page
	 *            被检查字符串
	 * @return
	 */
	public static boolean isNumeric(String page) {
		return isNumeric(page, false);
	}

	/**
	 * 将List以split串为分隔符合并成新的字符串
	 * 
	 * @param l
	 * @param split
	 * @return
	 */
	public static String join(List<?> l, String split) {
		StringBuffer sb = new StringBuffer();
		for (Object s : l) {
			sb.append(s).append(split);
		}
		if (sb.length() > 0)
			sb.delete(sb.length() - split.length(), sb.length());
		return sb.toString();
	}

	/**
	 * 将Object数组以split串为分隔符合并成新的字符串。
	 * 
	 * @param l
	 * @param split
	 * @return
	 */
	public static String join(Object[] l, String split) {
		StringBuffer sb = new StringBuffer();
		for (Object s : l) {
			sb.append(s).append(split);
		}
		if (sb.length() > 0)
			sb.delete(sb.length() - split.length(), sb.length());
		return sb.toString();
	}

	/**
	 * 将一个数值类型字符串按指定的进阶数和单位输出，如：1025输出成 1KB可以这样调用
	 * getBaseNum("1024","1024","KB,B")
	 * 
	 * @param v
	 * @param base
	 * @param unit
	 * @return
	 */
	public static String getBaseNum(String v, String base, String unit) {
		if (isNumeric(v, true) && isNumeric(base) && !isEmpty(unit)) {
			StringBuffer sb = new StringBuffer();
			long bv = new BigDecimal(v).setScale(0, BigDecimal.ROUND_DOWN)
					.longValue();
			String[] us = unit.split(",");
			int p = Integer.parseInt(base, 10);
			for (int i = 1; i <= us.length; i++) {
				long mod = bv % p;
				if (mod > 0) {
					sb.insert(0, us[i - 1]).insert(0, mod);
				}
				if ((bv = bv / p) <= 0)
					break;
			}
			return sb.toString();
		}
		return v;
	}
	public static String getPatternString(String source, String pattern, int index){
		if(StringUtil.isEmpty(source))return "";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		while (m.find()) {
			return m.group(index);
		}
		return "";
	}
	/**
	 * 简化方法获取XML的标签值
	 * @param source
	 * @param tag
	 * @return
	 */
	public static String getTagText(String source, String tag) {
		return getPatternString(source, "<" + tag + ">(?:<!\\[CDATA\\[)?(.*?)(\\]\\]>)*</" + tag + ">", 1);
	}
	public static Map<String, String> queryToMap(String qp){
		HashMap<String, String> k=new HashMap<String, String>();
		if(!isEmpty(qp)){
			Pattern p = Pattern.compile("([^&]+?)=([^&]*)");
			Matcher m = p.matcher(qp);
			while(m.find()){
				k.put(m.group(1), m.group(2));
			}
		}
		return k;
	}
	public static String getHighStep(BigDecimal v) {
		return getHighStep(v, 0);
	}

	public static String getHighStep(BigDecimal v, int scale) {
		BigDecimal wan = new BigDecimal("10000"), yi = new BigDecimal(
				"100000000");
		String step = "";
		if (yi.compareTo(v) <= 0) {
			v = v.divide(yi, scale, BigDecimal.ROUND_HALF_UP);
			step = "亿";
		} else if (wan.compareTo(v) <= 0) {
			v = v.divide(wan, scale, BigDecimal.ROUND_HALF_UP);
			step = "万";
		}
		return v.setScale(scale, BigDecimal.ROUND_HALF_UP) + step;
	}

	/**
	 * 返回字符串对应的数值，当无效字符串或null时，返回默认字符串指定的数值
	 * 
	 * @param vs
	 * @param defa
	 * @return
	 */
	public static int getNumberDefault(String vs, String defa) {
		int def = 0;
		int v = 0;
		try {
			def = Integer.parseInt(defa, 10);
			v = Integer.parseInt(vs, 10);
		} catch (Exception e) {
			return def;
		}
		return v;
	}

	/**
	 * 取字符串左边指定长度子串
	 * 
	 * @param s
	 *            源字符串
	 * @param len
	 *            指定长度
	 * @return
	 */
	public static String left(String s, int len) {
		return s == null ? null : s.substring(0, len);
	}
	public static String leftPad(long n, int len){
		return right(ZERO_STR+n, len);
	}

	/**
	 * 取字符串右边指定长度子串
	 * 
	 * @param s
	 *            源字符串
	 * @param len
	 *            指定长度
	 * @return
	 */
	public static String right(String s, int len) {
		return s == null ? null : s.substring(s.length() - len, s.length());
	}
	public static Map<String,String> toStringMap(Map<String,String[]> m){
		HashMap<String, String> map = new HashMap<String, String>();
		for(String k:m.keySet()){
			String[]v = m.get(k);
			map.put(k, v==null||v.length<=0?"":getDefault(v[0], ""));
			
		}
		return map;
	}
	public static Map<String,String> toStringMap(MultiValueMap<String,String>m){
		HashMap<String, String> map = new HashMap<String, String>();
		for(String k:m.keySet()){
			map.put(k, getDefault(m.getFirst(k), ""));
		}
		return map;
	}
	public static Map<String,String[]> toArrayMap(Map<String,String> m){
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		for(String k:m.keySet()){
			map.put(k, new String[]{getDefault(m.get(k), "")});
		}
		return map;
	}
	public static String HTMLEncode(String javaS){
		if(javaS==null)return "";
		return javaS.replaceAll("&","&amp;").replaceAll("\"","&quot;")
			.replaceAll("\'","&acute;").replaceAll("<","&lt;")
			.replaceAll(">","&gt;").replaceAll("\r|\n","<br/>");
	}
	public static String HTMLDecode(String javaS){
		if(javaS==null)return "";
		return javaS.replaceAll("<br>","\n").replaceAll("&quot;","\"")
			.replaceAll("&acute;","'").replaceAll("&lt;","<").replaceAll("&gt;",">")
			.replaceAll("&amp;","&");
	}
	public static String toJSString(String javaS){
		if(javaS==null)return "";
		StringBuffer sb = new StringBuffer("");
		for(int i=0;i<javaS.length();i++){
			char c = javaS.charAt(i);
			switch(c){
			case '\n' :
			case '\r' :
				sb.append("\\n");
				break;
			case '\'' :
			case '\"' :
			case '\\' :
				sb.append("\\");
			default :
				sb.append(c);
			}
		}
		return sb.toString();
	}
	public static String byteToString(byte[] digest) {
        String str = "";
        String tempStr = "";
        for (int i = 1; i < digest.length; i++) { 
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) { 
                str = str + "0" + tempStr; 
            } 
            else { 
                str = str + tempStr; 
            } 
        } 
        return str.toLowerCase(); 
    }
	public static boolean in(String source, String find, String splitBy){
		if(source==null)return false;
		String[] ss = source.split(splitBy);
		for(String f:ss){
			if(f.equals(find))return true;
		}
		return false;
	}
	public static boolean in(String source, String find){
		return in(source,find,",");
	}
	public static boolean isChinese(String s){
		return s!=null&&s.matches("^[\u4e00-\u9fa5]+$");
	}
	public static boolean isTrue(String v) {
		return "true".equalsIgnoreCase(v)||"1".equals(v);
	}
	public static String toJSON(Map<String, Object> map) {
		return toJSON("yyyy-MM-dd HH:mm:ss",map);
	}
	public static String toJSON(String pattern, Map<String, Object> map) {
		return new GsonBuilder().setDateFormat(pattern).create().toJson(map);
	}
	public static String mask(String s) {
		if(isEmpty(s) || s.length()==1)return s;
		if(s.length()==2)return s.replaceAll("(.).*", "$1*");
		else if(s.length()==3)return s.replaceAll("(.).(.)", "$1*$2");
		else
			return s.replaceAll(
					"(.).{" + (s.length() - 3) + "}(.{2})",
					String.format("$1%s$2",
							StringUtil.left(maskS, s.length() - 3)));
	}
	public static String filterEmojiChar(String s) {
		return StringUtil.isEmpty(s)?s:s.replaceAll("([\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff])*", "");
	}
	
	/**
	 * 将map转成queryString
	 * @param map
	 * @return
	 */
	public static String mapToQueryString(Map<String, String> map){
		StringBuffer sb = new StringBuffer();
		for(String k:map.keySet()){
			sb.append(k).append("=").append(map.get(k)).append("&");
		}
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	
	public static boolean in(String source, BigDecimal find, String splitBy ){
		if(source==null)return false;
		String[] ss = source.split(splitBy);
		BigDecimal[] b = new BigDecimal[ss.length];
		for (int i = 0; i < ss.length; i++) {
			b[i] = new BigDecimal(ss[i]);
			if(b[i].compareTo(find)==0)
				return true;
		}
		return false;
	}
	
	public static boolean in(String source, BigDecimal find){
		return in(source, find, ",");
	}
	
	public static String maskTelephone(String str) {
		return str==null?str:str.replaceAll("(1[3-9][0-9])\\d{4}(\\d{4})", "$1****$2");
	}
	public static String maskBankCardNo(String s){
		String mark = "**** **** **** ";
		if(!isEmpty(s))
			return mark+s.substring(s.length()-4,s.length());
		else 
		return s;
	}
	
	public static String maskIDcard(String s){
		if (!isEmpty(s)) {
			return s.replaceAll(
					"(.{0}).{" + (s.length() - 4) + "}(.{4})",
					String.format("$1%s$2",
							StringUtil.left(maskS, s.length() - 4)));
		} else {
			return s;
		}
	}
	public static String getMaskPassword(String pwd){
		return MD5.encode(maskCode+MD5.encode(pwd+maskCode));
	}
	/**
	 * 计算出数值点所在的数值区间对应的值
	 * @param exp 格式：flag:area1,area2,area3:value1,value2,value3,value4
	 * 				flag:0|1,0为向下包含，1为向上包含
	 * @param point
	 * @return
	 */
	public static BigDecimal spitAreaValue(String exp, BigDecimal point) {
		if (isEmpty(exp) || !exp.matches("[01]:[0-9.,]+:[0-9.,]+"))
			throw new IllegalArgumentException(
					String.format(
							"表达式格式应为flag:area1,area2,area3:value1,value2,value3,value4[%s]",
							exp));
		String[] arg = exp.split(":");
		if (arg.length < 3) {
			throw new IllegalArgumentException(
					String.format(
							"表达式格式应为flag:area1,area2,area3:value1,value2,value3,value4[%s]",
							exp));
		}
		int flag = StringUtil.getNumberDefault(arg[0], "1");
		String[] areas = arg[1].split(","), values = arg[2].split(",");

		// values个数areas个数+1
		if (areas.length != values.length - 1)
			throw new IllegalArgumentException(
					String.format(
							"表达式格式应为flag:area1,area2,area3:value1,value2,value3,value4[%s]",
							exp));

		for (int i = 0; i < areas.length; i++) {
			BigDecimal area = new BigDecimal(areas[i]);
			if (point.compareTo(area) < 0
					|| (flag == 1 && point.compareTo(area) == 0)) {
				return new BigDecimal(values[i]);
			}
		}
		return new BigDecimal(values[values.length - 1]);
	}

}
