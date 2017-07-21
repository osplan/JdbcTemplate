package com.zenoc.model;

import com.zenoc.core.model.AbstractModel;

import java.math.BigDecimal;

/**
 * Created by Ty on 2017/7/21.
 */
public class Account extends AbstractModel<Account>{
    private Long id;
    private BigDecimal total;
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public Account getInstance() {
        return new Account();
    }
}
