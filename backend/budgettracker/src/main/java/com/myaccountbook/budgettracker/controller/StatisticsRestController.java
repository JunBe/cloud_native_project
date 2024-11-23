package com.myaccountbook.budgettracker.controller;

import com.myaccountbook.budgettracker.dto.CategoryStatistics;
import com.myaccountbook.budgettracker.entity.AccountBook;
import com.myaccountbook.budgettracker.entity.User;
import com.myaccountbook.budgettracker.repository.AccountBookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsRestController {
    private final AccountBookRepository accountBookRepository;

    public StatisticsRestController(AccountBookRepository accountBookRepository) {
        this.accountBookRepository = accountBookRepository;
    }

    //총 통계
    @GetMapping
    public ResponseEntity<?> getMonthlyStatics(@RequestParam int year,
                                               @RequestParam int month
                                               , HttpSession session
                                               ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        List<Object[]> rawStatistics = accountBookRepository.findMonthlyStatistics(user, year, month);
//---------------------------------------------------------총
        BigDecimal totalAmount = rawStatistics.stream()
                .map(row -> (BigDecimal) row[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CategoryStatistics> statistics = new ArrayList<>();
        for (Object[] row : rawStatistics) {
            String category = (String) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            BigDecimal percentage = amount.divide(totalAmount, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            AccountBook.Type type = (AccountBook.Type) row[2];
            statistics.add(new CategoryStatistics(category, amount, percentage, type.name()));
        }
        //------------------ 지출
        BigDecimal expenseTotalAmount = rawStatistics.stream()
                .filter(row -> "EXPENSE".equals(((AccountBook.Type) row[2]).name()))
                .map(row -> (BigDecimal) row[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CategoryStatistics> expenseStatistics = new ArrayList<>();
        for (Object[] row : rawStatistics) {
            AccountBook.Type type = (AccountBook.Type) row[2];
            if(type.name().equals("EXPENSE")) {
                String category = (String) row[0];
                BigDecimal amount = (BigDecimal) row[1];
                BigDecimal percentage = amount.divide(expenseTotalAmount, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                expenseStatistics.add(new CategoryStatistics(category, amount, percentage, type.name()));
            }
        }
        //----------------수입
        BigDecimal incomeTotalAmount = rawStatistics.stream()
                .filter(row -> "INCOME".equals(((AccountBook.Type) row[2]).name()))
                .map(row -> (BigDecimal) row[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CategoryStatistics> incomeStatistics = new ArrayList<>();
        for (Object[] row : rawStatistics) {
            AccountBook.Type type = (AccountBook.Type) row[2];
            if (type.name().equals("INCOME")) {
                String category = (String) row[0];
                BigDecimal amount = (BigDecimal) row[1];
                BigDecimal percentage = amount.divide(incomeTotalAmount, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                incomeStatistics.add(new CategoryStatistics(category, amount, percentage, type.name()));
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("statistics", statistics);
        response.put("incomeStatistics", incomeStatistics);
        response.put("expenseStatistics", expenseStatistics);
        response.put("incomeTotalAmount", incomeTotalAmount);
        response.put("expenseTotalAmount", expenseTotalAmount);
        response.put("totalAmount", incomeTotalAmount.subtract(expenseTotalAmount));

        return ResponseEntity.ok(response);
    }

}
