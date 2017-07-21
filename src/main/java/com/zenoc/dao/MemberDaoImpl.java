package com.zenoc.dao;

import com.zenoc.core.dao.AbstractBaseDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by Ty on 2017/7/21.
 */
@Repository
public class MemberDaoImpl extends AbstractBaseDaoImpl{

    @Resource
    JdbcTemplate jdbcTemplate;
    @Override
    protected JdbcTemplate getDS() {
        return jdbcTemplate;
    }
}
