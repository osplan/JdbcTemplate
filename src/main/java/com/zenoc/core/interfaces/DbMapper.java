package com.zenoc.core.interfaces;


import com.zenoc.util.StringUtil;
import org.apache.ibatis.ognl.Ognl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class DbMapper {
	private static Logger logger = LoggerFactory.getLogger(DbMapper.class);
	
	public static String getTableName(Class<?> c){
		String[] ns = c.getName().split("\\.");
		String n = ns[ns.length-1];
		mapTable mt = (mapTable) c.getAnnotation(mapTable.class);
		if(mt!=null){
			n = mt.name();
		}
		return n.toLowerCase();
	}
	public static String getKeyField(Class<?> c){
//		Field[] fss = c.getDeclaredFields();
		List<Field>fss = getFields(c);
		for(Field f:fss){
			key k = f.getAnnotation(key.class);
			if(k!=null){
				return getDbField(f);
			}
		}
		try {
			Field f = c.getDeclaredField("id");
			return getDbField(f);
		} catch (Exception e){
//			e.printStackTrace();
			logger.warn("NO KEYFIELD FOUND IN "+c.toString());
		}//未指定，找id，如果有则返回，如果没有，则无主键
		return "";
	}
	public static String getDbField(Class<?> c,String f){
		try {
			return getDbField(c.getDeclaredField(f));
		} catch (Exception e){
//			e.printStackTrace();
			logger.warn(f+" NOT FOUND IN "+c.toString());
		}
		return "";
	}
	public static String getDbField(Field f){
		mapColumn mc = f.getAnnotation(mapColumn.class);
		return mc!=null? StringUtil.getDefault(mc.name(), f.getName()):f.getName();
	}
	public static List<String> getFields(Class<?> c,String subfix){
		List<String> fs = new ArrayList<String>();
//		Field[] fss = c.getDeclaredFields();
		List<Field>fss = getFields(c);
		for(Field f : fss){
			linkTable lt = f.getAnnotation(linkTable.class);
			if(lt!=null)continue; //
			mapColumn mc = f.getAnnotation(mapColumn.class);
			String pn = mc!=null?StringUtil.getDefault(mc.name(), f.getName()):f.getName();
			fs.add(subfix+"."+pn);
		}
		return fs;
	}
	public static List<Field> getFields(Class<?> c){
		return getFields(c, false);
	}
	public static List<Field> getFields(Class<?> c, boolean filte){
		List<Field> fs = new ArrayList<Field>();
		Field[] fss = c.getDeclaredFields();
		for(Field f : fss){
			if(filte){
				linkTable lt = f.getAnnotation(linkTable.class);
				if(lt!=null)continue; //
			}
			unColumn un = f.getAnnotation(unColumn.class);
			if(un==null)
				fs.add(f);
		}
		if(!c.getSuperclass().equals(Object.class)) {
			fs.addAll(getFields(c.getSuperclass()));
		}
		return fs;
	}
	public static Object getKeyValue(Object o){
//		Field[] fss = o.getClass().getDeclaredFields();
		List<Field>fss = getFields(o.getClass());
		for(Field f:fss){
			key k = f.getAnnotation(key.class);
			if(k!=null){
				return getValue(o, f);
			}
		}
		try {
			Field f = o.getClass().getDeclaredField("id");
			return getValue(o, f);
		} catch (Exception e){
//			e.printStackTrace();
			logger.warn("id NOT FOUND IN "+o.getClass().toString());
		}//未指定，找id，如果有则返回，如果没有，则无主键
		return null;
	}
	public static Object getValue(Object o,Field f){
		f.setAccessible(true);
		try {
			return f.get(o);
		} catch (Exception e) {
//			e.printStackTrace();
			logger.warn("getValue called error:"+e.getMessage());
		}
		return null;
	}
	public static Object getValue(Object o,String p){
		try {
			Field f = o.getClass().getDeclaredField(p);
			return getValue(o,f);
		} catch (Exception e) {
//			e.printStackTrace();
			logger.warn("getValue called error:"+e.getMessage());
		}
		return null;
	}
	public static Object getOgnlValue(Object rm, String p) {
		Object r = null;
		try{
			r = Ognl.getValue(p, rm);
		}catch(Exception e){}
		return r;
	}
	public static boolean hasSelector(List<String> selectors,String selector){
		selector = selector.endsWith(".")?selector:selector+".";
		for(String s : selectors){
			if(s.startsWith(selector))return true;
		}
		return false;
	}
	private static boolean getSql(Class<?> c,StringBuffer fields,StringBuffer tables,String root,List<String> selectors){
		boolean result = false;
		String prefix = "",trn = "",frn = "";
		if(StringUtil.isEmpty(root)){
			fields.append("select ");
			tables.append(" from ").append(trn = getTableName(c));
			trn = trn + ".";
		}else{
			prefix = root + ".";
			trn = root.replaceAll("\\.", "_")+".";
			frn = trn;
		}
		
//		Field[] fs = c.getDeclaredFields();
		List<Field>fs = getFields(c);
		boolean found = selectors.contains(prefix+"*");
		for(Field f : fs){
			String fn = f.getName();
			linkTable lt = f.getAnnotation(linkTable.class);
			
			
			if(lt==null){//自有属性
				mapColumn mc = f.getAnnotation(mapColumn.class);
				String pn = mc!=null?StringUtil.getDefault(mc.name(), f.getName()):f.getName();
				if(found || selectors.contains(prefix+fn)){//root如：status.name则为  status
					fields.append(trn).append(pn).append(" as '").append(frn).append(pn).append("',");
					result = true;
				}
			}else{//连接属性
				String nroot = prefix + getDbField(f),ntrn = nroot.replaceAll("\\.", "_");
				if(hasSelector(selectors,nroot)){
					tables.append(" left join ").append(getTableName(f.getType())).append(" as ").append(ntrn)
						.append(" on ").append(trn).append(lt.linkField()).append("=").append(ntrn).append(".").append(lt.to());
					getSql(f.getType(), fields, tables, nroot, selectors);
				}
			}
			
		}
		return result;
	}
	public static String getSql(Class<?> c,String selectors){
		StringBuffer fields = new StringBuffer(),tables = new StringBuffer();
		DbMapper.getSql(c,fields,tables,"",StringUtil.splitToList(selectors,","));
		if(fields.charAt(fields.length()-1)==',')fields.deleteCharAt(fields.length()-1);
		fields.append(tables);		
		return fields.toString();
	}
	
	public static <T>StringBuffer getCondition(List<Object>args, Class<?>c, List<String> pns, List<Object> vs, List<String>exp){
		StringBuffer sql = new StringBuffer();
		for(int i=0;i<pns.size() && i<vs.size();i++){
			if(exp!=null && "in".equalsIgnoreCase(exp.get(i))){
				sql.append(" and ").append(DbMapper.getRename(c,pns.get(i))).append(" in(");
				Object v = vs.get(i);
				List<String> ps = new ArrayList<String>();
				if(v==null){
					args.add(getArgValue(null));
					ps.add("?");
				}else if(v.getClass().isArray()){
					for(Object iv:(Object[])v){
						args.add(getArgValue(iv));
						ps.add("?");
					}
				}else if(v instanceof List<?>){
					for(Object iv:(List<?>)v){
						args.add(getArgValue(iv));
						ps.add("?");
					}
				}else{
					args.add(getArgValue(v));
					ps.add("?");
				}
				sql.append(StringUtil.join(ps, ","));
				sql.append(")");
			}else if(exp!=null && exp.get(i).toLowerCase().startsWith("is")){
				sql.append(" and ").append(DbMapper.getRename(c,pns.get(i)))
						.append(" ").append(exp.get(i).toLowerCase()).append(" NULL");
			}else if(exp!=null && exp.get(i).toLowerCase().startsWith("like")){
				sql.append(" and ").append(DbMapper.getRename(c,pns.get(i)))
					.append(" ").append(exp.get(i).toLowerCase()).append(" CONCAT('%',?,'%')");
				args.add(getArgValue(vs.get(i)));
			}else{
				sql.append(" and ").append(DbMapper.getRename(c,pns.get(i)))
						.append(" ").append(exp==null||i>=exp.size()?"=":exp.get(i)).append("(?)");//exp不传默认使用 =
				args.add(getArgValue(vs.get(i)));
			}
		}
		if(sql.length()>0){
			sql.delete(0, " and ".length());
			sql.insert(0, " where ");
		}
		return sql;
	}
	
	public static <T> T dbToBean(ResultSet rs, int ri, T o)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SQLException {
		List<String> fns = getDbFields(rs);
		return dbToBean(rs, ri, o, fns, "");
	}
	public static <T> T dbToBean(ResultSet rs, int ri, T o, String fns)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SQLException {
		return dbToBean(rs, ri, o, StringUtil.splitToList(fns,","),"");
	}
	public static <T> T dbToBean(ResultSet rs, int ri, T o,List<String> fns,String prefix)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SQLException {

		//T o = c.newInstance();
		Class<?> c = o.getClass();
		if(rs!=null){
//			Field[] fs = c.getDeclaredFields();
			List<Field> fs = getFields(c);
			for (Field f : fs) {
				String pn = getDbField(f);
				linkTable lt = f.getAnnotation(linkTable.class);
				if(lt==null){
					if(fns.contains(prefix+pn)){
						setValue(o, f, rs.getObject(prefix+pn));
					}
				}else{
					//String nprefix = (StringUtil.isEmpty(prefix)?pn:prefix+pn)+".";//生成select SQL时使用的是 table rename + 字段名
					String nprefix = (StringUtil.isEmpty(prefix)?pn:prefix+pn).replaceAll("\\.", "_")+".";//生成select SQL时使用的是 table rename + 字段名
					if(hasSelector(fns, nprefix)){
						f.setAccessible(true);
						f.set(o, f.getType().cast(dbToBean(rs, ri, f.getType().newInstance(),fns,nprefix)));
					}
				}
			}
		}
		return o;
	}
	private static <T>void setValue(T o, Field f, Object v) 
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SQLException {
		f.setAccessible(true);
		if(v==null && isBaseType(f.getType())){
			return;
		}
		if(f.getType().isEnum()){
			f.set(o, getEnum(f.getType(), String.valueOf(v)));
		}else{
			f.set(o, v);
		}
	}
	@SuppressWarnings({ "rawtypes" })
	private static Object getArgValue(Object v){
		if(v==null)return v;
		if(v.getClass().isEnum()){
			return ((Enum)v).ordinal();
		}
		return v;
	}
	public static Object getEnum(Class<?> type, String v){
		Method m;
		if(v==null)return null;
		try {
			m = type.getClass().getDeclaredMethod("enumConstantDirectory");
		
			if(m!=null){
				m.setAccessible(true);
				Map<String, Enum>map =  (Map<String, Enum>)m.invoke(type);
//				for(String k:map.keySet()){
				for(Map.Entry<String, Enum> k:map.entrySet()){
//					System.out.println(String.format("%s->%s", k,map.get(k).ordinal()));
					if(v.equalsIgnoreCase(StringUtil.isNumeric(v)?String.valueOf(k.getValue().ordinal()):k.getKey()))
						return k.getValue();
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return null;
		}
		return null;
	}
	private static boolean isEnum(Class<?> type){
		return type.isEnum(); 
	}
	private static boolean isBaseType(Class<?> type) {
		return type==int.class || type==float.class || type==double.class || type==char.class || type==boolean.class;
	}
	public static List<String> getDbFields(ResultSet rs){
		List<String> l = new ArrayList<String>();
		if(rs!=null){
			try {
				ResultSetMetaData rsmd = rs.getMetaData();
				for(int i=1;i<=rsmd.getColumnCount();i++){
					l.add(rsmd.getColumnLabel(i));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return l;
	}

	public static String getRename(Class<?> c,String pn) {
		StringBuffer sb = new StringBuffer();
		if(pn.indexOf(".")<0)return sb.append(getTableName(c)).append(".").append(pn).toString();
		int i = pn.lastIndexOf(".");
		return sb.append(pn.substring(0, i).replaceAll("\\.", "_")).append(pn.substring(i)).toString();
//		return (pn.indexOf(".")>=0?"":getTableName(c)+".")+pn.replaceAll("\\.", "_").replaceAll("_([^_]*?$)", ".$1");
	}
	
	
}
