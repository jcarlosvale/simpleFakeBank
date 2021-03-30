package com.saltpay.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    public static final String PREFIX_TABLE = "account_";

    @Id
    @Column(name = PREFIX_TABLE + "id")
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = PREFIX_TABLE + "initial_deposit_amount")
    private BigDecimal initialDepositAmount;

    @NotNull
    @Column(name = PREFIX_TABLE + "creation_timestamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime creationTimestamp;

    @NotNull
    @Column(name = PREFIX_TABLE + "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = PREFIX_TABLE + "id_user")
    private User user;
}
