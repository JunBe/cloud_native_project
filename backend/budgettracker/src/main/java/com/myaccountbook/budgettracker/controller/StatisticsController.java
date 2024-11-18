package com.myaccountbook.budgettracker.controller;

import com.myaccountbook.budgettracker.dto.CategoryStatistics;
import com.myaccountbook.budgettracker.entity.AccountBook;
import com.myaccountbook.budgettracker.repository.AccountBookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
    private final AccountBookRepository accountBookRepository;

    public StatisticsController(AccountBookRepository accountBookRepository) {
        this.accountBookRepository = accountBookRepository;
    }

    //통계 페이지 이동
    @GetMapping
    public String showStaticsPage(Model model) {
        List<AccountBook> accountBooks = accountBookRepository.findAll();
        model.addAttribute("accountBooks", accountBooks);
        return "statics-account-book";
    }

    //총 통계
    @PostMapping
    public String getMonthlyStatics(@RequestParam int year,
                                    @RequestParam int month,
                                    Model model) {
        List<Object[]> rawStatistics = accountBookRepository.findMonthlyStatistics(year, month);
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


        model.addAttribute("incomeTotalAmount", incomeTotalAmount);
        model.addAttribute("expenseTotalAmount", expenseTotalAmount);
        model.addAttribute("incomeStatistics", incomeStatistics);
        model.addAttribute("expenseStatistics", expenseStatistics);
        model.addAttribute("totalAmount", incomeTotalAmount.subtract(expenseTotalAmount));
        model.addAttribute("statistics", statistics);
        model.addAttribute("year", year);
        model.addAttribute("month", month);

        return "statics-account-book";
    }

}
