package com.myaccountbook.budgettracker.dto;

import com.myaccountbook.budgettracker.entity.AccountBook;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CategoryStatistics {
    private String category;
    private BigDecimal amount;
    private BigDecimal percentage;
    private String type;

    public CategoryStatistics(String category, BigDecimal amount, BigDecimal percentage, String type) {
        this.category = category;
        this.amount = amount;
        this.percentage = percentage;
        this.type = type;
    }


}
