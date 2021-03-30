package com.saltpay.bank.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Account {

    public static final String PREFIX_TABLE = "account_";

    @Id
    @Column(name = PREFIX_TABLE + "id")
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = PREFIX_TABLE + "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = PREFIX_TABLE + "id_user")
    private User user;
}
