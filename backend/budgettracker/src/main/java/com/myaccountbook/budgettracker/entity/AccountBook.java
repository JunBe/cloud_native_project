package com.myaccountbook.budgettracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
public class AccountBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate date;
    private BigDecimal amount;
    private String category;
    private String description;

    @Enumerated(EnumType.STRING) // ENUM 타입 매핑
    private Type type;

    public enum Type{
        INCOME, EXPENSE
    }


}
