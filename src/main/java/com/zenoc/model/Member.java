package com.zenoc.model;

import com.zenoc.core.interfaces.linkTable;
import com.zenoc.core.model.AbstractModel;

/**
 * Created by Ty on 2017/7/21.
 */
public class Member extends AbstractModel<Member>{
    private Long id;
    private String name;
    private int age;
    @linkTable(linkField = "id", to="id")
    private Account account;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public Member getInstance() {
        return new Member();
    }
}
