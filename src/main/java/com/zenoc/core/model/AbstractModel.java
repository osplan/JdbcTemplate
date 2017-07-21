package com.zenoc.core.model;

import com.zenoc.core.interfaces.DbMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractModel<T> implements RowMapper<T> {
	
	public abstract T getInstance();
	
	public String getSql(){
		return getSql("*");
	}
	public String getSql(String selectors){
		return DbMapper.getSql(getClass(), selectors);
	}
	@Override
	public T mapRow(ResultSet rs, int rowIndex) throws SQLException {
		T o = getInstance();
		try {
			return DbMapper.dbToBean(rs, rowIndex, o);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}
	
}
