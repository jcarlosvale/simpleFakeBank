package com.saltpay.bank.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Operation {

    public static final String PREFIX_TABLE = "operation_";

    @Id
    @Column(name = PREFIX_TABLE + "id")
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = PREFIX_TABLE + "id_sender")
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = PREFIX_TABLE + "id_receiver")
    private Account receiverAccount;

    @NotNull
    @Column(name = PREFIX_TABLE + "value")
    private BigDecimal value;

    @NotNull
    @Column(name = PREFIX_TABLE + "date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

}
