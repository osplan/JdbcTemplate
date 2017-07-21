package com.zenoc.core.dao;

import com.zenoc.core.interfaces.DbMapper;
import com.zenoc.core.interfaces.linkTable;
import com.zenoc.core.interfaces.unColumn;
import com.zenoc.core.model.AbstractModel;
import com.zenoc.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractBaseDaoImpl {
	private final static Logger logger = LoggerFactory.getLogger(AbstractBaseDaoImpl.class);
	
	protected abstract JdbcTemplate getDS();
	
	public <T> T getObjectByProperty(String selectors, List<String> pns, List<Object> vs, List<String> exp, AbstractModel<T> o, boolean update){
		List<T> l = getListByProperty(selectors, pns, vs, exp, 0, 1, null, o,update);
		return l.size()>0?l.get(0):null;
	}
	public <T> T getObjectByProperty(String selectors,String pn,Object v, AbstractModel<T> o,boolean update){
		List<String>pns = new ArrayList<String>();
		List<Object>vs = new ArrayList<Object>();
		pns.add(pn);vs.add(v);
		return getObjectByProperty(selectors, pns, vs, null, o,update);
	}
	public <T> T getObjectByProperty(String pn,Object v, AbstractModel<T> o,boolean update){
		return getObjectByProperty("*", pn, v, o,update);
	}
	public <T> T getObjectByProperty(String pn,Object v, AbstractModel<T> o){
		return getObjectByProperty(pn, v, o,false);
	}
	public <T> T getObjectById(Long id, String selectors, AbstractModel<T> o,boolean update){
		return getObjectByProperty(selectors, "id", id, o,update);
	}
	public <T> T getObjectById(Long id, String selectors, AbstractModel<T> o){
		return getObjectById(id, selectors, o, false);
	}
	public <T> T getObjectById(Long id, AbstractModel<T> o){
		return getObjectById(id, "*", o);
	}
	public <T> int getCount(String sql,String columnName,List<Object> args){
		logger.debug(sql);
		List<Map<String,Object>> list = null;//new ArrayList<Map<String,Object>>();
		if(args==null||args.size()<=0)list = getDS().queryForList(sql);
		else list = getDS().queryForList(sql.toString(),args.toArray());
		return list.size()<=0?0:Integer.parseInt(String.valueOf(list.get(0).get(columnName)));
	}
	public <T> int getCount(String countColumn,List<String> pns,List<Object> vs,List<String> exp,AbstractModel<T> o){
		return getCount(countColumn, "*", pns, vs, exp, o);
	}
	public <T> int getCount(String countColumn,String selectors,List<String> pns,List<Object> vs,List<String> exp,AbstractModel<T> o){
		StringBuffer sql = new StringBuffer();
		String ssql = o.getSql(selectors);
		sql.append("select count(").append(DbMapper.getRename(o.getClass(),countColumn)).append(") as ").append(countColumn).append(" from ")
			.append(ssql.replaceAll("^select .*? from ", ""));//.append(" where 1=1");

		List<Object> args = new ArrayList<Object>();
		sql.append(DbMapper.getCondition(args, o.getClass(), pns, vs, exp));

		logger.debug(sql.toString());
		List<Map<String,Object>> list = getDS().queryForList(sql.toString(),args.toArray());
		return list.size()<=0?0:Integer.parseInt(String.valueOf(list.get(0).get(countColumn)));
	}
	public <T> List<T> getListBySql(String sql,AbstractModel<T> o){
		return getListBySql(sql,null,o);
	}
	public <T> List<T> getListBySql(String sql,List<Object>args,AbstractModel<T> o){
		logger.debug(sql);
		return args==null||args.size()<=0?getDS().query(sql, o):getDS().query(sql, args.toArray(), o);
	}
	public <T> List<T> getListByProperty(String selectors, List<String> pns,List<Object> vs,List<String> exp,int offset,int pageSize,String orderBy,AbstractModel<T> o, boolean update){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		//sql.append("select * from ").append(DbMapper.getTableName(o.getClass())).append(" where 1=1");
		sql.append(o.getSql(selectors));//.append(" where 1=1");
		sql.append(DbMapper.getCondition(args, o.getClass(), pns, vs, exp));
		if(!StringUtil.isEmpty(orderBy)){
			sql.append(" order by ").append(orderBy);
		}
		sql.append(" limit ?,? ");
		if(update)sql.append(" for update");
		args.add(offset);args.add(pageSize);
		logger.debug(sql.toString());
		return getDS().query(sql.toString(), args.toArray(), o);
	}
	public <T> List<T> getListByProperty(List<String> pns,List<Object> vs,List<String> exp,int offset,int pageSize,AbstractModel<T> o,boolean update){
		return getListByProperty("*", pns, vs, exp, offset, pageSize,null, o,update);
	}
	public <T> List<T> getListByProperty(String pn, Object v,int offset,int pageSize,AbstractModel<T> o,boolean update){
		String sql = "select * from "+DbMapper.getTableName(o.getClass())+" where "+pn+"=?"+(update?" for update":"");
		logger.debug(sql);
		return getDS().query(sql, new Object[]{v}, o);
	}
	
	public <T>T addObject(T o){
		StringBuffer sb = new StringBuffer("insert into ").append(DbMapper.getTableName(o.getClass())).append("("),vs = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		List<Field> fs = DbMapper.getFields(o.getClass());//o.getClass().getDeclaredFields();
		for(Field f:fs){
			linkTable lt = f.getAnnotation(linkTable.class);
			if(lt==null){//非连接属性
				f.setAccessible(true);
				try {
					Object v = f.get(o);
					if(v!=null){//只对非null字段进行保存
						sb.append(DbMapper.getDbField(f)).append(",");
						vs.append("?,");
						if(v.getClass().isEnum()){
							args.add(((Enum)v).ordinal());
						}
						else args.add(v);
					}
				} catch (Exception e){
					logger.error("属性："+f.getName()+"["+DbMapper.getDbField(f)+"]取值出错!");
				}
			}
		}
		
		if(sb.charAt(sb.length()-1)==',') sb.deleteCharAt(sb.length()-1);
		if(vs.charAt(vs.length()-1)==',') vs.deleteCharAt(vs.length()-1);
		sb.append(") values(").append(vs).append(")");
		logger.debug(sb.toString());
		int r = getDS().update(sb.toString(),args.toArray());
		if(r>0)return o;
		else return null;
	}
	
	public <T> int delObject(T o){
		StringBuffer sb = new StringBuffer("delete from ").append(DbMapper.getTableName(o.getClass()))
							.append(" where ").append(DbMapper.getKeyField(o.getClass())).append("=?");
		logger.debug(sb.toString());
		return getDS().update(sb.toString(), new Object[]{
			DbMapper.getKeyValue(o)
		});
	}
	public <T> int delObject(T o,List<String>pns,List<Object>vs,List<String> exp){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("delete from ").append(DbMapper.getTableName(o.getClass()));//.append(" where ");
		sql.append(DbMapper.getCondition(args, o.getClass(), pns, vs, exp));
		logger.debug(sql.toString());
		return getDS().update(sql.toString(), args.toArray());
	}
	public int delObject(String sql,List<Object> args){
		logger.debug(sql);
		return getDS().update(sql, args.toArray()); 
	}
	public <T> int updateObject(T o){
		return updateObject(o, "*");
	}
	public <T> int updateObject(T o,String selectors){
		return updateObject(o, selectors, null, null, null);
	}
	public <T> int updateObject(T o,String selectors,List<String>pns,List<Object>vs,List<String> exp){
		StringBuffer sql = new StringBuffer("update ").append(DbMapper.getTableName(o.getClass())).append(" set ");
		List<String>fs = StringUtil.splitToList(selectors, ",");
		List<Object>args = new ArrayList<Object>();
		boolean toUpdateAll = selectors.equals("*");
		for(Field f : o.getClass().getDeclaredFields()){
			if(toUpdateAll || fs.contains(f.getName())){
				unColumn uncolumn = f.getAnnotation(unColumn.class);
				if(uncolumn!=null){
					continue;
				}
				linkTable l = f.getAnnotation(linkTable.class);
				if(l==null){
					sql.append(DbMapper.getDbField(f)).append("=?,");
					args.add(DbMapper.getValue(o, f));
				}
			}
		}
		if(sql.charAt(sql.length()-1)==',') sql.deleteCharAt(sql.length()-1);
		if(pns==null || pns.size()<=0){//防止因开发问题导致全表删除
			sql.append(" where ").append(DbMapper.getKeyField(o.getClass())).append("=?");
			args.add(DbMapper.getKeyValue(o));
		}else{
			sql.append(DbMapper.getCondition(args, o.getClass(), pns, vs, exp));
		}
		return updateObject(sql.toString(),args);//getDS().update(sql.toString(), args.toArray()); 
	}
	public int updateObject(String sql,List<Object> args){
		logger.debug(sql.toString());
		return getDS().update(sql, args.toArray()); 
	}
	public <T>BigDecimal sum(String sumColumn,String selectors,List<String> pns,List<Object> vs,List<String> exp,AbstractModel<T> o){
		StringBuffer sql = new StringBuffer();
		String ssql = o.getSql(selectors);
		sql.append("select sum(").append(DbMapper.getRename(o.getClass(),sumColumn)).append(")");
		if(sumColumn.indexOf("[\\+\\-\\*\\/\\(]")>0) sumColumn = "_sumColumn";
		sql.append(" as ").append(sumColumn).append(" from ")
			.append(ssql.replaceAll("^select .*? from ", ""));//.append(" where 1=1");
		List<Object> args = new ArrayList<Object>();
		sql.append(DbMapper.getCondition(args, o.getClass(), pns, vs, exp));
		logger.debug(sql.toString());
		List<Map<String,Object>> list = getDS().queryForList(sql.toString(),args.toArray());
		return list.size()<=0||list.get(0).get(sumColumn)==null?BigDecimal.ZERO:new BigDecimal(String.valueOf(list.get(0).get(sumColumn)));
	}
}
