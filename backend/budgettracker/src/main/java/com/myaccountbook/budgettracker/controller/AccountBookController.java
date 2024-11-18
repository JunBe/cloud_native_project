package com.myaccountbook.budgettracker.controller;

import com.myaccountbook.budgettracker.entity.AccountBook;
import com.myaccountbook.budgettracker.repository.AccountBookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/account-book")
public class AccountBookController {
    private final AccountBookRepository accountBookRepository;


    public AccountBookController(AccountBookRepository accountBookRepository) {
        this.accountBookRepository = accountBookRepository;
    }

    //리스트 조회
    @GetMapping
    public String getAccountBookEntries(Model model) {
        List<AccountBook> incomes = accountBookRepository.findByTypeOrderByDateAsc(AccountBook.Type.INCOME);
        List<AccountBook> expenses = accountBookRepository.findByTypeOrderByDateAsc(AccountBook.Type.EXPENSE);

        model.addAttribute("incomes", incomes);
        model.addAttribute("expenses", expenses);
        return "account-book";
    }

    //항목 추가 페이지 이동
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("newEntry", new AccountBook());
        return "add-account-book";
    }
    
    //항목 추가
    @PostMapping("/add")
    public String addAccountBookEntry(@ModelAttribute AccountBook accountBook) {
        accountBookRepository.save(accountBook);
        return "redirect:/account-book";
    }

    //항목 수정 페이지 이동
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        AccountBook accountBook = accountBookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 ID 입니다"));

        if (accountBook.getDate() == null) {
            accountBook.setDate(LocalDate.now());
        }

        model.addAttribute("accountBook", accountBook);
        return "edit-account-book";
    }
    
    //항목 수정
    @PostMapping("/update")
    public String updateAccountBookEntry(@ModelAttribute AccountBook accountBook) {
        accountBookRepository.save(accountBook);
        return "redirect:/account-book";
    }
    
    //항목 삭제
    @PostMapping("/delete/{id}")
    public String deleteAccountBookEntry(@PathVariable int id) {

        accountBookRepository.deleteById(id);
        return "redirect:/account-book";
    }

    //항목 모두 삭제
    @PostMapping("/delete")
    public String deleteAllAccountBookEntry() {
        accountBookRepository.deleteAll();
        return "redirect:/account-book";
    }

}
