package com.myaccountbook.budgettracker.controller;

import com.myaccountbook.budgettracker.entity.AccountBook;
import com.myaccountbook.budgettracker.entity.User;
import com.myaccountbook.budgettracker.repository.AccountBookRepository;
import com.myaccountbook.budgettracker.service.AccountBookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account-book")
public class AccountBookRestController {
    private final AccountBookRepository accountBookRepository;
    private final AccountBookService accountBookService;

    public AccountBookRestController(AccountBookRepository accountBookRepository, AccountBookService accountBookService) {
        this.accountBookRepository = accountBookRepository;
        this.accountBookService = accountBookService;
    }

    @GetMapping
    public ResponseEntity<?> getAccountBooks(HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println("목록 받아오기 = "+user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        List<AccountBook> accountBooks = accountBookService.getAccountBooks(user);
        return ResponseEntity.ok(accountBooks);
    }

    @PostMapping
    public ResponseEntity<?> addAccountBook(@RequestBody AccountBook accountBook, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        accountBookService.addAccountBook(user, accountBook);
        return ResponseEntity.ok("생성 완료");
    }

    //수정 항목 조회
    @GetMapping("/{id}")
    public Optional<AccountBook> getEditAccountBookEntries(@PathVariable int id) {
        return accountBookRepository.findById(id);
    }

    //항목 수정
    @PutMapping("/{id}")
    public AccountBook updateAccountBookEntry(@PathVariable int id, @RequestBody AccountBook updatedAccountBook) {
        return accountBookRepository.findById(id)
                .map(accountBook -> {
                    accountBook.setDate(updatedAccountBook.getDate());
                    accountBook.setAmount(updatedAccountBook.getAmount());
                    accountBook.setCategory(updatedAccountBook.getCategory());
                    accountBook.setDescription(updatedAccountBook.getDescription());
                    accountBook.setType(updatedAccountBook.getType());
                    return accountBookRepository.save(accountBook);
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
    }
    
    //항목 삭제
    @DeleteMapping("/{id}")
    public void deleteAccountBookEntry(@PathVariable int id) {
        accountBookRepository.deleteById(id);
    }

    //항목 모두 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteAllAccountBookEntry(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        accountBookService.deleteAllAccountBook(user);
        return ResponseEntity.ok("전체 삭제 완료");
    }

}
