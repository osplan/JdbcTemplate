package com.zenoc.service;

import com.zenoc.dao.MemberDaoImpl;
import com.zenoc.model.Account;
import com.zenoc.model.Member;
import com.zenoc.util.DatabaseUtil;
import com.zenoc.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ty on 2017/7/21.
 */
@Service
public class MemberService {
    @Autowired
    MemberDaoImpl memberDaoImpl;


//  单表操作
    public Member get(Long memberId){
        return memberDaoImpl.getObjectById(memberId,new Member());
    }

    public Member save(String name, int age){
        Member member = new Member();
        member.setId(DatabaseUtil.getDatabasePriykey());
        member.setName(name);
        member.setAge(age);
//      初始化资产信息
        save(member);
        return memberDaoImpl.addObject(member);
    }

    public int modify(Long memberId, int age){
        Member member = new Member();
        member.setId(memberId);
        member.setAge(age);
        return memberDaoImpl.updateObject(member,"age");
    }

//   表连接查询

    public Member getBalance(Long memberId){
        return memberDaoImpl.getObjectById(memberId,"*,account.*",new Member(), false);
    }

//  条件查询，及其分页信息

    public Pagination<Member> get(String name, int page, int pageSize){
//      数据库字段
        List<String> pns = new ArrayList<>();
//      对应的值
        List<Object> vs = new ArrayList<>();
//      表达式 全是=关系可以不行写，可使用（“=”，“>”，“<”，“<>”，“in”）
        List<String> exp = new ArrayList<>();
        pns.add("name"); exp.add("="); vs.add(name);
        int count = memberDaoImpl.getCount("id",pns,vs,exp,new Member());
        Pagination<Member> pagination = new Pagination<>(count,pageSize);
        pagination.setCurrentPage(page);
        pagination.setResultList(memberDaoImpl.getListByProperty(pns,vs,exp,pagination.getStart(),pageSize,new Member(),false));
        return pagination;
    }


//  获取列表
    public List<Member> get(){
    return memberDaoImpl.getListByProperty("*,account.*",new ArrayList<String>(),new ArrayList<>(),null,0,9999,"id",new Member(),false);
    }



    public Account save(Member member){
        Account account = new Account();
        account.setId(member.getId());
        account.setTotal(new BigDecimal("100000"));
        account.setBalance(new BigDecimal("50"));

        return memberDaoImpl.addObject(account);
    }
}
