package com.saltpay.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    public static final String PREFIX_TABLE = "user_";

    @Id
    @Column(name = PREFIX_TABLE + "id")
    private Long id;

    @NotNull
    @NotBlank(message = "Name is mandatory")
    @Column(name = PREFIX_TABLE + "name")
    private String name;
}
