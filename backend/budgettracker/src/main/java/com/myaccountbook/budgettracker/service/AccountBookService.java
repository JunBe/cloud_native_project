package com.myaccountbook.budgettracker.service;

import com.myaccountbook.budgettracker.entity.AccountBook;
import com.myaccountbook.budgettracker.entity.User;
import com.myaccountbook.budgettracker.repository.AccountBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountBookService {

    private AccountBookRepository accountBookRepository;

    public AccountBookService(AccountBookRepository accountBookRepository) {
        this.accountBookRepository = accountBookRepository;
    }

    public List<AccountBook> getAccountBooks(User user) {
        return accountBookRepository.findByUser(user);
    }

    public void addAccountBook(User user, AccountBook accountBook) {
        accountBook.setUser(user);
        accountBookRepository.save(accountBook);
    }

    @Transactional
    public void deleteAllAccountBook(User user) {
        accountBookRepository.deleteByUser(user);
    }
}
